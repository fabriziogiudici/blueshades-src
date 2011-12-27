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
package it.tidalwave.uniformity.main.ui.impl;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.NotThreadSafe;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.MessageListener;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.blueargyle.util.MutableProperty;
import it.tidalwave.uniformity.UniformityCheckRequest;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.main.ui.UniformityCheckMainPresentation;
import it.tidalwave.uniformity.main.ui.UniformityCheckMainPresentationProvider;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.uniformity.Position.*;

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
public class UniformityCheckMainControllerActor
  {
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    abstract class MeasurementProcessor
      {
        public void render (@Nonnull UniformityMeasurements measurements)
          {
            final int columns = measurements.getColumns();
            final int rows = measurements.getRows();
            final UniformityMeasurement centerMeasurement = measurements.getAt(pos(columns / 2, rows / 2));

            final String[][] s = new String[rows][columns];

            for (int row = 0; row < rows; row++)  
              {
                for (int column = 0; column < columns; column++)
                  {
                    s[row][column] = formatMeasurement(centerMeasurement, measurements.getAt(pos(column, row)));
                  }
              }

            presentation.renderMeasurements(s);
          }
        
        @Nonnull
        protected abstract String formatMeasurement (@Nonnull UniformityMeasurement centerMeasurement,
                                                     @Nonnull UniformityMeasurement measurement);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    class TemperatureProcessor extends MeasurementProcessor
      {
        @Override @Nonnull
        protected String formatMeasurement (final @Nonnull UniformityMeasurement centerMeasurement,
                                            final @Nonnull UniformityMeasurement measurement)
          {
            final int centerValue = centerMeasurement.getTemperature().getT();  
            final int value = measurement.getTemperature().getT();
            final int delta = value - centerValue;
            
            final StringBuilder buffer = new StringBuilder();
            buffer.append(String.format("%d K", value));
            
            if (centerMeasurement != measurement)
              {
                buffer.append(String.format("\n\u0394 = %+d K", delta));
              }
            
            return buffer.toString();
          }
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    class LuminanceProcessor extends MeasurementProcessor
      {
        @Override @Nonnull
        protected String formatMeasurement (final @Nonnull UniformityMeasurement centerMeasurement,
                                            final @Nonnull UniformityMeasurement measurement)
          {
            final double centerValue = centerMeasurement.getLuminance();  
            final double value = measurement.getLuminance();
            final double delta = value - centerValue;

            final StringBuilder buffer = new StringBuilder();
            buffer.append(String.format("%.0f cd/m\u00b2", value));
            
            if (centerMeasurement != measurement)
              {
                buffer.append(String.format("\n\u0394 = %+.0f cd/m\u00b2", delta));
              }
            
            return buffer.toString();
          }
      }
    
    private final UniformityCheckMainPresentationProvider presentationBuilder = Locator.find(UniformityCheckMainPresentationProvider.class);
    
    private UniformityCheckMainPresentation presentation;

    private UniformityMeasurements measurements;
    
    private final MutableProperty<Integer> selectedMeasurement = new MutableProperty<Integer>(0);
    
    private final List<MeasurementProcessor> measurementProcessors = new ArrayList<MeasurementProcessor>();
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private final PropertyChangeListener pcl = new PropertyChangeListener() 
      {
        @Override
        public void propertyChange (final @Nonnull PropertyChangeEvent event) 
          {
            refreshPresentation();
          }
      };

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    /* package */ final Action startAction = new AbstractAction("Start measurement") 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            setEnabled(false);
            new UniformityCheckRequest().send();
          }
      };
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public UniformityCheckMainControllerActor()
      {
        selectedMeasurement.addPropertyChangeListener(pcl);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @PostConstruct
    public void initialize()
      {
        measurementProcessors.clear();
        measurementProcessors.add(new LuminanceProcessor());
        measurementProcessors.add(new TemperatureProcessor());
        presentation = presentationBuilder.getPresentation();
        presentation.bind(startAction, selectedMeasurement);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @MessageListener
    public void renderMeasurements (final @Nonnull UniformityMeasurementMessage message)
      {
        log.info("renderMeasurements({})", message);
        measurements = null; // prevents a double refresh because of changing selectedMeasurement
        selectedMeasurement.setValue(0);
        measurements = message.getMeasurements();
        refreshPresentation();
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void refreshPresentation()
      {
        if (measurements != null)
          {
            measurementProcessors.get(selectedMeasurement.getValue()).render(measurements);
          }
      }
  }
