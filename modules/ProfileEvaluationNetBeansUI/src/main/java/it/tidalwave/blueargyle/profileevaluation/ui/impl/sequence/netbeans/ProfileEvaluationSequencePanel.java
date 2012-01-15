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
package it.tidalwave.blueargyle.profileevaluation.ui.impl.sequence.netbeans;

import javax.annotation.Nonnull;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import it.tidalwave.swing.SafeActionAdapter;
import it.tidalwave.blueargyle.profileevaluation.ui.sequence.ProfileEvaluationSequencePresentation;
import it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans.GrangerRainbowPanel;
import it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans.HiKeyPanel;
import it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans.LoKeyPanel;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ProfileEvaluationSequencePanel extends JPanel implements ProfileEvaluationSequencePresentation
  {
    private final SafeActionAdapter nextAction = new SafeActionAdapter();
    
    private final SafeActionAdapter previousAction = new SafeActionAdapter();
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public ProfileEvaluationSequencePanel() 
      {
        assert EventQueue.isDispatchThread();
        initComponents();
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull Action nextAction, final @Nonnull Action previousAction) 
      {
        this.nextAction.bind(nextAction);
        this.previousAction.bind(previousAction);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void showUp (final @Nonnull GraphicsDevice graphicsDevice)
      {
        assert EventQueue.isDispatchThread();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void dismiss()
      {
        assert EventQueue.isDispatchThread();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderEvaluationStep (final @Nonnull String next) 
      {
        assert EventQueue.isDispatchThread();
        log.info("renderEvaluationStep({})", next);
       
        JComponent c = null;
        
        if ("hi".equals(next))
          {
            c = new HiKeyPanel();
          }
        else if ("lo".equals(next))
          {
            c = new LoKeyPanel();
          }
        else if ("granger".equals(next))
          {
            c = new GrangerRainbowPanel();
          }
        
        pnContents.removeAll();
        
        if (c != null)
          {
            pnContents.add(c, BorderLayout.CENTER);
          }
        
        revalidate();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void removeNotify()
      {
        assert EventQueue.isDispatchThread();
        nextAction.unbind();
        previousAction.unbind();
        super.removeNotify();
      } 
    
    /*******************************************************************************************************************
     * 
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     * 
     ******************************************************************************************************************/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btNext = new javax.swing.JButton();
        btPrevious = new javax.swing.JButton();
        pnContents = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBounds(new java.awt.Rectangle(16, 16, 16, 16));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 5, 0, 5, 0};
        layout.rowHeights = new int[] {0, 5, 0, 5, 0};
        setLayout(layout);

        btNext.setAction(nextAction);
        btNext.setText(org.openide.util.NbBundle.getMessage(ProfileEvaluationSequencePanel.class, "ProfileEvaluationSequencePanel.btNext.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        add(btNext, gridBagConstraints);

        btPrevious.setAction(previousAction);
        btPrevious.setText(org.openide.util.NbBundle.getMessage(ProfileEvaluationSequencePanel.class, "ProfileEvaluationSequencePanel.btPrevious.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        add(btPrevious, gridBagConstraints);

        pnContents.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(pnContents, gridBagConstraints);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ProfileEvaluationSequencePanel.class, "ProfileEvaluationSequencePanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btNext;
    private javax.swing.JButton btPrevious;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel pnContents;
    // End of variables declaration//GEN-END:variables
  }
