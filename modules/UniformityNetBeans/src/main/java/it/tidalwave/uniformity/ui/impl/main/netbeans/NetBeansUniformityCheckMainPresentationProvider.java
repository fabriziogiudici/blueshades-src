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
package it.tidalwave.uniformity.ui.impl.main.netbeans;

import javax.annotation.Nonnull;
import org.openide.util.lookup.ServiceProvider;
import it.tidalwave.swing.SwingSafeComponentBuilder;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentationProvider;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.swing.SwingSafeComponentBuilder.*;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentation;

/***********************************************************************************************************************
 * 
 * @stereotype Factory
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ServiceProvider(service=UniformityCheckMainPresentationProvider.class) @Slf4j
public class NetBeansUniformityCheckMainPresentationProvider implements UniformityCheckMainPresentationProvider
  {
    private final SwingSafeComponentBuilder<NetBeansUniformityCheckMainPresentation, UniformityCheckMainPresentation> builder = 
            builderFor(NetBeansUniformityCheckMainPresentation.class, UniformityCheckMainPresentation.class);
    
    @Override @Nonnull
    public UniformityCheckMainPresentation getPresentation()
      {
        return builder.getInstance();
      }
  }