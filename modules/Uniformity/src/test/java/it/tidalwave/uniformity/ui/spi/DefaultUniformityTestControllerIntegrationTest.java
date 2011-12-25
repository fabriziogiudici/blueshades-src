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
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JFrame;
import it.tidalwave.argyll.impl.MessageVerifier;
import it.tidalwave.netbeans.util.test.MockLookup;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import it.tidalwave.uniformity.ui.impl.SwingUniformityTestPresentation;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.fest.swing.core.BasicComponentFinder;
import org.fest.swing.core.ComponentFinder;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultUniformityTestControllerIntegrationTest extends DefaultUniformityTestControllerTestSupport
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
            final ComponentFinder componentFinder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            final JButton button = componentFinder.findByName("btContinue", JButton.class);
            invocation.callRealMethod();
            
            EventQueue.invokeLater(new Runnable()
              {
                @Override
                public void run() 
                  {
                    log.info("Clicking on 'Continue'...");
                    button.doClick();
                  }
              });
            
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
        presentation = spy(new SwingUniformityTestPresentation());
        doAnswer(clickContinue).when(presentation).renderInvitation(any(UniformityTestPresentation.Position.class));
        presentationBuilder = mock(UniformityTestPresentationBuilder.class);
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
