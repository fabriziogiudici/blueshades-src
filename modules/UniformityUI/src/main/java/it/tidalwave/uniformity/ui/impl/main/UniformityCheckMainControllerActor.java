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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.nodes.Node;
import it.tidalwave.util.Finder;
import it.tidalwave.role.spi.DefaultSimpleComposite;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.netbeans.nodes.NodePresentationModel;
import it.tidalwave.netbeans.nodes.LookupFilterDecoratorNode;
import it.tidalwave.netbeans.nodes.LookupFilterDecoratorNode.LookupFilter;
import it.tidalwave.blueargyle.util.MutableProperty;
import it.tidalwave.netbeans.nodes.role.ActionProvider;
import it.tidalwave.swing.ActionMessageAdapter;
import it.tidalwave.uniformity.UniformityCheckRequest;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.archive.UniformityArchiveContentMessage;
import it.tidalwave.uniformity.archive.UniformityArchiveQuery;
import it.tidalwave.uniformity.ui.UniformityMeasurementsSelectedMessage;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentation;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final UniformityCheckMainPresentationProvider presentationBuilder = Locator.find(UniformityCheckMainPresentationProvider.class);
    
    private UniformityCheckMainPresentation presentation;

    private UniformityMeasurements measurements;
    
    private final MutableProperty<Integer> selectedMeasurement = new MutableProperty<Integer>(0);
    
    private final List<MeasurementRenderer> measurementRenderers = new ArrayList<MeasurementRenderer>();
    
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
    @RequiredArgsConstructor
    private static class MeasurementsActionProvider implements ActionProvider
      {
        @Nonnull
        private final UniformityMeasurements measurements;
        
        @Override @Nonnull
        public Action getPreferredAction() 
          {
            return new ActionMessageAdapter("Select", new UniformityMeasurementsSelectedMessage(measurements));
          }

        @Override @Nonnull
        public Collection<? extends Action> getActions() 
          {
            return Collections.<Action>emptyList();
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
    public void onNewMeasurements (final @ListensTo @Nonnull UniformityMeasurementMessage message)
      {
        log.info("onNewMeasurements({})", message);
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
    public void onSelectedArchivedMeasurements (final @ListensTo @Nonnull UniformityMeasurementsSelectedMessage message)
      {
        log.info("onSelectedArchivedMeasurements({})", message);
        measurements = message.getMeasurements();
        refreshPresentation();
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
//    public void populateMeasurementsArchive (final @ListensTo @Nonnull UniformityArchiveUpdatedMessage message)
//      {
//        log.info("populateMeasurementsArchive({})", message);
//        populateMeasurementsArchive(message.findMeasurements());
//      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void onArchivedMeasurementsNotified (final @ListensTo @Nonnull UniformityArchiveContentMessage message)
      {
        log.info("onArchivedMeasurementsNotified({})", message);
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
                final UniformityMeasurements measurements = lookup.lookup(UniformityMeasurements.class);                  
                return (measurements == null) ? lookup // e.g. the root node
                                              : new ProxyLookup(Lookups.fixed(new DateTimeDisplayable(lookup), 
                                                                              new MeasurementsActionProvider(measurements)), 
                                                                lookup);
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
