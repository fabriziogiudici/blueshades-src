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
import java.awt.EventQueue;
import javax.swing.Action;
import javax.swing.JPanel;
import org.openide.nodes.Node;
import org.openide.explorer.ExplorerManager;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.swing.ActionAdapter;
import it.tidalwave.swing.RadioButtonsSelector;
import it.tidalwave.netbeans.SimpleExplorerPanel;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentation;
import it.tidalwave.blueargyle.util.MutableProperty;
import it.tidalwave.netbeans.explorer.view.EnhancedListView;
import java.awt.Color;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class UniformityCheckMainPanel extends JPanel implements UniformityCheckMainPresentation
  {
    public static final String PROP_SELECTED_MEASURE = "selectedMeasure";
    
    private final UniformityMeasurementsPanel measurementsPanel = new UniformityMeasurementsPanel();
 
    private final RadioButtonsSelector radioButtonsSelector;
    
    private final ActionAdapter startNewMeasurementAction = new ActionAdapter();
    
    private final EnhancedListView lvDisplays = new EnhancedListView();
    
    private final EnhancedListView lvMeasurementsArchive = new EnhancedListView();
    
    private final SimpleExplorerPanel epDisplays = new SimpleExplorerPanel(lvDisplays);
    
    private final SimpleExplorerPanel epMeasurementsArchive = new SimpleExplorerPanel(lvMeasurementsArchive);
    
    public UniformityCheckMainPanel() 
      {
        assert EventQueue.isDispatchThread();
        initComponents();
        setOpaque(true);
        radioButtonsSelector = new RadioButtonsSelector(rbLuminance, rbTemperature);
        pnInnerMeasurements.add(measurementsPanel, BorderLayout.CENTER);
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

        bgMeasurement = new javax.swing.ButtonGroup();
        pnMeasurements = new javax.swing.JPanel();
        pnInnerMeasurements = new javax.swing.JPanel();
        rbLuminance = new javax.swing.JRadioButton();
        rbTemperature = new javax.swing.JRadioButton();
        pnDisplays = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btStart = new javax.swing.JButton();
        pnArchive = new javax.swing.JPanel();
        lbArchive = new javax.swing.JLabel();
        lbDisplaySelection = new javax.swing.JLabel();
        lbMeasurements = new javax.swing.JLabel();

        setBackground(new java.awt.Color(80, 80, 80));
        setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.name")); // NOI18N

        pnMeasurements.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pnMeasurements.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.pnMeasurements.name")); // NOI18N
        pnMeasurements.setOpaque(false);

        pnInnerMeasurements.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.pnInnerMeasurements.name")); // NOI18N
        pnInnerMeasurements.setOpaque(false);
        pnInnerMeasurements.setLayout(new java.awt.BorderLayout());

        bgMeasurement.add(rbLuminance);
        rbLuminance.setForeground(new java.awt.Color(255, 255, 255));
        rbLuminance.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbLuminance.text")); // NOI18N
        rbLuminance.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbLuminance.name")); // NOI18N

        bgMeasurement.add(rbTemperature);
        rbTemperature.setForeground(new java.awt.Color(255, 255, 255));
        rbTemperature.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbTemperature.text")); // NOI18N
        rbTemperature.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbTemperature.name")); // NOI18N

        javax.swing.GroupLayout pnMeasurementsLayout = new javax.swing.GroupLayout(pnMeasurements);
        pnMeasurements.setLayout(pnMeasurementsLayout);
        pnMeasurementsLayout.setHorizontalGroup(
            pnMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnMeasurementsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnMeasurementsLayout.createSequentialGroup()
                        .addGap(0, 275, Short.MAX_VALUE)
                        .addComponent(rbLuminance)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbTemperature))
                    .addGroup(pnMeasurementsLayout.createSequentialGroup()
                        .addComponent(pnInnerMeasurements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnMeasurementsLayout.setVerticalGroup(
            pnMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnMeasurementsLayout.createSequentialGroup()
                .addGroup(pnMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbTemperature)
                    .addComponent(rbLuminance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnInnerMeasurements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnDisplays.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.pnDisplays.name")); // NOI18N
        pnDisplays.setOpaque(false);
        pnDisplays.setLayout(new java.awt.BorderLayout());

        jPanel3.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.jPanel3.name")); // NOI18N
        jPanel3.setOpaque(false);

        btStart.setAction(startNewMeasurementAction);
        btStart.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.btStart.text")); // NOI18N
        btStart.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.btStart.name")); // NOI18N
        jPanel3.add(btStart);

        pnArchive.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.pnArchive.name")); // NOI18N
        pnArchive.setOpaque(false);
        pnArchive.setLayout(new java.awt.BorderLayout());

        lbArchive.setForeground(new java.awt.Color(255, 255, 255));
        lbArchive.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbArchive.text")); // NOI18N
        lbArchive.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbArchive.name")); // NOI18N

        lbDisplaySelection.setForeground(new java.awt.Color(255, 255, 255));
        lbDisplaySelection.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbDisplaySelection.text")); // NOI18N
        lbDisplaySelection.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbDisplaySelection.name")); // NOI18N

        lbMeasurements.setFont(lbMeasurements.getFont().deriveFont(lbMeasurements.getFont().getStyle() | java.awt.Font.BOLD, lbMeasurements.getFont().getSize()+3));
        lbMeasurements.setForeground(new java.awt.Color(255, 255, 255));
        lbMeasurements.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbMeasurements.text")); // NOI18N
        lbMeasurements.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.lbMeasurements.name")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbArchive)
                    .addComponent(lbDisplaySelection)
                    .addComponent(lbMeasurements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnDisplays, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnArchive, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnMeasurements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnMeasurements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(lbMeasurements)
                        .addGap(60, 60, 60)
                        .addComponent(lbDisplaySelection)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnDisplays, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lbArchive)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnArchive, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgMeasurement;
    private javax.swing.JButton btStart;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lbArchive;
    private javax.swing.JLabel lbDisplaySelection;
    private javax.swing.JLabel lbMeasurements;
    private javax.swing.JPanel pnArchive;
    private javax.swing.JPanel pnDisplays;
    private javax.swing.JPanel pnInnerMeasurements;
    private javax.swing.JPanel pnMeasurements;
    private javax.swing.JRadioButton rbLuminance;
    private javax.swing.JRadioButton rbTemperature;
    // End of variables declaration//GEN-END:variables
  }
