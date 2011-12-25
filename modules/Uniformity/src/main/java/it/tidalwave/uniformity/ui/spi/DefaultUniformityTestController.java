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
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.MessageSupport;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.MessageListener;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.uniformity.UniformityTestRequest;
import it.tidalwave.uniformity.ui.UniformityTestController;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import it.tidalwave.uniformity.ui.UniformityTestPresentation.Position;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.uniformity.ui.UniformityTestPresentation.Position.pos;
import javax.inject.Provider;

/***********************************************************************************************************************
 *
 * @stereotype Controller
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Actor(threadSafe=false) @NotThreadSafe @Slf4j
public class DefaultUniformityTestController implements UniformityTestController
  {
    private static final Position DEFAULT_CONTROL_PANEL_POSITION = pos(0, 0);
    private static final Position ALTERNATE_CONTROL_PANEL_POSITION = pos(0, 1);
    
    private final Provider<UniformityTestPresentationBuilder> presentationBuilder = Locator.createProviderFor(UniformityTestPresentationBuilder.class);
    
    private UniformityTestPresentation presentation;

    private final int columns = 3;
    
    private final int rows = 3;

    private final List<Position> positions = new ArrayList<Position>();

    private Iterator<Position> positionIterator;
    
    private Position currentPosition;
    
    private Collaboration collaborationPendingUserIntervention = Collaboration.NULL_COLLABORATION;
    
    private Object suspensionToken;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    /* package */ final Action continueAction = new AbstractAction("Continue") 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            collaborationPendingUserIntervention.resume(suspensionToken, new Runnable()
              {
                @Override
                public void run() 
                  {
                    presentation.renderWhite(currentPosition);
                    new MeasurementRequest().sendLater(500, TimeUnit.MILLISECONDS);
                    collaborationPendingUserIntervention = Collaboration.NULL_COLLABORATION;
                  }
              });
          }
      };
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @MessageListener
    public void start (final @Nonnull UniformityTestRequest message)
      {
        log.info("start({})", message);
        initialize();
        prepareNextMeasurement(message);  
      }
        
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @MessageListener
    public void processMeasure (final @Nonnull MeasurementMessage message)
      {
        log.info("processMeasure({})", message);
        presentation.renderMeasurement(currentPosition, "Luminance: 1 cd/m2", "White point: 2420 K");
        eventuallyMoveInControlPanel();
        prepareNextMeasurement(message);  
      }
        
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void initialize()
      {
        log.info("initialize()");
        presentation = presentationBuilder.get().buildUI();
        computePositions();
        presentation.bind(continueAction);
        presentation.setGridSize(columns, rows);
        presentation.renderControlPanel(DEFAULT_CONTROL_PANEL_POSITION);
      }
    
    /*******************************************************************************************************************
     * 
     * Prepares the next measurement inviting the user to properly position the sensor.
     *
     ******************************************************************************************************************/
    private void prepareNextMeasurement (final @Nonnull MessageSupport message)
      {
        log.info("prepareNextMeasurement()");
        
        if (!positionIterator.hasNext())
          {
            presentation.dispose();  
          }
        else
          {
            currentPosition = positionIterator.next(); 
            collaborationPendingUserIntervention = message.getCollaboration();
            suspensionToken = collaborationPendingUserIntervention.suspend();
            presentation.renderInvitation(currentPosition);
            eventuallyMoveOutControlPanel();
          }
      }
 
    /*******************************************************************************************************************
     *
     * If the current position is the one where the ControlPanel is rendered, move it to the alternate position.
     *
     ******************************************************************************************************************/
    private void eventuallyMoveOutControlPanel()
      {
        if (currentPosition.equals(DEFAULT_CONTROL_PANEL_POSITION))
          {
            presentation.renderControlPanel(ALTERNATE_CONTROL_PANEL_POSITION);
          }
      }
    
    /*******************************************************************************************************************
     *
     * If the ControlPanel was moved to the alternate position, restore it.
     *
     ******************************************************************************************************************/
    private void eventuallyMoveInControlPanel()
      {
        if (currentPosition.equals(DEFAULT_CONTROL_PANEL_POSITION))
          {
            presentation.renderControlPanel(currentPosition);
            presentation.renderEmpty(ALTERNATE_CONTROL_PANEL_POSITION);
          }
      }
    
    /*******************************************************************************************************************
     * 
     * Compute the sequence of positions to make measurements. It starts from the central cell then it proceeds from the
     * upper left control, rightbound and downwards.
     *
     ******************************************************************************************************************/
    private void computePositions()
      {
        positions.clear();
        
        for (int row = 0; row < rows; row++)
          {
            for (int column = 0; column < columns; column++)
              {
                positions.add(pos(column, row));
              }
          }
        
        positions.add(0, positions.remove((rows * columns) / 2));
        positionIterator = positions.iterator();
      }
  }
