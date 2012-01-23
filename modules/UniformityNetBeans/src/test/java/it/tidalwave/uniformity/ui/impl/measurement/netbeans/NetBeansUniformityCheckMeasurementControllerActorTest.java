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
package it.tidalwave.uniformity.ui.impl.measurement.netbeans;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.Timer;
import it.tidalwave.uniformity.Position;
import it.tidalwave.uniformity.ui.measurement.UniformityCheckMeasurementPresentation;
import it.tidalwave.uniformity.ui.impl.measurement.UniformityCheckMeasurementControllerActorTestSupport;
import it.tidalwave.uniformity.ui.measurement.UniformityCheckMeasurementPresentationProvider;
import lombok.Delegate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class NetBeansUniformityCheckMeasurementControllerActorTest extends UniformityCheckMeasurementControllerActorTestSupport
  {
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
     * Mockito can't spy() synthetic proxies and unfortunately our Presentation is one. So we wrap it with a regular
     * class.
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor
    static class Wrapper implements UniformityCheckMeasurementPresentation
      {
        @Nonnull @Delegate(types=UniformityCheckMeasurementPresentation.class)
        private final UniformityCheckMeasurementPresentation delegate;
      }
            
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override @Nonnull
    protected UniformityCheckMeasurementPresentation createPresentation()
      {
        final UniformityCheckMeasurementPresentationProvider presentationProvider = new NetBeansUniformityCheckMeasurementPresentationProvider();
        final UniformityCheckMeasurementPresentation presentation = spy(new Wrapper(presentationProvider.getPresentation()));
        doAnswer(clickContinue).when(presentation).renderSensorPlacementInvitationCellAt(any(Position.class));
        return presentation;
      }
  }
