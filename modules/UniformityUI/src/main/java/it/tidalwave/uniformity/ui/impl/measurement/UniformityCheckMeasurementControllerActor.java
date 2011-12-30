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
package it.tidalwave.uniformity.ui.impl.measurement;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.Action;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.MessageSupport;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.actor.annotation.Message;
import it.tidalwave.swing.ActionMessageAdapter;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.argyll.ArgyllFailureMessage;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.uniformity.Position;
import it.tidalwave.uniformity.UniformityCheckRequest;
import it.tidalwave.uniformity.UniformityMeasurement;
import it.tidalwave.uniformity.UniformityMeasurements;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.ui.measurement.UniformityCheckMeasurementPresentation;
import it.tidalwave.uniformity.ui.measurement.UniformityCheckMeasurementPresentationProvider;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import static java.util.concurrent.TimeUnit.*;
import static it.tidalwave.actor.Collaboration.*;
import static it.tidalwave.colorimetry.ColorPoint.ColorSpace.*;
import static it.tidalwave.uniformity.Position.xy;

/***********************************************************************************************************************
 *
 * @stereotype Controller
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Actor(threadSafe=false) @NotThreadSafe @Slf4j
public class UniformityCheckMeasurementControllerActor
  {
    @Message(outOfBand=true) @ToString
    private static class DoMeasurementMessage extends MessageSupport
      {
      }
    
    @Message(outOfBand=true) @ToString
    private static class CancelMessage extends MessageSupport
      {
      }
    
    private static final int COLUMNS = 3;
    private static final int ROWS = 3;

    private static final Position DEFAULT_CONTROL_PANEL_POSITION = xy(0, 0);
    private static final Position ALTERNATE_CONTROL_PANEL_POSITION = xy(0, 1);
    
    private static final int MEASUREMENT_DELAY = 100;
    
    private final Provider<UniformityCheckMeasurementPresentationProvider> presentationBuilder = Locator.createProviderFor(UniformityCheckMeasurementPresentationProvider.class);
    
    private UniformityCheckMeasurementPresentation presentation;

    private String displayName;
    
    private final List<Position> positionSequence = new ArrayList<Position>();

    private Iterator<Position> positionIterator;
    
    private Position currentPosition;
    
    private Collaboration collaborationPendingUserIntervention = NULL_COLLABORATION;
    
    private Object suspensionToken;
    
    private final SortedMap<Position, UniformityMeasurement> measurementMapByPosition = new TreeMap<Position, UniformityMeasurement>();
    
    private final Action continueAction = new ActionMessageAdapter("Continue", new DoMeasurementMessage()); 
    
    private final Action cancelAction = new ActionMessageAdapter("Cancel", new CancelMessage()); 
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void start (final @ListensTo @Nonnull UniformityCheckRequest message)
      {
        log.info("start({})", message);
        displayName = message.getDisplayName();
        initializeMeasurement();
        prepareNextMeasurement(message.getCollaboration());  
      }
        
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void processMeasure (final @ListensTo @Nonnull MeasurementMessage message) 
      throws NotFoundException
      {
        log.info("processMeasure({})", message);
        presentation.hideMeasureInProgress();
        // FIXME: do the right math here
        final UniformityMeasurement measurement = new UniformityMeasurement(message.getCcTemperature().getMeasure(), 
                                                                            (int)message.getColorPoints().find(Lab).getC1());
        measurementMapByPosition.put(currentPosition, measurement);
        presentation.renderMeasurementCellAt(currentPosition,
                                             String.format("Luminance: %d cd/m\u00b2", measurement.getLuminance()), 
                                             String.format("White point: %d K", measurement.getTemperature().getT()));
        eventuallyMoveInControlPanel();
        prepareNextMeasurement(message.getCollaboration());  
      }
        
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void failure (final @ListensTo @Nonnull ArgyllFailureMessage message) 
      {
        log.info("failure({})", message);
        new CancelMessage().send(); // FIXME: harsh, do a notification on the UI too
      }
        
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void doMeasurement (final @ListensTo @Nonnull DoMeasurementMessage message)
      {
        log.info("doMeasurement()");
        continueAction.setEnabled(false);
        cancelAction.setEnabled(false);
        collaborationPendingUserIntervention.resume(suspensionToken, new Runnable()
          {
            @Override
            public void run() 
              {
                presentation.renderWhiteCellAt(currentPosition);
                presentation.showMeasureInProgress();
                new MeasurementRequest().sendLater(MEASUREMENT_DELAY, MILLISECONDS);
              }
          });

        suspensionToken = null;
        collaborationPendingUserIntervention = NULL_COLLABORATION;
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void cancel (final @ListensTo @Nonnull CancelMessage message)
      {
        log.info("cancel()");
        cancelAction.setEnabled(false);
        
        if (suspensionToken != null)
          {
            collaborationPendingUserIntervention.resume(suspensionToken, new Runnable()
              {
                @Override
                public void run() 
                  {
                    // do nothing, but it will make the Collaboration to complete
                    // TODO: perhaps a specific method on Collaboration would make sense (such as collaborationPendingUserIntervention.terminate()).
                  }
              });

            suspensionToken = null;
            collaborationPendingUserIntervention = NULL_COLLABORATION;
          }

        presentation.dismiss();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void initializeMeasurement()
      {
        log.info("initializeMeasurement()");
        presentation = presentationBuilder.get().getPresentation();
        computePositions();
        measurementMapByPosition.clear();
        presentation.bind(continueAction, cancelAction);
        presentation.setGridSize(COLUMNS, ROWS);
        continueAction.setEnabled(false);
        cancelAction.setEnabled(false);
        presentation.showUp();
        presentation.renderControlPanelAt(DEFAULT_CONTROL_PANEL_POSITION);
      }
    
    /*******************************************************************************************************************
     * 
     * Prepares the next measurement inviting the user to properly position the sensor.
     *
     ******************************************************************************************************************/
    private void prepareNextMeasurement (final @Nonnull Collaboration collaboration)
      {
        log.info("prepareNextMeasurement()");
        
        if (!positionIterator.hasNext())
          {
            presentation.dismiss();  
            new UniformityMeasurementMessage(new UniformityMeasurements(displayName, measurementMapByPosition)).send();
          }
        else
          {
            currentPosition = positionIterator.next(); 
            collaborationPendingUserIntervention = collaboration;
            suspensionToken = collaborationPendingUserIntervention.suspend();
            presentation.renderSensorPlacementInvitationCellAt(currentPosition);
            eventuallyMoveOutControlPanel();
            continueAction.setEnabled(true);
            cancelAction.setEnabled(true);
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
            presentation.renderControlPanelAt(ALTERNATE_CONTROL_PANEL_POSITION);
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
            presentation.renderControlPanelAt(DEFAULT_CONTROL_PANEL_POSITION);
            presentation.renderEmptyCellAt(ALTERNATE_CONTROL_PANEL_POSITION);
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
        positionSequence.clear();
        
        for (int row = 0; row < ROWS; row++)
          {
            for (int column = 0; column < COLUMNS; column++)
              {
                positionSequence.add(xy(column, row));
              }
          }
        
        positionSequence.add(0, positionSequence.remove((ROWS * COLUMNS) / 2));
        positionIterator = positionSequence.iterator();
      }
  }
