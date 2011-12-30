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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.NotThreadSafe;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
import it.tidalwave.actor.RepeatingMessageSender;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.netbeans.nodes.NodePresentationModel;
import it.tidalwave.netbeans.nodes.LookupFilterDecoratorNode;
import it.tidalwave.netbeans.nodes.LookupFilterDecoratorNode.LookupFilter;
import it.tidalwave.netbeans.nodes.role.ActionProvider;
import it.tidalwave.swing.ActionMessageAdapter;
import it.tidalwave.blueargyle.util.MutableProperty;
import it.tidalwave.argyll.DisplayDiscoveryMessage;
import it.tidalwave.argyll.DisplayDiscoveryQueryMessage;
import it.tidalwave.argyll.Display;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.uniformity.UniformityCheckRequest;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.archive.UniformityArchiveContentMessage;
import it.tidalwave.uniformity.archive.UniformityArchiveQuery;
import it.tidalwave.uniformity.archive.UniformityArchiveUpdatedMessage;
import it.tidalwave.uniformity.ui.UniformityMeasurementsSelectedMessage;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentation;
import it.tidalwave.uniformity.ui.main.UniformityCheckMainPresentationProvider;
import it.tidalwave.util.spi.SimpleFinderSupport;
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
    // FIXME: use @Inject
    private final UniformityCheckMainPresentationProvider presentationBuilder = Locator.find(UniformityCheckMainPresentationProvider.class);
    
    /** The presentation controlled by this class */
    private UniformityCheckMainPresentation presentation;

    /** The measurements currently selected and rendered in details. */
    @CheckForNull
    private UniformityMeasurements selectedMeasurements;
    
    /** The index [0..1] pointing to the property to render in details (Luminance, Temperature) */
    private final MutableProperty<Integer> selectedPropertyRendereIndex = new MutableProperty<Integer>(0);
    
    /** The property renderers. */
    private final List<PropertyRenderer> propertyRenderers = new ArrayList<PropertyRenderer>();
    
    /** The requestor sending query messages to the archive at initialization. */
    private final RepeatingMessageSender archivedMeasurementsRequestor = new RepeatingMessageSender(new UniformityArchiveQuery());
 
    /** The requestor sending discovery messages for displays at initialization. */
    private final RepeatingMessageSender displayDiscoveryRequestor = new RepeatingMessageSender(new DisplayDiscoveryQueryMessage());
    
    /*******************************************************************************************************************
     *
     * The renderer for temperature.
     *
     ******************************************************************************************************************/
    static class TemperatureRenderer extends PropertyRenderer
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
     * The renderer for luminance.
     *
     ******************************************************************************************************************/
    static class LuminanceRenderer extends PropertyRenderer
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
     * Tracks the currently selected property render and eventually refreshes the presentation.
     *
     ******************************************************************************************************************/
    private final PropertyChangeListener selectedPropertyRendererTracker = new PropertyChangeListener() 
      {
        @Override
        public void propertyChange (final @Nonnull PropertyChangeEvent event) 
          {
            refreshPresentation();
          }
      };

    /*******************************************************************************************************************
     * 
     * Injects some capabilities into the PresentationModel for measurements. 
     *
     ******************************************************************************************************************/
    private final LookupFilter capabilityInjectorLookupFilter = new LookupFilter() 
      {
        @Override @Nonnull
        public Lookup filter (final @Nonnull Lookup lookup)
          {
            final UniformityMeasurements measurements = lookup.lookup(UniformityMeasurements.class);        
            return (measurements == null) ? lookup // e.g. the root node 
                                          : new ProxyLookup(Lookups.fixed(new DateTimeDisplayable(measurements), 
                                                                          new MeasurementsActionProvider(measurements)),
                                                            lookup);
          }
      };
    
    /*******************************************************************************************************************
     * 
     * Starts a new measurement sequence.
     *
     ******************************************************************************************************************/
    private final Action startNewMeasurementAction = new AbstractAction("Start measurement") 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            // TODO: query Argyll for existing devices
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            final GraphicsDevice[] screenDevices = ge.getScreenDevices();
            setEnabled(false);
            new UniformityCheckRequest(screenDevices[0].getIDstring()).send();
          }
      };
    
    /*******************************************************************************************************************
     * 
     * The ActionProvider for measurements.
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
        selectedPropertyRendereIndex.addPropertyChangeListener(selectedPropertyRendererTracker);
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
        propertyRenderers.clear();
        propertyRenderers.add(new LuminanceRenderer(presentation));
        propertyRenderers.add(new TemperatureRenderer(presentation));
        presentation.bind(startNewMeasurementAction, selectedPropertyRendereIndex);
        
        presentation.showWaitingOnDisplayList();
        presentation.showWaitingOnMeasurementsArchive();
        archivedMeasurementsRequestor.start();
        displayDiscoveryRequestor.start();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void onDiscoveredDisplays (final @ListensTo @Nonnull DisplayDiscoveryMessage message)
      {
        log.info("onDiscoveredDisplays({})", message);
        displayDiscoveryRequestor.stop();
        populateDisplays(message.findDisplays());
        presentation.hideWaitingOnDisplayList();
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void onArchivedMeasurementsNotified (final @ListensTo @Nonnull UniformityArchiveContentMessage message)
      {
        log.info("onArchivedMeasurementsNotified({})", message);
        archivedMeasurementsRequestor.stop();
        populateMeasurementsArchive(message.findMeasurements());
        presentation.hideWaitingOnMeasurementsArchive();
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void onUpdatedArchivedMeasurements (final @ListensTo @Nonnull UniformityArchiveUpdatedMessage message)
      {
        log.info("onUpdatedArchivedMeasurements({})", message);
        archivedMeasurementsRequestor.stop();
        populateMeasurementsArchive(message.findMeasurements());
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void onNewMeasurements (final @ListensTo @Nonnull UniformityMeasurementMessage message)
      {
        log.info("onNewMeasurements({})", message);
        selectedMeasurements = null; // prevents a double refresh because of changing selectedMeasurement
        selectedPropertyRendereIndex.setValue(0);
        selectedMeasurements = message.getMeasurements();
        refreshPresentation();
        startNewMeasurementAction.setEnabled(true);
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void onSelectedArchivedMeasurements (final @ListensTo @Nonnull UniformityMeasurementsSelectedMessage message)
      {
        log.info("onSelectedArchivedMeasurements({})", message);
        selectedMeasurements = message.getMeasurements();
        refreshPresentation();
      }  
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void populateDisplays (final @Nonnull Finder<Display> finder)
      {
        final PresentationModel presentationModel = new NodePresentationModel(new DefaultSimpleComposite<Display>(finder));
        presentation.populateDisplays(presentationModel);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void populateMeasurementsArchive (final @Nonnull Finder<UniformityMeasurements> finder)
      {
        final Node presentationModel = new NodePresentationModel(new DefaultSimpleComposite<UniformityMeasurements>(finder));
        presentation.populateMeasurementsArchive(new LookupFilterDecoratorNode(presentationModel, capabilityInjectorLookupFilter));
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void refreshPresentation()
      {
        if (selectedMeasurements != null)
          {
            propertyRenderers.get(selectedPropertyRendereIndex.getValue()).render(selectedMeasurements);
          }
      }
  }
