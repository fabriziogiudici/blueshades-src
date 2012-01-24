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
package it.tidalwave.blueshades.profileevaluation.ui.impl.sequence.netbeans;

import javax.annotation.Nonnull;
import it.tidalwave.swing.SwingSafeComponentBuilder;
import it.tidalwave.blueshades.profileevaluation.ui.sequence.ProfileEvaluationSequencePresentation;
import it.tidalwave.blueshades.profileevaluation.ui.sequence.ProfileEvaluationSequencePresentationProvider;
import org.openide.util.lookup.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.swing.SwingSafeComponentBuilder.*;

/***********************************************************************************************************************
 * 
 * @stereotype Factory
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ServiceProvider(service=ProfileEvaluationSequencePresentationProvider.class) @Slf4j
public class NetBeansProfileEvaluationSequencePresentationProvider implements ProfileEvaluationSequencePresentationProvider
  {
    private final SwingSafeComponentBuilder<NetBeansProfileEvaluationSequencePresentation, ProfileEvaluationSequencePresentation> builder = 
            builderFor(NetBeansProfileEvaluationSequencePresentation.class, ProfileEvaluationSequencePresentation.class);
    
    @Override @Nonnull
    public ProfileEvaluationSequencePresentation getPresentation()
      {
        return builder.getInstance();
      }
  }