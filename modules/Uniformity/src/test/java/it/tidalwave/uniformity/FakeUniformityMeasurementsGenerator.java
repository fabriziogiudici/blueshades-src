/***********************************************************************************************************************
 *
 * blueArgyle - a Java UI for Argyll
 * Copyright (C) 2011-2012 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.uniformity;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import org.joda.time.DateTime;
import it.tidalwave.colorimetry.ColorTemperature;
import it.tidalwave.argyll.Display;
import lombok.NoArgsConstructor;
import static it.tidalwave.uniformity.Position.xy;
import static it.tidalwave.colorimetry.ColorTemperature.kelvin;
import static lombok.AccessLevel.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access=PRIVATE)
public final class FakeUniformityMeasurementsGenerator 
  {
    @Nonnull
    public static UniformityMeasurements createMeasurements (final @Nonnull String displayName, final @Nonnull Random r)
      {
        final DateTime dateTime = new DateTime((40L * 365 * 24 * 60 * 60 + r.nextLong() % (365L * 24 * 60 * 60)) * 1000);
        final SortedMap<Position, UniformityMeasurement> m = new TreeMap<Position, UniformityMeasurement>();
        m.put(xy(0, 0), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
        m.put(xy(1, 0), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
        m.put(xy(2, 0), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
        m.put(xy(0, 1), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
        m.put(xy(1, 1), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
        m.put(xy(2, 1), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
        m.put(xy(0, 2), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
        m.put(xy(1, 2), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
        m.put(xy(2, 2), new UniformityMeasurement(randomTemperature(r), randomLuminance(r)));
       
        return new UniformityMeasurements(new Display(displayName, 0), dateTime, m);
      }  
    
    @Nonnull
    private static ColorTemperature randomTemperature (final @Nonnull Random r)
      {
        return kelvin(2000 + r.nextInt(6000));  
      }
    
    @Nonnegative
    private static int randomLuminance (final @Nonnull Random r)
      {
        return r.nextInt(200);  
      }
  }
