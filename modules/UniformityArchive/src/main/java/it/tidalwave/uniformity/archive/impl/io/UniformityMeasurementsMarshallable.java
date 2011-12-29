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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import it.tidalwave.role.Marshallable;
import it.tidalwave.uniformity.Position;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurements;
import lombok.RequiredArgsConstructor;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class UniformityMeasurementsMarshallable implements Marshallable
  {
    @Nonnull
    private final UniformityMeasurements uniformityMeasurements;
    
    @Override
    public void marshal (final @Nonnull OutputStream os)
      throws IOException 
      {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        final int rows = uniformityMeasurements.getRows();
        final int columns = uniformityMeasurements.getColumns();
        final DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();
        pw.printf("%s D='%s'", formatter.print(uniformityMeasurements.getDateTime()), uniformityMeasurements.getDisplayName());

        for (int row = 0; row < rows; row++)
          {
            for (int column = 0; column < columns; column++)
              {
                final UniformityMeasurement measurement = uniformityMeasurements.getAt(Position.pos(column, row)); 
                pw.printf(" L[%d,%d]=%3d", column, row, measurement.getLuminance());
                pw.printf(" T[%d,%d]=%5d", column, row, measurement.getTemperature().getT());
              }
          }

        pw.println();
        pw.close();
        os.write(sw.toString().getBytes()); // FIXME: charset
      }    
  } 
