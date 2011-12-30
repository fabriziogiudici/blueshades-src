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
package it.tidalwave.uniformity.ui.impl.main;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.NotThreadSafe;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.joda.time.format.DateTimeFormat;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.nodes.Node;
import it.tidalwave.util.Finder;
import it.tidalwave.role.Displayable;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.netbeans.nodes.NodePresentationModel;
import it.tidalwave.netbeans.nodes.LookupFilterDecoratorNode;
import it.tidalwave.netbeans.nodes.LookupFilterDecoratorNode.LookupFilter;
import it.tidalwave.blueargyle.util.MutableProperty;
import it.tidalwave.uniformity.UniformityCheckRequest;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.archive.UniformityArchiveContentMessage;
import it.tidalwave.uniformity.archive.UniformityArchiveQuery;
import it.tidalwave.uniformity.archive.UniformityArchiveUpdatedMessage;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentation;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentationProvider;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import static it.tidalwave.uniformity.Position.*;
import org.joda.time.format.DateTimeFormatter;

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
    @RequiredArgsConstructor
    private static class DateTimeDisplayable implements Displayable
      {
        private final DateTimeFormatter dateFormat = DateTimeFormat.shortDateTime();
        
        @Nonnull
        private final Lookup lookup;

        @Override @Nonnull
        public String getDisplayName() 
          {
            final UniformityMeasurements measurements = lookup.lookup(UniformityMeasurements.class);
            return (measurements != null) ? dateFormat.print(measurements.getDateTime()) : "???";
          }
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    static abstract class MeasurementRenderer
      {
        @Nonnull
        private final UniformityCheckMainPresentation presentation;
        
        @Nonnull
        private final String upperFormat;
        
        @Nonnull
        private final String lowerFormat;
        
        public void render (@Nonnull UniformityMeasurements measurements)
          {
            final int columns = measurements.getColumns();
            final int rows = measurements.getRows();
            final UniformityMeasurement centerMeasurement = measurements.getAt(xy(columns / 2, rows / 2));

            final String[][] s = new String[rows][columns];

            for (int row = 0; row < rows; row++)  
              {
                for (int column = 0; column < columns; column++)
                  {
                    s[row][column] = formatMeasurement(centerMeasurement, measurements.getAt(xy(column, row)));
                  }
              }

            presentation.renderMeasurements(s);
          }
        
        @Nonnull
        protected String formatMeasurement (final @Nonnull UniformityMeasurement centerMeasurement,
                                            final @Nonnull UniformityMeasurement measurement)
          {
            final double centerValue = getValue(centerMeasurement);  
            final double value = getValue(measurement);
            final double delta = value - centerValue;

            final StringBuilder buffer = new StringBuilder();
            buffer.append(String.format(upperFormat, value));
            
            if (centerMeasurement != measurement)
              {
                buffer.append(String.format(lowerFormat, delta));
              }
            
            return buffer.toString();
          }
        
        protected abstract double getValue (@Nonnull UniformityMeasurement measurement);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    static class TemperatureRenderer extends MeasurementRenderer
      {
        public TemperatureRenderer (final @Nonnull UniformityCheckMainPresentation presentation)
          {
            super(presentation, "%.0f K", "\n\u0394 = %+.0f K");  
          }
        
        @Override
        protected double getValue (final @Nonnull UniformityMeasurement measurement)
          {
            return measurement.getTemperature().getT();  
          }
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    static class LuminanceRenderer extends MeasurementRenderer
      {
        public LuminanceRenderer (final UniformityCheckMainPresentation presentation)
          {
            super(presentation, "%.0f cd/m\u00b2", "\n\u0394 = %+.0f cd/m\u00b2");  
          }
        
        @Override
        protected double getValue (final @Nonnull UniformityMeasurement measurement)
          {
            return measurement.getLuminance();  
          }
      }
    
    private final UniformityCheckMainPresentationProvider presentationBuilder = Locator.find(UniformityCheckMainPresentationProvider.class);
    
    private UniformityCheckMainPresentation presentation;

    private UniformityMeasurements measurements;
    
    private final MutableProperty<Integer> selectedMeasurement = new MutableProperty<Integer>(0);
    
    private final List<MeasurementRenderer> measurementRenderers = new ArrayList<MeasurementRenderer>();
    
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
    private final Action startAction = new AbstractAction("Start measurement") 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice[] screenDevices = ge.getScreenDevices();
            setEnabled(false);
            new UniformityCheckRequest(screenDevices[0].getIDstring()).send();
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
        log.info("initialize()");
        presentation = presentationBuilder.getPresentation();
        measurementRenderers.clear();
        measurementRenderers.add(new LuminanceRenderer(presentation));
        measurementRenderers.add(new TemperatureRenderer(presentation));
        presentation.bind(startAction, selectedMeasurement);
        
        new UniformityArchiveQuery().sendLater(1, TimeUnit.SECONDS); // FIXME
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderMeasurements (final @ListensTo @Nonnull UniformityMeasurementMessage message)
      {
        log.info("renderMeasurements({})", message);
        measurements = null; // prevents a double refresh because of changing selectedMeasurement
        selectedMeasurement.setValue(0);
        measurements = message.getMeasurements();
        refreshPresentation();
        startAction.setEnabled(true);
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderUpdatedArchive (final @ListensTo @Nonnull UniformityArchiveUpdatedMessage message)
      {
        log.info("renderUpdatedArchive({})", message);
        populateMeasurementsArchive(message.findMeasurements());
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderArchiveContents (final @ListensTo @Nonnull UniformityArchiveContentMessage message)
      {
        log.info("renderArchiveContents({})", message);
        populateMeasurementsArchive(message.findMeasurements());
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void populateMeasurementsArchive (final @Nonnull Finder<UniformityMeasurements> finder)
      {
        final Node pm = new NodePresentationModel(new DefaultSimpleComposite<UniformityMeasurements>(finder));
        // Can't use ThreadLookupBinder as model objects have already been created.
        presentation.populateMeasurementsArchive(new LookupFilterDecoratorNode(pm, new LookupFilter() 
          {
            @Override @Nonnull
            public Lookup filter (final @Nonnull Lookup lookup)
              {
                return new ProxyLookup(Lookups.fixed(new DateTimeDisplayable(lookup)), lookup);
              }
          }));
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void refreshPresentation()
      {
        if (measurements != null)
          {
            measurementRenderers.get(selectedMeasurement.getValue()).render(measurements);
          }
      }
  }
