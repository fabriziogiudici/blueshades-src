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
package it.tidalwave.uniformity.ui.impl.main.netbeans;

import java.awt.EventQueue;
import org.openide.windows.WindowManager;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentation;
import lombok.Delegate;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class NetBeansUniformityCheckMainPresentation implements UniformityCheckMainPresentation
  {
    private static interface DelegateExclusions
      {
        public void showUp();
        public void dismiss();    
      }
    
    protected final UniformityCheckMainTopComponent topComponent;
    
    @Delegate(types=UniformityCheckMainPresentation.class, excludes=DelegateExclusions.class)
    protected final UniformityCheckMainPanel panel;
    
    public NetBeansUniformityCheckMainPresentation()
      {
        assert EventQueue.isDispatchThread();
        topComponent = (UniformityCheckMainTopComponent)WindowManager.getDefault().findTopComponent("UniformityCheckMainTopComponent");
        panel = topComponent.getContent();
      }
    
    @Override
    public void showUp() 
      {
        assert EventQueue.isDispatchThread();
        panel.showUp();
        topComponent.requestActive();
      }

    @Override
    public void dismiss()
      {
        assert EventQueue.isDispatchThread();
        topComponent.close();
        panel.dismiss();
      }
  }
