/***********************************************************************************************************************
 *
 * blueArgyle - a Java UI for Argyll
 * Copyright (C) 2011-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * WWW: http://blueargyle.java.net
 * SCM: https://bitbucket.org/tidalwave/blueargyle-src
 *
 **********************************************************************************************************************/
package it.tidalwave.argyll.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Scanner;
import java.io.IOException;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.colorimetry.ColorPoint;
import it.tidalwave.colorimetry.ColorPoints;
import it.tidalwave.colorimetry.ColorTemperature;
import it.tidalwave.colorimetry.MeasureWithPrecision;
import it.tidalwave.argyll.ArgyllFailureMessage;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.argyll.SensorOperationInvitationMessage;
import it.tidalwave.blueargyle.util.Executor;
import it.tidalwave.blueargyle.util.Executor.ConsoleOutput;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.colorimetry.ColorPoint.*;
import static it.tidalwave.colorimetry.ColorTemperature.*;
import static it.tidalwave.colorimetry.MeasureWithPrecision.*;

/***********************************************************************************************************************
 * 
 * This actor wraps the functions provided by the {@code spotread} executable.
 * 
 * @stereotype Actor
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Actor(threadSafe=false) @NotThreadSafe @Slf4j
public class SpotReadActor 
  {
    private final static String COMMAND_DO_MEASUREMENT = "";
    private final static String COMMAND_QUIT = "QQ";
    
    /*******************************************************************************************************************
     * 
     * Answers to the request for a measurement.
     * 
     ******************************************************************************************************************/
    public void spotRead (final @ListensTo @Nonnull MeasurementRequest message)
      throws InterruptedException
      {
        try
          {
            log.info("spotRead({})", message);

            // FIXME: -yl is required e.g. by the old EyeOne, but not tolerated by the x-rite i1 Display
            final Executor executor = Executor.forExecutable("spotread")
                                            .withArgument("-T");
//                                            .withArgument("-yl");
            executor.start().getStdout().waitFor("(^.*to do a calibration.*$)").clear();
            executor.send(COMMAND_DO_MEASUREMENT).getStdout().waitFor("(^.*to do a calibration.*$)");
            executor.send(COMMAND_QUIT).waitForCompletion();

            if (!executor.getStdout().filteredBy("(.*Ambient filter should be removed.*)").isEmpty())
              {
                new SensorOperationInvitationMessage("Please remove the ambient filter").send();
              }
            else
              {
                parseMessage(executor.getStdout()).send();
              }  
          }
        catch (IOException e)
          {
            new ArgyllFailureMessage(e).send(); 
          }
      }
    
    /*******************************************************************************************************************
     * 
     * 
     * 
     ******************************************************************************************************************/
    @Nonnull
    private static MeasurementMessage parseMessage (final @Nonnull ConsoleOutput stdout)
      {
        // Result is XYZ: 6.678702 5.688142 1.342970, D50 Lab: 28.611635 13.050721 26.227069
        final Scanner measure = stdout.filteredAndSplitBy("^ *Result is (XYZ:.*$)", "[ ,]"); // FIXME: try ^[0123456789.]+
        
        measure.next(); // XYZ:
        final ColorPoint xyz = colorXYZ(measure.nextDouble(), measure.nextDouble(), measure.nextDouble());
        measure.next(); // blank
        measure.next(); // D50
        measure.next(); // Lab:
        final ColorPoint lab = colorLab(measure.nextDouble(), measure.nextDouble(), measure.nextDouble());
        
        final ColorPoints colorPoints = new ColorPoints(lab, xyz);
        
        // CCT = 2390K (Delta E 0.202257)
        final MeasureWithPrecision<ColorTemperature> ccTemp = parseTemperature(stdout, "^ *CCT *= *(.*$)");
        // Closest Planckian temperature = 2389K (Delta E 0.110858)
        final MeasureWithPrecision<ColorTemperature> planckianTemp = parseTemperature(stdout, "^ *Closest Planckian temperature *= *(.*$)");
        // Closest Daylight temperature  = 2244K (Delta E 13.893159)
        final MeasureWithPrecision<ColorTemperature> daylightTemp = parseTemperature(stdout, "^ *Closest Daylight temperature *= *(.*$)");
        
        log.info("Color:      {}", colorPoints);
        log.info("CCT:        {}", ccTemp);
        log.info("Planck T:   {}", planckianTemp);
        log.info("DayLight T: {}", daylightTemp);

        return new MeasurementMessage(colorPoints, ccTemp, planckianTemp, daylightTemp);
      }
    
    /*******************************************************************************************************************
     * 
     * Answers to the request for a measurement.
     * 
     ******************************************************************************************************************/
    @Nonnull
    private static MeasureWithPrecision<ColorTemperature> parseTemperature (final @Nonnull ConsoleOutput output, 
                                                                            final @Nonnull String regexp)
      {
        final Scanner scanner = output.filteredAndSplitBy(regexp, "[ (K)]");
        final int t = scanner.nextInt();
        scanner.next(); // K
        scanner.next(); // blank
        scanner.next(); // Delta
        scanner.next(); // E
        final double de = scanner.nextDouble();
        return measureWithPrecision(kelvin(t), de);
      }
  }
