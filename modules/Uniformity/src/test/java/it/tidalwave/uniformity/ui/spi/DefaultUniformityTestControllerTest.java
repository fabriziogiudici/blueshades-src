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
import it.tidalwave.actor.Collaboration;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.argyll.impl.MessageVerifier;
import it.tidalwave.netbeans.util.test.MockLookup;
import it.tidalwave.uniformity.UniformityTestRequest;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import it.tidalwave.uniformity.ui.UniformityTestPresentation.Position;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static it.tidalwave.uniformity.ui.UniformityTestPresentation.Position.pos;
import java.util.Timer;
import java.util.TimerTask;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultUniformityTestControllerTest extends DefaultUniformityTestControllerTestSupport
  {
    private TestActivator testActivator;
    
    private InOrder inOrder;
    
    private MessageVerifier messageVerifier;
    
    private Action continueAction;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private final Answer<Void> clickContinue = new Answer<Void>()
      {
        @Override
        public Void answer (final @Nonnull InvocationOnMock invocation) 
          {
            new Timer().schedule(new TimerTask() 
              {
                @Override
                public void run() 
                  {
                    log.info("Clicking on 'Continue'...");
                    continueAction.actionPerformed(null);
                  }
              }, 1000);

            return null;
          }
      };

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private final Answer<Void> storeActionReferences = new Answer<Void>()
      {
        @Override
        public Void answer (final @Nonnull InvocationOnMock invocation) 
          {
            continueAction = (Action)invocation.getArguments()[0];
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
        
        createPresentation();
        MockLookup.setInstances(presentationBuilder);
        
        inOrder = inOrder(presentation);
        
        testActivator = new TestActivator();
        testActivator.activate();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    protected void createPresentation()
      {
        presentation = mock(UniformityTestPresentation.class);
        doAnswer(storeActionReferences).when(presentation).bind(any(Action.class));
        doAnswer(clickContinue).when(presentation).renderInvitation(any(Position.class));
        presentationBuilder = mock(UniformityTestPresentationBuilder.class);
        doReturn(presentation).when(presentationBuilder).buildUI();
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
        presentation = null;
        testActivator = null;
        continueAction = null;
        MockLookup.reset();
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
        
        inOrder.verify(presentation).bind(any(Action.class));
        inOrder.verify(presentation).setGridSize(eq(3), eq(3));
        
        inOrder.verify(presentation).renderControlPanel(eq(pos(0, 0)));
        inOrder.verify(presentation).renderInvitation(  eq(pos(1, 1)));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(pos(1, 1)));

        // measure
        inOrder.verify(presentation).renderMeasurement( eq(pos(1, 1)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(pos(0, 0)));
        inOrder.verify(presentation).renderControlPanel(eq(pos(0, 1)));
        
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
        
    private void waitForNextPressed()
      {
      }
  }
