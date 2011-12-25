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
package it.tidalwave.uniformity.action;

import java.awt.event.ActionEvent;
import javax.annotation.Nonnull;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import it.tidalwave.uniformity.UniformityCheckRequest;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ActionID(id = "it.tidalwave.uniformity.action.UniformityCheckAction", category = "Tools")
@ActionRegistration(displayName = "#CTL_UniformityCheckAction",
                    iconBase = "it/tidalwave/uniformity/action/Uniformity.png")
@ActionReferences(value =
  {
    @ActionReference(path = "Menu/Tools", position = 2500),
    @ActionReference(path = "Toolbars/Standard", position = 2500)
  })
public final class UniformityCheckAction extends AbstractAction
  {

    @Override
    public void actionPerformed (final @Nonnull ActionEvent event)
      {
        new UniformityCheckRequest().send();
      }
  }
