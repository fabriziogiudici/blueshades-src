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

import javax.swing.JFrame;
import it.tidalwave.uniformity.ui.UniformityCheckPresentation;
import lombok.Delegate;

/***********************************************************************************************************************
 * 
 * FIXME: a separate base class which implements @Delegate methods. If you put everything together with 
 * UniformityCheckPresentationWindow, strange compilation errors occur (also elsewhere) probably because of a Lombok
 * bug.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
abstract class UniformityCheckPresentationWindowSupport implements UniformityCheckPresentation
  {
    protected final JFrame frame = new JFrame();
    
    @Delegate(types=UniformityCheckPresentation.class)
    protected final UniformityCheckPresentationPanel panel = new UniformityCheckPresentationPanel();
    
    protected UniformityCheckPresentationWindowSupport()
      {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true); 
        frame.add(panel);
      }
  }
