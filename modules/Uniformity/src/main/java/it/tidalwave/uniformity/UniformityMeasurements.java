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
package it.tidalwave.uniformity;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.joda.time.DateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable @EqualsAndHashCode(exclude="dateTime") // FIXME: dateTime temporarily excluded for testing problems
public class UniformityMeasurements
  {
    @Getter
    private final String displayName;
            
    @Getter
    private final DateTime dateTime;
    
    @Nonnegative @Getter
    private final int columns;
   
    @Nonnegative @Getter
    private final int rows;
    
    @Nonnull
    private final SortedMap<Position, UniformityMeasurement> measurementMapByPosition;
    
    public UniformityMeasurements (final @Nonnull String displayName,
                                   final @Nonnull Map<Position, UniformityMeasurement> measurementMapByPosition)
      {
        this(displayName, new DateTime(), measurementMapByPosition);
      }
    
    public UniformityMeasurements (final @Nonnull String displayName,
                                   final @Nonnull DateTime dateTime,
                                   final @Nonnull Map<Position, UniformityMeasurement> measurementMapByPosition)
      {
        this.displayName = displayName;
        this.dateTime = dateTime;
        this.measurementMapByPosition = new TreeMap<Position, UniformityMeasurement>(measurementMapByPosition);
        
        int c = 0, r = 0;
        
        for (final Position pos : measurementMapByPosition.keySet())
          {
            c = Math.max(c, pos.column + 1);
            r = Math.max(r, pos.row + 1);
          }
        
        this.columns = c;
        this.rows = r;
      }
    
    @Nonnull
    public UniformityMeasurement getAt (final @Nonnull Position position)
      {
        return measurementMapByPosition.get(position);  
      }
    
    @Override @Nonnull 
    public String toString()
      {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        
        pw.printf("UniformityMeasurements('%s', %s\n", displayName, dateTime);
        
        for (final Entry<Position, UniformityMeasurement> e : measurementMapByPosition.entrySet())
          {
            pw.printf("  %s - %s\n", e.getKey(), e.getValue());
          }
        
        pw.println(")");
        pw.close();
        
        return sw.toString();
      }   
  }
