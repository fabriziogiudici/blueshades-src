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
package it.tidalwave.uniformity.archive;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import it.tidalwave.util.Finder;
import it.tidalwave.util.Finder.SortDirection;
import it.tidalwave.uniformity.UniformityMeasurements;
import lombok.NoArgsConstructor;
import static lombok.AccessLevel.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access=PRIVATE)
public final class SortCriteria
  {
    public final static Finder.SortCriterion BY_DATE_TIME = new Finder.FilterSortCriterion<UniformityMeasurements>() 
      {
        @Override
        public void sort (final @Nonnull List<? extends UniformityMeasurements> results, 
                          final @Nonnull SortDirection sortDirection)
          {
            Collections.sort(results, new Comparator<UniformityMeasurements>()
              {
                @Override
                public int compare (final @Nonnull UniformityMeasurements m1, final @Nonnull UniformityMeasurements m2) 
                  {
                    return m1.getDateTime().compareTo(m2.getDateTime()) * sortDirection.intValue();
                  }
              });
          }
      };
  }
