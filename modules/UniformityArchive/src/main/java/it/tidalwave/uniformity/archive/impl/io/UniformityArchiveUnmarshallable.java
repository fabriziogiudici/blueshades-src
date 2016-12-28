/***********************************************************************************************************************
 *
 * blueShades - a Java UI for Argyll
 * Copyright (C) 2011-2016 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.uniformity.archive.impl.io;

import javax.annotation.Nonnull;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import it.tidalwave.role.Unmarshallable;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.archive.impl.UniformityArchive;
import static it.tidalwave.role.Unmarshallable.Unmarshallable;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class UniformityArchiveUnmarshallable implements Unmarshallable
  {
    @Override @Nonnull
    public UniformityArchive unmarshal (final @Nonnull InputStream is) 
      throws IOException
      {
        final UniformityArchive archive = new UniformityArchive();
        
        for (;;)
          {
            try
              {
                final UniformityMeasurements measurements = new UniformityMeasurements().as(Unmarshallable).unmarshal(is);
                archive.add(measurements);
              }
            catch (EOFException e)
              {
                break; // ok, finished  
              }
          }
        
        return archive;
      }
  }
