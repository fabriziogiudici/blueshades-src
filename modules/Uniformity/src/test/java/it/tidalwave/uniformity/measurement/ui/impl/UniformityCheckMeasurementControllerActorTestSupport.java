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
package it.tidalwave.uniformity.measurement.ui.impl;

import javax.annotation.Nonnull;
import java.util.SortedMap;
import java.util.TreeMap;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.spi.ActorActivator;
import it.tidalwave.actor.spi.ActorGroupActivator;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.argyll.impl.MessageVerifier;
import it.tidalwave.argyll.impl.FakeSpotReadActor;
import it.tidalwave.netbeans.util.test.MockLookup;
import it.tidalwave.uniformity.Position;
import it.tidalwave.uniformity.UniformityCheckRequest;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.measurement.ui.UniformityCheckMeasurementPresentation;
import it.tidalwave.uniformity.measurement.ui.UniformityCheckMeasurementPresentationProvider;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.matchers.Equals;
import static it.tidalwave.uniformity.Position.pos;import static it.tidalwave.colorimetry.ColorTemperature.kelvin;
import static org.mockito.Mockito.*;
import org.mockito.internal.invocation.Invocation;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class UniformityCheckMeasurementControllerActorTestSupport
  {
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private static class TestActivator extends ActorGroupActivator
      {
        public TestActivator() 
          {
//            add(new ActorActivator(SpotReadActor.class, 1)); // to test the real thing
            add(new ActorActivator(FakeSpotReadActor.class, 1));
            add(new ActorActivator(UniformityCheckMeasurementControllerActor.class, 1));
          }
      }
    
    private TestActivator testActivator;
    
    private InOrder inOrder;
    
    private MessageVerifier messageVerifier;
    
    private UniformityCheckMeasurementPresentationProvider presentationBuilder;
    
    private UniformityCheckMeasurementPresentation presentation;
    
    protected ActionVerifier continueActionTracker;
    
    protected ActionVerifier cancelActionTracker;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private final Answer<Void> storeActionReferences = new Answer<Void>()
      {
        @Override
        public Void answer (final @Nonnull InvocationOnMock invocation)
          throws Throwable 
          {
            final Action continueActionDecorator = continueActionTracker.attach((Action)invocation.getArguments()[0]);
            final Action cancelActionDecorator = cancelActionTracker.attach((Action)invocation.getArguments()[1]);
            
            // FIXME: below is tricky because we also call some Mockito stuff not part of the public API
//            invocation.getMethod().invoke(invocation.getMock(), continueActionDecorator, cancelActionDecorator);
            ((Invocation)invocation).getRawArguments()[0] = continueActionDecorator;
            ((Invocation)invocation).getRawArguments()[1] = cancelActionDecorator;
            
            try
              {
                invocation.callRealMethod();
              }
            catch (Exception e)
              {
                // FIXME:
              }
            
            return null;
          }
      };

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    protected abstract UniformityCheckMeasurementPresentation createPresentation();
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        messageVerifier = new MessageVerifier();
        messageVerifier.initialize();
        continueActionTracker = new ActionVerifier();
        cancelActionTracker = new ActionVerifier();
        
        presentation = createPresentation();
        doAnswer(storeActionReferences).when(presentation).bind(any(Action.class), any(Action.class));
        presentationBuilder = mock(UniformityCheckMeasurementPresentationProvider.class);
        doReturn(presentation).when(presentationBuilder).getPresentation();
        MockLookup.setInstances(presentationBuilder);
        
        inOrder = inOrder(presentation, continueActionTracker.getSpy(), cancelActionTracker.getSpy());
        
        testActivator = new TestActivator();
        testActivator.activate();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @AfterMethod
    public void cleanup()
      {
        messageVerifier.dispose();
        testActivator.deactivate();
        continueActionTracker.dispose();
        cancelActionTracker.dispose();
        messageVerifier = null;
        presentation = null;
        testActivator = null;
        continueActionTracker = null;
        cancelActionTracker = null;
        MockLookup.reset();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Test
    public void must_properly_drive_a_complete_3x3_sequence() 
      throws InterruptedException
      {
        final Collaboration collaboration = new UniformityCheckRequest().send();
        collaboration.waitForCompletion();
        
        inOrder.verify(presentation).bind(any(Action.class), any(Action.class));
        inOrder.verify(presentation).setGridSize(eq(3), eq(3));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).showUp();
        inOrder.verify(presentation).renderControlPanelAt(eq(pos(0, 0)));
        
        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(1, 1)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(1, 1)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(1, 1)), eq("Luminance: 63 cd/m\u00b2"), eq("White point: 5925 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(0, 0)));
        inOrder.verify(presentation).renderControlPanelAt(                 eq(pos(0, 1)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(0, 0)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(0, 0)), eq("Luminance: 67 cd/m\u00b2"), eq("White point: 2753 K"));
        inOrder.verify(presentation).renderControlPanelAt(                 eq(pos(0, 0)));
        inOrder.verify(presentation).renderEmptyCellAt(                    eq(pos(0, 1)));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(1, 0)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(1, 0)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(1, 0)), eq("Luminance: 6 cd/m\u00b2"), eq("White point: 6507 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(2, 0)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(2, 0)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(2, 0)), eq("Luminance: 31 cd/m\u00b2"), eq("White point: 7284 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(0, 1)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(0, 1)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(0, 1)), eq("Luminance: 37 cd/m\u00b2"), eq("White point: 4102 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(2, 1)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(2, 1)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(2, 1)), eq("Luminance: 81 cd/m\u00b2"), eq("White point: 6456 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(0, 2)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(0, 2)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(0, 2)), eq("Luminance: 97 cd/m\u00b2"), eq("White point: 3813 K"));
        
        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(1, 2)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(1, 2)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(1, 2)), eq("Luminance: 33 cd/m\u00b2"), eq("White point: 2879 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(2, 2)));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(true));
        inOrder.verify(continueActionTracker.getSpy()).actionPerformed(any(ActionEvent.class));
        inOrder.verify(continueActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(cancelActionTracker.getSpy()).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(2, 2)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(2, 2)), eq("Luminance: 19 cd/m\u00b2"), eq("White point: 6071 K"));
        
        inOrder.verify(presentation).dismiss();
        
        if (!(presentation instanceof Component)) // Swing makes its own interactions
          {
            verifyNoMoreInteractions(presentation);
          }
        
        messageVerifier.verifyCollaborationStarted();
        messageVerifier.verify(UniformityCheckRequest.class);
        
        for (int i = 0; i < 9; i++)
          { 
            messageVerifier.verify(MeasurementRequest.class);  
            messageVerifier.verify(MeasurementMessage.class);  
          }
        
        final SortedMap<Position, UniformityMeasurement> m = new TreeMap<Position, UniformityMeasurement>();
        m.put(pos(0, 0), new UniformityMeasurement(kelvin(2753), 67));
        m.put(pos(1, 0), new UniformityMeasurement(kelvin(6507), 6));
        m.put(pos(2, 0), new UniformityMeasurement(kelvin(7284), 31));
        m.put(pos(0, 1), new UniformityMeasurement(kelvin(4102), 37));
        m.put(pos(1, 1), new UniformityMeasurement(kelvin(5925), 63));
        m.put(pos(2, 1), new UniformityMeasurement(kelvin(6456), 81));
        m.put(pos(0, 2), new UniformityMeasurement(kelvin(3813), 97));
        m.put(pos(1, 2), new UniformityMeasurement(kelvin(2879), 33));
        m.put(pos(2, 2), new UniformityMeasurement(kelvin(6071), 19));
        final UniformityMeasurements measurements = new UniformityMeasurements(m);
        messageVerifier.verify(UniformityMeasurementMessage.class).with("measurements", new Equals(measurements)); 
        
        messageVerifier.verifyCollaborationCompleted();
      }
  }
