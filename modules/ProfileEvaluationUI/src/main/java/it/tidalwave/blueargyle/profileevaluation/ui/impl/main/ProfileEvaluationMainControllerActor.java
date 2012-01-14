/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
            log.info("START");
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
