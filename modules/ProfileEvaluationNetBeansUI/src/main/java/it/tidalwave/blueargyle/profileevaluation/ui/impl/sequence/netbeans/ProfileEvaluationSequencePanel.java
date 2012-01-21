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
import java.awt.color.ICC_Profile;
import javax.swing.Action;
import javax.swing.JPanel;
import it.tidalwave.argyll.ProfiledDisplay;
import it.tidalwave.swing.SafeActionAdapter;
import it.tidalwave.blueargyle.profileevaluation.ui.sequence.ProfileEvaluationSequencePresentation;
import it.tidalwave.blueargyle.profileevaluation.ui.sequence.GrangerRainbowDescriptor;
import it.tidalwave.blueargyle.profileevaluation.ui.sequence.HiKeyDescriptor;
import it.tidalwave.blueargyle.profileevaluation.ui.sequence.LoKeyDescriptor;
import it.tidalwave.blueargyle.profileevaluation.ui.sequence.SequenceStepDescriptor;
import it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans.DeferredCreationEditableImageRenderer;
import it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans.GrangerRainbowRenderer;
import it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans.HiKeyRenderer;
import it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans.LoKeyRenderer;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ProfileEvaluationSequencePanel extends JPanel implements ProfileEvaluationSequencePresentation
  {
    private final SafeActionAdapter nextAction = new SafeActionAdapter();
    
    private final SafeActionAdapter previousAction = new SafeActionAdapter();
    
    @Nonnull
    private ProfiledDisplay profiledDisplay;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public ProfileEvaluationSequencePanel() 
      {
        assert EventQueue.isDispatchThread();
        initComponents();
        setOpaque(true);
        lbProfileName.setText(" ");
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
    public void showUp (final @Nonnull ProfiledDisplay profiledDisplay)
      {
        assert EventQueue.isDispatchThread();
        this.profiledDisplay = profiledDisplay;
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
    public void renderEvaluationStep (final @Nonnull SequenceStepDescriptor step) 
      {
        assert EventQueue.isDispatchThread();
        log.info("renderEvaluationStep({})", step);
       
        final ICC_Profile iccProfile = profiledDisplay.getProfile().getIccProfile();
        DeferredCreationEditableImageRenderer c = null;
        
        // FIXME: use a Factory
        if (HiKeyDescriptor.class.equals(step.getClass()))
          {
            c = new HiKeyRenderer("MelissaRGB", iccProfile);
          }
        else if (LoKeyDescriptor.class.equals(step.getClass()))
          {
            c = new LoKeyRenderer("MelissaRGB", iccProfile);
          }
        else if (GrangerRainbowDescriptor.class.equals(step.getClass()))
          {
            c = new GrangerRainbowRenderer("MelissaRGB", iccProfile); 
          }
        
        pnContents.removeAll();
        
        if (c != null)
          {
            pnContents.add(c, BorderLayout.CENTER);
          }
        
        lbTitle.setText(step.getTitle());
        lbInstructions.setText("<html>" + step.getInstructions() + "</html>");
        
        revalidate();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderProfileName (final @Nonnull String profileName) 
      {
        assert EventQueue.isDispatchThread();
        lbProfileName.setText(profileName);
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
        lbInstructions = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        lbTitle = new javax.swing.JLabel();
        lbProfileName = new javax.swing.JLabel();

        setBackground(new java.awt.Color(30, 30, 30));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setBounds(new java.awt.Rectangle(16, 16, 16, 16));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 5, 0, 5, 0};
        layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0};
        setLayout(layout);

        btNext.setAction(nextAction);
        btNext.setText(org.openide.util.NbBundle.getMessage(ProfileEvaluationSequencePanel.class, "ProfileEvaluationSequencePanel.btNext.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        add(btNext, gridBagConstraints);

        btPrevious.setAction(previousAction);
        btPrevious.setText(org.openide.util.NbBundle.getMessage(ProfileEvaluationSequencePanel.class, "ProfileEvaluationSequencePanel.btPrevious.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        add(btPrevious, gridBagConstraints);

        pnContents.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(pnContents, gridBagConstraints);

        lbInstructions.setForeground(new java.awt.Color(236, 236, 236));
        lbInstructions.setText(org.openide.util.NbBundle.getMessage(ProfileEvaluationSequencePanel.class, "ProfileEvaluationSequencePanel.lbInstructions.text")); // NOI18N
        lbInstructions.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.15;
        add(lbInstructions, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);

        lbTitle.setFont(lbTitle.getFont().deriveFont(lbTitle.getFont().getStyle() | java.awt.Font.BOLD, lbTitle.getFont().getSize()+3));
        lbTitle.setForeground(new java.awt.Color(236, 236, 236));
        lbTitle.setText(org.openide.util.NbBundle.getMessage(ProfileEvaluationSequencePanel.class, "ProfileEvaluationSequencePanel.lbTitle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        add(lbTitle, gridBagConstraints);

        lbProfileName.setForeground(new java.awt.Color(236, 236, 236));
        lbProfileName.setText(org.openide.util.NbBundle.getMessage(ProfileEvaluationSequencePanel.class, "ProfileEvaluationSequencePanel.lbProfileName.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        add(lbProfileName, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btNext;
    private javax.swing.JButton btPrevious;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lbInstructions;
    private javax.swing.JLabel lbProfileName;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel pnContents;
    // End of variables declaration//GEN-END:variables
  }
