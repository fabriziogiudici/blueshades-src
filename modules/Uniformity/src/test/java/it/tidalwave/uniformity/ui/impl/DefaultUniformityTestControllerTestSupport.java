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
package it.tidalwave.uniformity.ui.impl;

import javax.swing.Action;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.spi.ActorActivator;
import it.tidalwave.actor.spi.ActorGroupActivator;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.argyll.impl.MessageVerifier;
import it.tidalwave.argyll.impl.MockSpotReadActor;
import it.tidalwave.uniformity.UniformityTestRequest;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import it.tidalwave.uniformity.ui.spi.UniformityTestPresentationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.mockito.InOrder;
import static it.tidalwave.uniformity.ui.UniformityTestPresentation.Position.pos;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class DefaultUniformityTestControllerTestSupport 
  {
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    protected static class TestActivator extends ActorGroupActivator
      {
        public TestActivator() 
          {
            add(new ActorActivator(MockSpotReadActor.class, 1));
            add(new ActorActivator(DefaultUniformityTestController.class, 1));
          }
      }
    
    protected TestActivator testActivator;
    
    protected InOrder inOrder;
    
    protected MessageVerifier messageVerifier;
    
    protected UniformityTestPresentationBuilder presentationBuilder;
    
    protected UniformityTestPresentation presentation;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    protected abstract void createPresentation();
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void xxx() 
      throws InterruptedException
      {
        final Collaboration collaboration = new UniformityTestRequest().send();
        collaboration.waitForCompletion();
        
        inOrder.verify(presentation).bind(any(Action.class));
        inOrder.verify(presentation).setGridSize(eq(3), eq(3));
        inOrder.verify(presentation).renderControlPanel(eq(pos(0, 0)));
        
        inOrder.verify(presentation).renderInvitation(  eq(pos(1, 1)));
        inOrder.verify(presentation).renderWhite(       eq(pos(1, 1)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(1, 1)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));

        inOrder.verify(presentation).renderInvitation(  eq(pos(0, 0)));
        inOrder.verify(presentation).renderControlPanel(eq(pos(0, 1)));
        inOrder.verify(presentation).renderWhite(       eq(pos(0, 0)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(0, 0)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderControlPanel(eq(pos(0, 0)));
        inOrder.verify(presentation).renderEmpty       (eq(pos(0, 1)));

        inOrder.verify(presentation).renderInvitation(  eq(pos(1, 0)));
        inOrder.verify(presentation).renderWhite(       eq(pos(1, 0)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(1, 0)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));

        inOrder.verify(presentation).renderInvitation(  eq(pos(2, 0)));
        inOrder.verify(presentation).renderWhite(       eq(pos(2, 0)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(2, 0)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));

        inOrder.verify(presentation).renderInvitation(  eq(pos(0, 1)));
        inOrder.verify(presentation).renderWhite(       eq(pos(0, 1)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(0, 1)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));

        inOrder.verify(presentation).renderInvitation(  eq(pos(2, 1)));
        inOrder.verify(presentation).renderWhite(       eq(pos(2, 1)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(2, 1)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));

        inOrder.verify(presentation).renderInvitation(  eq(pos(0, 2)));
        inOrder.verify(presentation).renderWhite(       eq(pos(0, 2)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(0, 2)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        
        inOrder.verify(presentation).renderInvitation(  eq(pos(1, 2)));
        inOrder.verify(presentation).renderWhite(       eq(pos(1, 2)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(1, 2)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));

        inOrder.verify(presentation).renderInvitation(  eq(pos(2, 2)));
        inOrder.verify(presentation).renderWhite(       eq(pos(2, 2)));
        inOrder.verify(presentation).renderMeasurement( eq(pos(2, 2)), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        
        inOrder.verify(presentation).dispose();
        
        if (!presentation.getClass().getName().contains("Swing")) // Swing makes its own interactions
          {
            verifyNoMoreInteractions(presentation);
          }
        
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
