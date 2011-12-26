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

import java.awt.Component;
import javax.swing.Action;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.spi.ActorActivator;
import it.tidalwave.actor.spi.ActorGroupActivator;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.argyll.impl.MessageVerifier;
import it.tidalwave.argyll.impl.MockSpotReadActor;
import it.tidalwave.uniformity.UniformityCheckRequest;
import it.tidalwave.uniformity.ui.UniformityCheckPresentation;
import it.tidalwave.uniformity.ui.spi.UniformityCheckPresentationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.mockito.InOrder;
import static it.tidalwave.uniformity.ui.UniformityCheckPresentation.Position.pos;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class UniformityCheckControllerActorTestSupport 
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
            add(new ActorActivator(UniformityCheckControllerActor.class, 1));
          }
      }
    
    protected TestActivator testActivator;
    
    protected InOrder inOrder;
    
    protected MessageVerifier messageVerifier;
    
    protected UniformityCheckPresentationBuilder presentationBuilder;
    
    protected UniformityCheckPresentation presentation;
    
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
        final Collaboration collaboration = new UniformityCheckRequest().send();
        collaboration.waitForCompletion();
        
        inOrder.verify(presentation).bind(any(Action.class), any(Action.class));
        inOrder.verify(presentation).setGridSize(eq(3), eq(3));
        inOrder.verify(presentation).showUp();
        inOrder.verify(presentation).renderControlPanelAt(eq(pos(0, 0)));
        
        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(1, 1)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(1, 1)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(1, 1)), eq("Luminance: 101 cd/m2"), eq("White point: 6001 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(0, 0)));
        inOrder.verify(presentation).renderControlPanelAt(                 eq(pos(0, 1)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(0, 0)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(0, 0)), eq("Luminance: 102 cd/m2"), eq("White point: 6002 K"));
        inOrder.verify(presentation).renderControlPanelAt(                 eq(pos(0, 0)));
        inOrder.verify(presentation).renderEmptyCellAt(                    eq(pos(0, 1)));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(1, 0)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(1, 0)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(1, 0)), eq("Luminance: 103 cd/m2"), eq("White point: 6003 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(2, 0)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(2, 0)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(2, 0)), eq("Luminance: 104 cd/m2"), eq("White point: 6004 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(0, 1)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(0, 1)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(0, 1)), eq("Luminance: 105 cd/m2"), eq("White point: 6005 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(2, 1)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(2, 1)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(2, 1)), eq("Luminance: 106 cd/m2"), eq("White point: 6006 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(0, 2)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(0, 2)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(0, 2)), eq("Luminance: 107 cd/m2"), eq("White point: 6007 K"));
        
        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(1, 2)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(1, 2)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(1, 2)), eq("Luminance: 108 cd/m2"), eq("White point: 6008 K"));

        inOrder.verify(presentation).renderSensorPlacementInvitationCellAt(eq(pos(2, 2)));
        inOrder.verify(presentation).renderWhiteCellAt(                    eq(pos(2, 2)));
        inOrder.verify(presentation).renderMeasurementCellAt(              eq(pos(2, 2)), eq("Luminance: 109 cd/m2"), eq("White point: 6009 K"));
        
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
        
        messageVerifier.verifyCollaborationCompleted();
      }
  }
