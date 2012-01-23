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
 * WWW: http://blueargyle.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueargyle-src
 *
 **********************************************************************************************************************/
package it.tidalwave.uniformity.ui.impl.measurement;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.awt.GraphicsDevice;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.spi.ActorGroupActivator;
import it.tidalwave.argyll.Display;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.argyll.impl.FakeSpotReadActor;
import it.tidalwave.netbeans.util.test.MockLookup;
import it.tidalwave.uniformity.Position;
import it.tidalwave.uniformity.UniformityCheckRequest;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.ui.measurement.UniformityCheckMeasurementPresentation;
import it.tidalwave.uniformity.ui.measurement.UniformityCheckMeasurementPresentationProvider;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.InOrder;
import org.mockito.internal.matchers.Equals;
import it.tidalwave.netbeans.util.test.TestLoggerSetup;
import it.tidalwave.util.test.swing.ActionsTestHelper;
import it.tidalwave.util.test.swing.ActionTestHelper;
import it.tidalwave.actor.test.MessageVerifier;
import static it.tidalwave.actor.spi.ActorActivator.*;
import it.tidalwave.argyll.*;
import static it.tidalwave.uniformity.Position.xy;
import static it.tidalwave.colorimetry.ColorTemperature.kelvin;
import static org.mockito.Mockito.*;

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
//            add(activatorFor(SpotReadActor.class).withPoolSize(1)); // to test the real thing
            add(activatorFor(FakeSpotReadActor.class).withPoolSize(1));
            add(activatorFor(UniformityCheckMeasurementControllerActor.class).withPoolSize(1));
          }
      }
    
    private TestActivator testActivator;
    
    private InOrder inOrder;
    
    private MessageVerifier messageVerifier;
    
    private UniformityCheckMeasurementPresentationProvider presentationBuilder;
    
    private UniformityCheckMeasurementPresentation presentation;
    
    protected ActionsTestHelper actions;
    
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
        TestLoggerSetup.setupLogging(getClass());
          
        messageVerifier = new MessageVerifier();
        messageVerifier.initialize();
        actions = new ActionsTestHelper();
        
        presentation = createPresentation();
        actions.register(presentation).on().bind(any(Action.class), any(Action.class));
        
        presentationBuilder = mock(UniformityCheckMeasurementPresentationProvider.class);
        doReturn(presentation).when(presentationBuilder).getPresentation();
        MockLookup.setInstances(presentationBuilder);

        inOrder = inOrder(concatenate(presentation, actions.getVerifiers()));
        
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
        actions.dispose();
        messageVerifier = null;
        presentation = null;
        testActivator = null;
        actions = null;
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
        final Collaboration collaboration = new UniformityCheckRequest(new ProfiledDisplay(new Display("display1", 0), new Profile("profile"))).send();
        collaboration.waitForCompletion();
        
        inOrder.verify(presentation).bind(any(Action.class), any(Action.class));
        inOrder.verify(presentation).setGridSize(eq(3), eq(3));
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).showUp(any(GraphicsDevice.class)); // TODO: verify the graphicdevice
        inOrder.verify(presentation).renderControlPanelAt(                 eq(xy(0, 0)));
        
        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(1, 1)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(1, 1)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(1, 1)), eq("Luminance: 56 cd/m\u00b2"), eq("White point: 2111 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(0, 0)));
        inOrder.verify(presentation).renderControlPanelAt(                 eq(xy(0, 1)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(0, 0)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(0, 0)), eq("Luminance: 9 cd/m\u00b2"), eq("White point: 2568 K"));
        inOrder.verify(presentation).renderControlPanelAt(                 eq(xy(0, 0)));
        inOrder.verify(presentation).renderEmptyCellAt(                    eq(xy(0, 1)));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(1, 0)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(1, 0)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(1, 0)), eq("Luminance: 79 cd/m\u00b2"), eq("White point: 5436 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(2, 0)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(2, 0)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(2, 0)), eq("Luminance: 61 cd/m\u00b2"), eq("White point: 7916 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(0, 1)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(0, 1)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(0, 1)), eq("Luminance: 0 cd/m\u00b2"), eq("White point: 4329 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(2, 1)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(2, 1)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(2, 1)), eq("Luminance: 52 cd/m\u00b2"), eq("White point: 2641 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(0, 2)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(0, 2)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(0, 2)), eq("Luminance: 4 cd/m\u00b2"), eq("White point: 3015 K"));
        
        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(1, 2)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(1, 2)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(1, 2)), eq("Luminance: 20 cd/m\u00b2"), eq("White point: 6864 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(xy(2, 2)));
        inOrder.verify(action("Continue")).setEnabled(eq(true));
        inOrder.verify(action("Cancel")).setEnabled(eq(true));
        inOrder.verify(action("Continue")).actionPerformed(any(ActionEvent.class));
        inOrder.verify(presentation).hideInvitationToOperateOnTheSensor();
        inOrder.verify(action("Continue")).setEnabled(eq(false));
        inOrder.verify(action("Cancel")).setEnabled(eq(false));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(xy(2, 2)));
        inOrder.verify(presentation).showMeasureInProgress();
        inOrder.verify(presentation).hideMeasureInProgress();
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(xy(2, 2)), eq("Luminance: 0 cd/m\u00b2"), eq("White point: 7209 K"));
        
        inOrder.verify(presentation).dismiss();
        
        verifyNoMoreInteractions(presentation);
        
        messageVerifier.verifyCollaborationStarted();
        messageVerifier.verify(UniformityCheckRequest.class);
        
        for (int i = 0; i < 9; i++)
          { 
            messageVerifier.verify(MeasurementRequest.class);  
            messageVerifier.verify(MeasurementMessage.class);  
          }
        
        final SortedMap<Position, UniformityMeasurement> m = new TreeMap<Position, UniformityMeasurement>();
        m.put(xy(0, 0), new UniformityMeasurement(kelvin(2568), 9));
        m.put(xy(1, 0), new UniformityMeasurement(kelvin(5436), 79));
        m.put(xy(2, 0), new UniformityMeasurement(kelvin(7916), 61));
        m.put(xy(0, 1), new UniformityMeasurement(kelvin(4329), 0));
        m.put(xy(1, 1), new UniformityMeasurement(kelvin(2111), 56));
        m.put(xy(2, 1), new UniformityMeasurement(kelvin(2641), 52));
        m.put(xy(0, 2), new UniformityMeasurement(kelvin(3015), 4));
        m.put(xy(1, 2), new UniformityMeasurement(kelvin(6864), 20));
        m.put(xy(2, 2), new UniformityMeasurement(kelvin(7209), 0));
        final UniformityMeasurements measurements = new UniformityMeasurements(new ProfiledDisplay(new Display("display1", 0), new Profile("profile")), m);
        messageVerifier.verify(UniformityMeasurementMessage.class).with("measurements", new Equals(measurements)); 
        
        messageVerifier.verifyCollaborationCompleted();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    private ActionTestHelper.Verifier action (final @Nonnull String actionName) // syntactic sugar
      {
        return actions.getVerifierFor(actionName);  
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Object[] concatenate (final @Nonnull Object object, final @Nonnull Collection<Object> objects) 
      {
        final List<Object> result = new ArrayList<Object>();
        result.add(object);
        result.addAll(objects);
        return result.toArray();
      }
  }
