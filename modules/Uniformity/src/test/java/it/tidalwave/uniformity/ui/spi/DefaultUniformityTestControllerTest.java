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
package it.tidalwave.uniformity.ui.spi;

import javax.annotation.Nonnull;
import javax.swing.Action;
import javax.swing.JFrame;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.spi.ActorActivator;
import it.tidalwave.actor.spi.ActorGroupActivator;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.argyll.impl.MessageVerifier;
import it.tidalwave.argyll.impl.MockSpotReadActor;
import it.tidalwave.uniformity.UniformityTestRequest;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import it.tidalwave.uniformity.ui.UniformityTestPresentation.Position;
import it.tidalwave.uniformity.ui.impl.SwingUniformityTestPresentation;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static it.tidalwave.uniformity.ui.UniformityTestPresentation.Position.pos;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultUniformityTestControllerTest 
  {
    static class TestActivator extends ActorGroupActivator
      {
        public final DefaultUniformityTestController fixture;
        
        public TestActivator() 
          {
            final ActorActivator fixtureActivator = new ActorActivator(DefaultUniformityTestController.class, 1);
            add(new ActorActivator(MockSpotReadActor.class, 1));
            add(fixtureActivator);
            
            fixture = (DefaultUniformityTestController)fixtureActivator.getActorObject();
          }
      }

    private DefaultUniformityTestController fixture;
    
    private UniformityTestPresentation presentation;
    
    private TestActivator testActivator;
    
    private InOrder inOrder;
    
    private MessageVerifier messageVerifier;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private final Answer<Void> clickContinue = new Answer<Void>()
      {
        @Override
        public Void answer (final @Nonnull InvocationOnMock invocation) 
          {
            log.info("Clicking on 'Continue'...");
            fixture.continueAction.actionPerformed(null);
            return null;
          }
      };

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @BeforeMethod
    public void setupFixture()
      {
        messageVerifier = new MessageVerifier();
        messageVerifier.initialize();
        presentation = mock(UniformityTestPresentation.class);
        doAnswer(clickContinue).when(presentation).renderInvitation(any(Position.class));
        
        inOrder = inOrder(presentation);
        testActivator = new TestActivator();
        testActivator.activate();
        fixture = testActivator.fixture;
        fixture.presentation = presentation;
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
        messageVerifier = null;
        fixture = null;
        presentation = null;
        testActivator = null;
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Test
    public void must_follow_the_proper_sequence_3x3() 
      throws InterruptedException
      {
        final Collaboration collaboration = new UniformityTestRequest().send();
        collaboration.waitForCompletion();
//        Thread.sleep(20000); // FIXME: wait for completion
        
        inOrder.verify(presentation).bind(any(Action.class));
        inOrder.verify(presentation).setGridSize(eq(3), eq(3));
        
        inOrder.verify(presentation).renderControlPanel(eq(pos(0, 0)));
        inOrder.verify(presentation).renderInvitation(  eq(pos(1, 1)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(1, 1)));

        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(1, 1)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderControlPanel(eq(pos(0, 1)));
        inOrder.verify(presentation).renderInvitation(  eq(pos(0, 0)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(0, 0)));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(0, 0)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderControlPanel(eq(pos(0, 0)));
        inOrder.verify(presentation).renderEmpty       (eq(pos(0, 1)));
        inOrder.verify(presentation).renderInvitation(  eq(pos(1, 0)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(1, 0)));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(1, 0)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(pos(2, 0)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(2, 0)));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(2, 0)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(pos(0, 1)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(0, 1)));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(0, 1)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(pos(2, 1)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(2, 1)));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(2, 1)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(pos(0, 2)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(0, 2)));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(0, 2)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(pos(1, 2)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(1, 2)));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(1, 2)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(pos(2, 2)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(2, 2)));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(2, 2)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        
        inOrder.verify(presentation).dispose();
        verifyNoMoreInteractions(presentation);
        
        messageVerifier.verifyCollaborationStarted();
        messageVerifier.verify(UniformityTestRequest.class);
        
        for (int i = 0; i < 9; i++)
          { 
            messageVerifier.verify(MeasurementRequest.class);  
            messageVerifier.verify(MeasurementMessage.class);  
          }
        
        messageVerifier.verifyCollaborationCompleted();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Test(enabled=false)
    public void must_follow_the_proper_sequence_3x3b() throws InterruptedException
      {
        JFrame jframe = new JFrame();
        final SwingUniformityTestPresentation presentation = new SwingUniformityTestPresentation();
        jframe.add(presentation);
        jframe.setSize(800, 600);
        jframe.setVisible(true);
        fixture.presentation = presentation;
        new UniformityTestRequest().send();

        do
          {
            Thread.sleep(2000);
          }
        while (jframe.isVisible());
      }
    
    private void waitForNextPressed()
      {
//        try
//          {
//            log.info("WAITING");
//          } 
//        catch (InterruptedException e) 
//          {
//          }
      }
  }
