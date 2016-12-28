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
package it.tidalwave.blueshades.profileevaluation.ui.impl.main.netbeans;

import java.awt.EventQueue;
import it.tidalwave.blueshades.profileevaluation.ui.main.ProfileEvaluationMainPresentation;
import org.openide.windows.WindowManager;
import lombok.Delegate;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class NetBeansProfileEvaluationMainPresentation implements ProfileEvaluationMainPresentation
  {
    @Delegate(types=ProfileEvaluationMainPresentation.class)
    protected final ProfileEvaluationMainPresentation panel;
    
    protected final ProfileEvaluationMainTopComponent topComponent;
    
    public NetBeansProfileEvaluationMainPresentation()
      {
        assert EventQueue.isDispatchThread();
        topComponent = (ProfileEvaluationMainTopComponent)WindowManager.getDefault().findTopComponent("ProfileEvaluationMainTopComponent");
        panel = topComponent.getContent();
      }
  }
