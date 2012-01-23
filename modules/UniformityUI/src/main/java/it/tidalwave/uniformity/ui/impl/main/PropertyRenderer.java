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
 * WWW: http://blueargyle.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueargyle-src
 *
 **********************************************************************************************************************/
package it.tidalwave.uniformity.ui.impl.main;

import javax.annotation.Nonnull;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentation;
import lombok.RequiredArgsConstructor;
import static it.tidalwave.uniformity.Position.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
abstract class PropertyRenderer
  {
    @Nonnull
    private final UniformityCheckMainPresentation presentation;

    @Nonnull
    private final String upperFormat;

    @Nonnull
    private final String lowerFormat;

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void render (@Nonnull UniformityMeasurements measurements)
      {
        final int columns = measurements.getColumns();
        final int rows = measurements.getRows();
        final UniformityMeasurement centerMeasurement = measurements.getAt(xy(columns / 2, rows / 2));

        final String[][] s = new String[rows][columns];

        for (int row = 0; row < rows; row++)  
          {
            for (int column = 0; column < columns; column++)
              {
                s[row][column] = formatMeasurement(centerMeasurement, measurements.getAt(xy(column, row)));
              }
          }

        presentation.populateMeasurements(s);
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    protected String formatMeasurement (final @Nonnull UniformityMeasurement centerMeasurement,
                                        final @Nonnull UniformityMeasurement measurement)
      {
        final double centerValue = getValue(centerMeasurement);  
        final double value = getValue(measurement);
        final double delta = value - centerValue;

        final StringBuilder buffer = new StringBuilder();
        buffer.append(String.format(upperFormat, value));

        if (centerMeasurement != measurement)
          {
            buffer.append(String.format(lowerFormat, delta, 100.0 * delta / centerValue));
          }

        return buffer.toString();
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    protected abstract double getValue (@Nonnull UniformityMeasurement measurement);
  }
