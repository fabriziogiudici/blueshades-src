/***********************************************************************************************************************
 *
 * blueArgyle - a Java UI for Argyll
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
 * WWW: http://blueargyle.java.net
 * SCM: https://bitbucket.org/tidalwave/blueargyle-src
 *
 **********************************************************************************************************************/
package it.tidalwave.blueargyle.profileevaluation.ui.impl.main;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.NotThreadSafe;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.argyll.Display;
import it.tidalwave.argyll.ProfiledDisplay;
import it.tidalwave.blueargyle.profileevaluation.ProfileEvaluationRequest;
import it.tidalwave.blueargyle.profileevaluation.ui.main.ProfileEvaluationMainPresentation;
import it.tidalwave.blueargyle.profileevaluation.ui.main.ProfileEvaluationMainPresentationProvider;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @stereotype Controller
 * @stereotype Actor
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Actor(threadSafe=false) @NotThreadSafe @Slf4j
public class ProfileEvaluationMainControllerActor
  {
    // FIXME: use @Inject
    private final ProfileEvaluationMainPresentationProvider presentationProvider = Locator.find(ProfileEvaluationMainPresentationProvider.class);
    
    /** The presentation controlled by this class */
    private ProfileEvaluationMainPresentation presentation;
    
    private ProfiledDisplay selectedDisplay = new ProfiledDisplay(new Display("dummy", 0), "dummy"); // FIXME
    
    /*******************************************************************************************************************
     * 
     * 
     *
     ******************************************************************************************************************/
    private final Action startAction = new AbstractAction("Start") 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            new ProfileEvaluationRequest(selectedDisplay).send();
          }
      };
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @PostConstruct
    public void initialize()
      {
        log.info("initialize()");
        presentation = presentationProvider.getPresentation();
        presentation.bind(startAction);        
      }
  }
