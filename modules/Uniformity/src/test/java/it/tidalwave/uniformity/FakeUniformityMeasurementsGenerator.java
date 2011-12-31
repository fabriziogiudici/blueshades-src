/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author fritz
 */
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
