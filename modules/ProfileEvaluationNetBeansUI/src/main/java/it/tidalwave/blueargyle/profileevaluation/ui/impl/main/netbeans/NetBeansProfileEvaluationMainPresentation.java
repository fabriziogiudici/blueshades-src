/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 **********************************************************************************************************************/
package it.tidalwave.blueargyle.profileevaluation.ui.impl.main.netbeans;

import java.awt.EventQueue;
import it.tidalwave.blueargyle.profileevaluation.ui.main.ProfileEvaluationMainPresentation;
import org.openide.windows.WindowManager;
import lombok.Delegate;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class NetBeansProfileEvaluationMainPresentation implements ProfileEvaluationMainPresentation
  {
    @Delegate(types=ProfileEvaluationMainPresentation.class)
    protected final ProfileEvaluationMainPresentation panel;
    
    protected final ProfileEvaluationMainTopComponent topComponent;
    
    public NetBeansProfileEvaluationMainPresentation()
      {
        assert EventQueue.isDispatchThread();
        topComponent = (ProfileEvaluationMainTopComponent)WindowManager.getDefault().findTopComponent("ProfileEvaluationMainTopComponent");
        panel = topComponent.getContent();
      }
  }
