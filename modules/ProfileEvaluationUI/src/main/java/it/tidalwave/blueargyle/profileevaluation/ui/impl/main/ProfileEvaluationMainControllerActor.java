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
import java.util.Collection;
import java.util.Collections;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.nodes.Node;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.util.Finder;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.swing.ActionMessageAdapter;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.actor.RepeatingMessageSender;
import it.tidalwave.argyll.*;
import it.tidalwave.blueargyle.profileevaluation.ProfileEvaluationRequest;
import it.tidalwave.blueargyle.profileevaluation.ui.main.ProfileEvaluationMainPresentation;
import it.tidalwave.blueargyle.profileevaluation.ui.main.ProfileEvaluationMainPresentationProvider;
import it.tidalwave.netbeans.nodes.LookupFilterDecoratorNode;
import it.tidalwave.netbeans.nodes.NodePresentationModel;
import it.tidalwave.netbeans.nodes.role.ActionProvider;
import lombok.RequiredArgsConstructor;
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
    
    private ProfiledDisplay selectedDisplay;
    
    /** The requestor sending discovery messages for displays at initialization. */
    private final RepeatingMessageSender displayDiscoveryRequestor = new RepeatingMessageSender();
    
    /*******************************************************************************************************************
     * 
     * The ActionProvider for displays.
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    private static class DisplayActionProvider implements ActionProvider
      {
        @Nonnull
        private final ProfiledDisplay display;
        
        @Override @Nonnull
        public Action getPreferredAction() 
          {
            return new ActionMessageAdapter("Select", new DisplaySelectionMessage(display));
          }

        @Override @Nonnull
        public Collection<? extends Action> getActions() 
          {
            return Collections.<Action>emptyList();
          }
      };
    
    /*******************************************************************************************************************
     * 
     * Injects some capabilities into the PresentationModel for Displays. 
     *
     ******************************************************************************************************************/
    private final LookupFilterDecoratorNode.LookupFilter displaysCapabilityInjectorLookupFilter = new LookupFilterDecoratorNode.LookupFilter() 
      {
        @Override @Nonnull
        public Lookup filter (final @Nonnull Lookup lookup)
          {
            final ProfiledDisplay display = lookup.lookup(ProfiledDisplay.class);        
            return (display == null) ? lookup // e.g. the root node 
                                      : new ProxyLookup(Lookups.fixed(new DisplayActionProvider(display)), lookup);
          }
      };
    
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
        presentation.showWaitingOnDisplayList();
        displayDiscoveryRequestor.start(new DisplayDiscoveryQueryMessage());
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void onDiscoveredDisplays (final @ListensTo @Nonnull DisplayDiscoveryMessage message)
      {
        log.info("onDiscoveredDisplays({})", message);
        displayDiscoveryRequestor.stop();
        populateDisplays(message.findDisplays());
//        presentation.selectFirstDisplay();
        presentation.hideWaitingOnDisplayList();
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void onDisplaySelection (final @ListensTo @Nonnull DisplaySelectionMessage message)
      {
        log.info("onDisplaySelection({})", message);
        selectedDisplay = message.getSelectedDisplay();
//        presentation.showWaitingOnMeasurementsArchive();
//        archivedMeasurementsRequestor.start(new UniformityArchiveQuery(selectedDisplay.getDisplay())); 
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void populateDisplays (final @Nonnull Finder<ProfiledDisplay> finder)
      {
        final Node presentationModel = new NodePresentationModel(new DefaultSimpleComposite<ProfiledDisplay>(finder));
        presentation.populateDisplays(new LookupFilterDecoratorNode(presentationModel, displaysCapabilityInjectorLookupFilter));
      }
  }
