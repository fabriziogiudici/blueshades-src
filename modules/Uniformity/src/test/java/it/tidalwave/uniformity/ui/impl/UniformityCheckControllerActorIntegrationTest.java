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

import javax.annotation.Nonnull;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;
import it.tidalwave.argyll.impl.MessageVerifier;
import it.tidalwave.netbeans.util.test.MockLookup;
import it.tidalwave.uniformity.ui.UniformityCheckPresentation;
import it.tidalwave.uniformity.ui.spi.UniformityCheckPresentationBuilder;
import it.tidalwave.uniformity.ui.impl.swing.UniformityCheckPresentationPanel;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;
import static org.fest.swing.core.BasicComponentFinder.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class UniformityCheckControllerActorIntegrationTest extends UniformityCheckControllerActorTestSupport
  {
    private JFrame frame;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private final Answer<Void> clickContinue = new Answer<Void>()
      {
        @Override
        public Void answer (final @Nonnull InvocationOnMock invocation) 
          throws Throwable 
          {
            invocation.callRealMethod();

            final Timer timer = new Timer(1000, new ActionListener() 
              {
                @Override
                public void actionPerformed (final @Nonnull ActionEvent event) 
                  {
                    log.info("Clicking on 'Continue'...");
                    finderWithCurrentAwtHierarchy().findByName("btContinue", JButton.class).doClick();
                  }
              });

            timer.setRepeats(false);
            timer.start();
            
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
        presentation = spy(new UniformityCheckPresentationPanel());
        doAnswer(clickContinue).when(presentation).renderInvitation(any(UniformityCheckPresentation.Position.class));
        presentationBuilder = mock(UniformityCheckPresentationBuilder.class);
        doReturn(presentation).when(presentationBuilder).buildUI();
        
        frame = new JFrame();
        frame.add((Component)presentation);
        frame.setSize(1024, 768);
        frame.setVisible(true);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @AfterMethod
    public void cleanup()
      throws InterruptedException
      {
        messageVerifier.dispose();
        testActivator.deactivate();
        messageVerifier = null;
        testActivator = null;
        presentation = null;
        
        if (frame != null)
          {
            Thread.sleep(2000);
            frame.dispose();
          }
        
        frame = null;
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
        xxx();
      }
  }
