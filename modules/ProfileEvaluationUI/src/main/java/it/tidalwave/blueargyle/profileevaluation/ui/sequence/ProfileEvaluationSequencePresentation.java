/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 **********************************************************************************************************************/
package it.tidalwave.blueargyle.profileevaluation.ui.sequence;

import javax.annotation.Nonnull;
import javax.swing.Action;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface ProfileEvaluationSequencePresentation 
  {
    public void bind (@Nonnull Action nextAction, @Nonnull Action previousAction);
  }
