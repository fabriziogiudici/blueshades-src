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
package it.tidalwave.uniformity.ui.impl.main.netbeans;

import javax.annotation.Nonnull;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.swing.RadioButtonsSelector;
import it.tidalwave.swing.SafeActionAdapter;
import it.tidalwave.netbeans.SimpleExplorerPanel;
import it.tidalwave.netbeans.explorer.view.EnhancedListView;
import it.tidalwave.blueargyle.util.MutableProperty;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentation;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class UniformityCheckMainPanel extends JPanel implements UniformityCheckMainPresentation
  {
    public static final String PROP_SELECTED_MEASURE = "selectedMeasure";
    
    private final UniformityMeasurementsPanel measurementsPanel = new UniformityMeasurementsPanel();
 
    private final RadioButtonsSelector radioButtonsSelector;
    
    private final SafeActionAdapter startNewMeasurementAction = new SafeActionAdapter();
    
    private final EnhancedListView lvDisplays = new EnhancedListView();
    
    private final EnhancedListView lvMeasurementsArchive = new EnhancedListView();
    
    private final SimpleExplorerPanel epDisplays = new SimpleExplorerPanel(lvDisplays);
    
    private final SimpleExplorerPanel epMeasurementsArchive = new SimpleExplorerPanel(lvMeasurementsArchive);
    
    public UniformityCheckMainPanel() 
      {
        assert EventQueue.isDispatchThread();
        initComponents();
        setOpaque(true);
        pnMeasurements.setBorder(BorderFactory.createEmptyBorder());
        radioButtonsSelector = new RadioButtonsSelector(rbLuminance, rbWhitePoint);
        pnMeasurements.add(measurementsPanel, BorderLayout.CENTER);
        pnDisplays.add(epDisplays, BorderLayout.CENTER);
        pnArchive.add(epMeasurementsArchive, BorderLayout.CENTER);
        
        lvMeasurementsArchive.setOpaque(true);
        lvMeasurementsArchive.putClientProperty("List.selectionBackground", new Color(60, 60, 60));
        lvMeasurementsArchive.putClientProperty("List.selectionForeground", Color.WHITE);
        lvMeasurementsArchive.setBackground(new Color(80, 80, 80));
        lvMeasurementsArchive.setForeground(Color.white);
        
        lvDisplays.setOpaque(true);
        lvDisplays.putClientProperty("List.selectionBackground", new Color(60, 60, 60));
        lvDisplays.putClientProperty("List.selectionForeground", Color.WHITE);
        lvDisplays.setBackground(new Color(80, 80, 80));
        lvDisplays.setForeground(Color.WHITE);
      }

    @Override
    public void bind (final @Nonnull Action startNewMeasurementAction, final @Nonnull MutableProperty<Integer> selectedMeasurement)
      {
        assert EventQueue.isDispatchThread();
        this.startNewMeasurementAction.bind(startNewMeasurementAction);
        radioButtonsSelector.bind(selectedMeasurement);
      }
    
    @Override
    public void showUp() 
      {
        assert EventQueue.isDispatchThread();
      }

    @Override
    public void dismiss()
      {
        assert EventQueue.isDispatchThread();
      }
    
    @Override
    public void populateDisplays (final @Nonnull PresentationModel presentationModel)
      {
        assert EventQueue.isDispatchThread();
        epDisplays.getExplorerManager().setRootContext((Node)presentationModel);
      }
    
    @Override
    public void populateMeasurementsArchive (final @Nonnull PresentationModel presentationModel)
      {  
        assert EventQueue.isDispatchThread();
        epMeasurementsArchive.getExplorerManager().setRootContext((Node)presentationModel);
      }
    
    @Override
    public void populateMeasurements (final @Nonnull String[][] measurements)
      {
        assert EventQueue.isDispatchThread();
        measurementsPanel.renderMeasurements(measurements);  
      }
    
    @Override
    public void selectFirstDisplay()  
      {
//        try 
//          {
            assert EventQueue.isDispatchThread();
            final ExplorerManager explorerManager = epDisplays.getExplorerManager();
            // FIXME: doesn't work
//            explorerManager.setSelectedNodes(new Node[] { explorerManager.getRootContext().getChildren().getNodes()[0] });
//          }
//        catch (PropertyVetoException e)
//          {
//            log.error("", e);
//          }
      }
    
    @Override
    public void showWaitingOnDisplayList() 
      {
        assert EventQueue.isDispatchThread();
        epDisplays.setBusy(true);
      }

    @Override
    public void showWaitingOnMeasurementsArchive() 
      {
        assert EventQueue.isDispatchThread();
        epMeasurementsArchive.setBusy(true);
      } 

    @Override
    public void hideWaitingOnDisplayList() 
      {
        assert EventQueue.isDispatchThread();
        epDisplays.setBusy(false);
      }

    @Override
    public void hideWaitingOnMeasurementsArchive() 
      {
        assert EventQueue.isDispatchThread();
        epMeasurementsArchive.setBusy(false);
      }
    
    @Override
    public void removeNotify()
      {
        assert EventQueue.isDispatchThread();
        startNewMeasurementAction.unbind();
        radioButtonsSelector.unbind();
        super.removeNotify();
      } 
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bgProperty = new javax.swing.ButtonGroup();
        rbLuminance = new javax.swing.JRadioButton();
        rbWhitePoint = new javax.swing.JRadioButton();
        lbUniformityMeasurements = new javax.swing.JLabel();
        pnMeasurements = new javax.swing.JPanel();
        btStartMeasurements = new javax.swing.JButton();
        lbDisplaySelection = new javax.swing.JLabel();
        lbArchive = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        pnDisplays = new javax.swing.JPanel();
        pnArchive = new javax.swing.JPanel();

        setBackground(new java.awt.Color(80, 80, 80));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 8, 16));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 5, 0, 5, 0, 5, 0};
        layout.rowHeights = new int[] {0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0};
        setLayout(layout);

        bgProperty.add(rbLuminance);
        rbLuminance.setForeground(new java.awt.Color(255, 255, 255));
        rbLuminance.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbLuminance.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(rbLuminance, gridBagConstraints);

        bgProperty.add(rbWhitePoint);
        rbWhitePoint.setForeground(new java.awt.Color(255, 255, 255));
        rbWhitePoint.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbWhitePoint.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_TRAILING;
        add(rbWhitePoint, gridBagConstraints);

        lbUniformityMeasurements.setFont(lbUniformityMeasurements.getFont().deriveFont(lbUniformityMeasurements.getFont().getStyle() | java.awt.Font.BOLD, lbUniformityMeasurements.getFont().getSize()+3));
        lbUniformityMeasurements.setForeground(new java.awt.Color(255, 255, 255));
        lbUniformityMeasurements.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbUniformityMeasurements.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE_LEADING;
        add(lbUniformityMeasurements, gridBagConstraints);

        pnMeasurements.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        pnMeasurements.setOpaque(false);
        pnMeasurements.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        add(pnMeasurements, gridBagConstraints);

        btStartMeasurements.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.btStartMeasurements.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 7;
        add(btStartMeasurements, gridBagConstraints);

        lbDisplaySelection.setFont(lbDisplaySelection.getFont().deriveFont(lbDisplaySelection.getFont().getSize()+1f));
        lbDisplaySelection.setForeground(new java.awt.Color(255, 255, 255));
        lbDisplaySelection.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbDisplaySelection.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 0, 0);
        add(lbDisplaySelection, gridBagConstraints);

        lbArchive.setFont(lbArchive.getFont().deriveFont(lbArchive.getFont().getSize()+1f));
        lbArchive.setForeground(new java.awt.Color(255, 255, 255));
        lbArchive.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbArchive.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 0, 0);
        add(lbArchive, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(filler1, gridBagConstraints);

        pnDisplays.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(70, 70, 70), 1, true));
        pnDisplays.setMinimumSize(new java.awt.Dimension(100, 80));
        pnDisplays.setOpaque(false);
        pnDisplays.setPreferredSize(new java.awt.Dimension(214, 80));
        pnDisplays.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(pnDisplays, gridBagConstraints);

        pnArchive.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(70, 70, 70), 1, true));
        pnArchive.setOpaque(false);
        pnArchive.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(pnArchive, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgProperty;
    private javax.swing.JButton btStartMeasurements;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lbArchive;
    private javax.swing.JLabel lbDisplaySelection;
    private javax.swing.JLabel lbUniformityMeasurements;
    private javax.swing.JPanel pnArchive;
    private javax.swing.JPanel pnDisplays;
    private javax.swing.JPanel pnMeasurements;
    private javax.swing.JRadioButton rbLuminance;
    private javax.swing.JRadioButton rbWhitePoint;
    // End of variables declaration//GEN-END:variables
  }
