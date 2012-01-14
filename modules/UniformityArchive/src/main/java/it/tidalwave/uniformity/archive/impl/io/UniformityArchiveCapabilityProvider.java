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
package it.tidalwave.uniformity.archive.impl.io;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import org.openide.util.lookup.ServiceProvider;
import it.tidalwave.netbeans.capabilitiesprovider.CapabilitiesProvider;
import it.tidalwave.netbeans.capabilitiesprovider.CapabilitiesProviderSupport;
import it.tidalwave.uniformity.archive.impl.UniformityArchive;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ServiceProvider(service=CapabilitiesProvider.class)
public class UniformityArchiveCapabilityProvider extends CapabilitiesProviderSupport<UniformityArchive>
  {
    @Override @Nonnull
    public Collection<? extends Object> createCapabilities (final @Nonnull UniformityArchive owner) 
      {
        return Arrays.asList(new UniformityArchiveMarshallable(owner), new UniformityArchiveUnmarshallable());
      }
  }
