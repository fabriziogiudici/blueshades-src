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
package it.tidalwave.uniformity.archive.impl.io;

import it.tidalwave.colorimetry.ColorTemperature;
import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import it.tidalwave.role.Unmarshallable;
import it.tidalwave.uniformity.Position;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurements;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class UniformityMeasurementsUnmarshallable implements Unmarshallable
  {
    private static final Pattern PATTERN_DISPLAY_NAME = Pattern.compile("D='([^']*)'");
    private static final Pattern PATTERN_LUMINANCE = Pattern.compile("L\\[([0-9]*),([0-9]*)\\]=([0-9]*)");
    private static final Pattern PATTERN_TEMPERATURE = Pattern.compile("T\\[([0-9]*),([0-9]*)\\]=([0-9]*)");
    
    @Override
    public UniformityMeasurements unmarshal (final @Nonnull InputStream is) 
      throws IOException
      {  
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));
        final String s = br.readLine();
        
        if (s == null)
          {
            throw new EOFException();  
          }
        
        final Scanner scanner = new Scanner(s.trim()).useDelimiter(" ");
        final DateTime dateTime = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(scanner.next());
        
        String displayName = "";
        int luminance = 0;
        final Map<Position, UniformityMeasurement> map = new TreeMap<Position, UniformityMeasurement>();
        
        while (scanner.hasNext())
          {  
            final String t = scanner.next();
              System.err.println("SCANNED " + t);
            
            final Matcher displayNameMatcher = PATTERN_DISPLAY_NAME.matcher(t);
            
            if (displayNameMatcher.matches())
              {
                displayName = displayNameMatcher.group(1);
                continue;
              }
            
            final Matcher luminanceMatcher = PATTERN_LUMINANCE.matcher(t);
            
            if (luminanceMatcher.matches())
              {
//                final Position pos = Position.pos(Integer.parseInt(m2.group(1)), Integer.parseInt(m2.group(2)));
                luminance = Integer.parseInt(luminanceMatcher.group(3));
                continue;
              }
            
            final Matcher temperatureMatcher = PATTERN_TEMPERATURE.matcher(t);
            
            if (temperatureMatcher.matches())
              {
                final Position pos = Position.pos(Integer.parseInt(temperatureMatcher.group(1)), Integer.parseInt(temperatureMatcher.group(2)));
                final ColorTemperature temperature = ColorTemperature.kelvin(Integer.parseInt(temperatureMatcher.group(3)));
                map.put(pos, new UniformityMeasurement(temperature, luminance));
                continue;
              }
          }

        return new UniformityMeasurements(displayName, dateTime, map);
      }    
  } 
