/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 **********************************************************************************************************************/
package it.tidalwave.blueargyle.profileevaluation.ui.sequence;

import javax.annotation.Nonnull;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface ProfileEvaluationSequencePresentationProvider 
  {
    @Nonnull
    public ProfileEvaluationSequencePresentation getPresentation();
  }
