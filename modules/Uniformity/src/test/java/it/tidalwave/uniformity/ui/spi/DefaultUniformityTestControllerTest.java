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

import javax.swing.JFrame;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import it.tidalwave.uniformity.ui.impl.SwingUniformityTestPresentation;
import lombok.extern.slf4j.Slf4j;
import org.mockito.InOrder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
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
    private DefaultUniformityTestController fixture;
    
    private UniformityTestPresentation presentation;
    
    private InOrder inOrder;
    
    @BeforeMethod
    public void setupFixture()
      {
        fixture = new DefaultUniformityTestController();   
        presentation = mock(UniformityTestPresentation.class);
        inOrder = inOrder(presentation);
        fixture.presentation = presentation;
      }
    
    @Test
    public void must_follow_the_proper_sequence_3x3()
      {
        fixture.run();  
        
        inOrder.verify(presentation).setGridSize(eq(3), eq(3));
        
        inOrder.verify(presentation).renderControlPanel(eq(0), eq(0));
        inOrder.verify(presentation).renderInvitation(  eq(1), eq(1));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(1), eq(1));

        // measure
        inOrder.verify(presentation).renderMeasurement( eq(1), eq(1), eq("Luminance: 1 cd/m2"), eq("White point: 2720 K"));
        inOrder.verify(presentation).renderInvitation(  eq(0), eq(0));
        inOrder.verify(presentation).renderControlPanel(eq(1), eq(0));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(0), eq(0));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(0), eq(0), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderControlPanel(eq(0), eq(0));
        inOrder.verify(presentation).renderEmpty       (eq(1), eq(0));
        inOrder.verify(presentation).renderInvitation(  eq(0), eq(1));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(0), eq(1));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(0), eq(1), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(0), eq(2));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(0), eq(2));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(0), eq(2), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(1), eq(0));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(1), eq(0));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(1), eq(0), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(1), eq(2));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(1), eq(2));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(1), eq(2), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(2), eq(0));
        
        waitForNextPressed();
        inOrder.verify(presentation).renderWhite(       eq(2), eq(0));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(2), eq(0), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(2), eq(1));
        
        waitForNextPressed();;
        inOrder.verify(presentation).renderWhite(       eq(2), eq(1));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(2), eq(1), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
        inOrder.verify(presentation).renderInvitation(  eq(2), eq(2));
        
        waitForNextPressed();;
        inOrder.verify(presentation).renderWhite(       eq(2), eq(2));
        // measure
        inOrder.verify(presentation).renderMeasurement( eq(2), eq(2), eq("Luminance: 1 cd/m2"), eq("White point: 2420 K"));
      }
    
    @Test
    public void must_follow_the_proper_sequence_3x3b() throws InterruptedException
      {
        JFrame jframe = new JFrame();
        final SwingUniformityTestPresentation presentation = new SwingUniformityTestPresentation();
        jframe.add(presentation);
        jframe.setSize(800, 600);
        jframe.setVisible(true);
        fixture.presentation = presentation;
        fixture.run();  
//        
//        Thread.sleep(99999);
      }
    
    private void waitForNextPressed()
      {
//        try
//          {
//            log.info("WAITING");
//            Thread.sleep(2000);
//          } 
//        catch (InterruptedException e) 
//          {
//          }
      }
  }
