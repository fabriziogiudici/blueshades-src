/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 **********************************************************************************************************************/
package it.tidalwave.blueargyle.profileevaluation.ui.impl.sequence;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.NotThreadSafe;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.blueargyle.profileevaluation.ui.sequence.ProfileEvaluationSequencePresentation;
import it.tidalwave.blueargyle.profileevaluation.ui.sequence.ProfileEvaluationSequencePresentationProvider;
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
public class ProfileEvaluationSequenceControllerActor
  {
    // FIXME: use @Inject
    private final ProfileEvaluationSequencePresentationProvider presentationProvider = Locator.find(ProfileEvaluationSequencePresentationProvider.class);
    
    /** The presentation controlled by this class */
    private ProfileEvaluationSequencePresentation presentation;
    
    /*******************************************************************************************************************
     * 
     * 
     *
     ******************************************************************************************************************/
    private final Action previousAction = new AbstractAction("Previous") 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            log.info("PREVIOUS");
          }
      };
    
    /*******************************************************************************************************************
     * 
     * 
     *
     ******************************************************************************************************************/
    private final Action nextAction = new AbstractAction("Next") 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            log.info("NEXT");
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
        presentation.bind(nextAction, previousAction);        
      }
  }
