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
package it.tidalwave.uniformity.main.ui.impl.netbeans;

import javax.annotation.Nonnull;
import java.awt.BorderLayout;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle;
import it.tidalwave.actor.spi.ActorGroupActivator;
import it.tidalwave.uniformity.UniformityCheckMainControllerActorActivator;
import lombok.Getter;

/***********************************************************************************************************************
 * 
 * This {@link TopComponent} acts as both a Presentation, holding the display estate, and as an Adapter, activating
 * and deactivating the Controller Actor when the NetBeans Platform opens and closes it.
 * 
 * @stereotype Presentation
 * @stereotype Adapter
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@TopComponent.Description(preferredID = "UniformityCheckMainTopComponent", persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Tools", id = "it.tidalwave.uniformity.main.ui.impl.swing.UniformityCheckMainTopComponent")
@ActionReferences(value =
  {
    @ActionReference(path = "Menu/Tools", position = 2500),
    @ActionReference(path = "Toolbars/Standard", position = 2500)
  })
@TopComponent.OpenActionRegistration(displayName = "#CTL_UniformityCheckMainAction", preferredID = "UniformityCheckMainTopComponent")
public final class UniformityCheckMainTopComponent extends TopComponent
  {
    @Getter @Nonnull
    private final UniformityCheckMainPanel panel;
    
    private final ActorGroupActivator activator = new UniformityCheckMainControllerActorActivator();
            
    public UniformityCheckMainTopComponent() 
      {
        setName(NbBundle.getMessage(UniformityCheckMainTopComponent.class, "CTL_UniformityCheckMainTopComponent"));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        setLayout(new BorderLayout());
        add(panel = new UniformityCheckMainPanel(), BorderLayout.CENTER);
      }

    @Override
    public void componentOpened()
      {
        activator.activate();
      }

    @Override
    public void componentClosed() 
      {
        activator.deactivate();
      }
  }
