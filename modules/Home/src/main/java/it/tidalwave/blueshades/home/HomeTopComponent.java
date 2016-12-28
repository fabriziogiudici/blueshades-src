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
package it.tidalwave.blueshades.home;

import java.awt.BorderLayout;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@TopComponent.Description(preferredID = "HomeTopComponent", 
                          iconBase = "it/tidalwave/blueshades/home/home-icon-th.png",
                          persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "it.tidalwave.blueshades.home.HomeTopComponent")
@ActionReferences(value =
  {
    @ActionReference(path = "Toolbars/Standard", position = 10)
  })
@TopComponent.OpenActionRegistration(displayName = "#CTL_HomeAction", preferredID = "HomeTopComponent")
public final class HomeTopComponent extends TopComponent 
  {
    public HomeTopComponent() 
      {
        setName(NbBundle.getMessage(HomeTopComponent.class, "CTL_HomeTopComponent"));
        putClientProperty(PROP_DRAGGING_DISABLED, true);
        putClientProperty(PROP_MAXIMIZATION_DISABLED, true);
        putClientProperty(PROP_UNDOCKING_DISABLED, true);
        setLayout(new BorderLayout());
        add(new HomePanel(), BorderLayout.CENTER);
      }
  }
