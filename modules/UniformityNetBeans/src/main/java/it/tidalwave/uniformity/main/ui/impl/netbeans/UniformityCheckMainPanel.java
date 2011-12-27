/***********************************************************************************************************************
 *
 * blueArgyle - a Java UI for Argyll
 * Copyright (C) 2011-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.uniformity.main.ui.impl.netbeans;

import javax.annotation.Nonnull;
import java.awt.BorderLayout;
import javax.swing.Action;
import javax.swing.JPanel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import it.tidalwave.blueargyle.util.MutableProperty;
import it.tidalwave.uniformity.main.ui.UniformityCheckMainPresentation;
import static it.tidalwave.blueargyle.util.SafeRunner.*;
//import static java.util.Collections.*;
//import javax.annotation.Nonnegative;
//import javax.swing.AbstractButton;
//import org.jdesktop.beansbinding.AutoBinding;
//import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.*;
//import static org.jdesktop.beansbinding.BeanProperty.create;
//import org.jdesktop.beansbinding.BindingGroup;
//import static org.jdesktop.beansbinding.Bindings.*;
//import org.jdesktop.observablecollections.ObservableList;

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
    
    private MutableProperty<Integer> selectedMeasurement;
    
    private final PropertyChangeListener pcl = new PropertyChangeListener() 
      {
        @Override
        public void propertyChange (final @Nonnull PropertyChangeEvent event) 
          {
            switch (selectedMeasurement.getValue())
              {
                case 0: rbLuminance.setSelected(true); break;  
                case 1: rbTemperature.setSelected(true); break;  
              }
          }
      };

    public UniformityCheckMainPanel() 
      {
        initComponents();
        pnInnerMeasurements.add(measurementsPanel, BorderLayout.CENTER);
        //        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${selectedMeasure}"), rbLuminance, org.jdesktop.beansbinding.BeanProperty.create("iconTextGap"));
        
      }

    @Override
    public void bind (final @Nonnull Action startAction, final @Nonnull MutableProperty<Integer> selectedMeasurement)
      {
        btStart.setAction(startAction);
        this.selectedMeasurement = selectedMeasurement;
        this.selectedMeasurement.addPropertyChangeListener(pcl);
        pcl.propertyChange(null);
//        final AbstractButton[] buttons = list(bgMeasurement.getElements()).toArray(new AbstractButton[0]);
//        bindingGroup.addBinding(createAutoBinding(READ_WRITE, buttons[0], null, create("selected"), create("selected1")));
//        bindingGroup.addBinding(createAutoBinding(READ_WRITE, buttons[1], null, create("selected"), create("selected2")));
//        bindingGroup.bind();
      }
    
    @Override
    public void showUp() 
      {
      }

    @Override
    public void dismiss()
      {
      }
    
    @Override
    public void renderMeasurements (final @Nonnull String[][] measurements)
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                measurementsPanel.renderMeasurements(measurements);  
              }
        });
      }
    
    @Override
    public void removeNotify()
      {
        btStart.setAction(null);
        this.selectedMeasurement.removePropertyChangeListener(pcl);
        selectedMeasurement = null;
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
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btStart = new javax.swing.JButton();

        setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.name")); // NOI18N

        pnMeasurements.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.pnMeasurements.border.outsideBorder.title")), javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8))); // NOI18N
        pnMeasurements.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.pnMeasurements.name")); // NOI18N

        pnInnerMeasurements.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.pnInnerMeasurements.name")); // NOI18N
        pnInnerMeasurements.setLayout(new java.awt.BorderLayout());

        bgMeasurement.add(rbLuminance);
        rbLuminance.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbLuminance.text")); // NOI18N
        rbLuminance.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbLuminance.name")); // NOI18N
        rbLuminance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLuminanceActionPerformed(evt);
            }
        });

        bgMeasurement.add(rbTemperature);
        rbTemperature.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbTemperature.text")); // NOI18N
        rbTemperature.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.rbTemperature.name")); // NOI18N
        rbTemperature.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTemperatureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnMeasurementsLayout = new javax.swing.GroupLayout(pnMeasurements);
        pnMeasurements.setLayout(pnMeasurementsLayout);
        pnMeasurementsLayout.setHorizontalGroup(
            pnMeasurementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnMeasurementsLayout.createSequentialGroup()
                .addGap(0, 222, Short.MAX_VALUE)
                .addComponent(rbLuminance)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbTemperature))
            .addGroup(pnMeasurementsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnInnerMeasurements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.jPanel2.border.title"))); // NOI18N
        jPanel2.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.jPanel2.name")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 242, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );

        jPanel3.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.jPanel3.name")); // NOI18N

        btStart.setText(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.btStart.text")); // NOI18N
        btStart.setName(org.openide.util.NbBundle.getMessage(UniformityCheckMainPanel.class, "UniformityCheckMainPanel.btStart.name")); // NOI18N
        jPanel3.add(btStart);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 150, Short.MAX_VALUE))
                    .addComponent(pnMeasurements, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rbLuminanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLuminanceActionPerformed
        if (selectedMeasurement != null)
          {
            selectedMeasurement.setValue(0);  
          }
    }//GEN-LAST:event_rbLuminanceActionPerformed

    private void rbTemperatureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTemperatureActionPerformed
        if (selectedMeasurement != null)
          {
            selectedMeasurement.setValue(1);  
          }
    }//GEN-LAST:event_rbTemperatureActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgMeasurement;
    private javax.swing.JButton btStart;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel pnInnerMeasurements;
    private javax.swing.JPanel pnMeasurements;
    private javax.swing.JRadioButton rbLuminance;
    private javax.swing.JRadioButton rbTemperature;
    // End of variables declaration//GEN-END:variables
  }
