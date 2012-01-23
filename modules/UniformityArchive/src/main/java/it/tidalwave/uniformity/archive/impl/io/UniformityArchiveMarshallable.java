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
package it.tidalwave.uniformity.archive.impl.io;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStream;
import it.tidalwave.role.Marshallable;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.archive.impl.UniformityArchive;
import lombok.RequiredArgsConstructor;
import static it.tidalwave.uniformity.archive.SortCriteria.*;
import static it.tidalwave.role.Marshallable.Marshallable;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
public class UniformityArchiveMarshallable implements Marshallable
  {
    @Nonnull
    private final UniformityArchive archive;
    
    @Override
    public void marshal (final @Nonnull OutputStream os)
      throws IOException 
      {
        for (final UniformityMeasurements measurements : archive.findMeasurements().sort(BY_DATE_TIME).results())
          {
            measurements.as(Marshallable).marshal(os);
          }
        
        os.close();
      }
  }
