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
package it.tidalwave.uniformity.ui.impl.swing;

import javax.annotation.Nonnull;
import javax.swing.Action;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import static it.tidalwave.uniformity.ui.impl.swing.SafeRunner.*;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class UniformityCheckPresentationWindow extends UniformityCheckPresentationWindowSupport
  {    
    @Override
    public void showUp()
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                panel.showUp();
                final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                final GraphicsDevice graphicsDevice = graphicsEnvironment.getScreenDevices()[0];
                graphicsDevice.setFullScreenWindow(frame);
                frame.setVisible(true);
              }
          });
      }
    
    @Override
    public void dismiss()
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                frame.setVisible(false);
                frame.dispose();
                panel.dismiss();
              }
          });
      }
  }
