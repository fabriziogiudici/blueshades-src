/***********************************************************************************************************************
 *
 * blueShades - a Java UI for Argyll
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
 * WWW: http://blueshades.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueshades-src
 *
 **********************************************************************************************************************/
package it.tidalwave.uniformity.archive.impl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import it.tidalwave.util.Finder;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.colorimetry.Display;
import it.tidalwave.netbeans.util.AsLookupSupport;
import it.tidalwave.uniformity.UniformityMeasurements;
import lombok.EqualsAndHashCode;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@EqualsAndHashCode(callSuper=false)
public class UniformityArchive extends AsLookupSupport
  {
    private final Set<UniformityMeasurements> contents = new HashSet<UniformityMeasurements>();

    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    public void add (final @Nonnull UniformityMeasurements measurements) 
      {
        contents.add(measurements);
      }

    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    @Nonnull
    public Finder<UniformityMeasurements> findMeasurements() 
      {
        return new SimpleFinderSupport<UniformityMeasurements>("measurements") 
          {
            @Override @Nonnull
            protected List<? extends UniformityMeasurements> computeResults()  
              {
                return new ArrayList<UniformityMeasurements>(contents);
              }                      
          };
      }
    
    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    @Nonnull
    public Finder<UniformityMeasurements> findMeasurementsByDisplay (final @Nonnull Display display) 
      {
        return new SimpleFinderSupport<UniformityMeasurements>("measurements") 
          {
            @Override @Nonnull
            protected List<? extends UniformityMeasurements> computeResults()  
              {
                final ArrayList<UniformityMeasurements> result = new ArrayList<UniformityMeasurements>();
                
                for (final UniformityMeasurements measurements : contents)
                  { 
                    if (measurements.getDisplay().getDisplay().getDisplayName().equals(display.getDisplayName()))
                      {
                        result.add(measurements);  
                      }
                  }
                
                return result;
              }                      
          };
      }
    
    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    public void clear() 
      {
        contents.clear();
      }
  }
