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
package it.tidalwave.uniformity.measurement.ui.impl.netbeans;

import javax.annotation.Nonnull;
import org.openide.util.lookup.ServiceProvider;
import it.tidalwave.blueargyle.util.SwingSafeComponentBuilder;
import it.tidalwave.uniformity.measurement.ui.UniformityCheckMeasurementPresentationProvider;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.blueargyle.util.SwingSafeComponentBuilder.*;

/***********************************************************************************************************************
 * 
 * @stereotype Factory
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ServiceProvider(service=UniformityCheckMeasurementPresentationProvider.class) @Slf4j
public class NetBeansUniformityCheckMeasurementPresentationProvider implements UniformityCheckMeasurementPresentationProvider
  {
    private final SwingSafeComponentBuilder<NetBeansUniformityCheckMeasurementPresentation> builder = builderFor(NetBeansUniformityCheckMeasurementPresentation.class);
    
    @Override @Nonnull
    public NetBeansUniformityCheckMeasurementPresentation getPresentation()
      {
        return builder.getInstance();
      }
  }
