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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import it.tidalwave.actor.annotation.MessageListener;
import it.tidalwave.argyll.MeasurementMessage;
import it.tidalwave.argyll.MeasurementRequest;
import it.tidalwave.uniformity.ui.UniformityTestController;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import it.tidalwave.uniformity.ui.UniformityTestPresentation.Position;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.uniformity.ui.UniformityTestPresentation.Position.pos;
import java.awt.event.ActionEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.AbstractAction;
import javax.swing.Action;

/***********************************************************************************************************************
 *
 * @stereotype Controller
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class DefaultUniformityTestController implements UniformityTestController
  {
    private static final Position DEFAULT_CONTROL_PANEL_POSITION = pos(0, 0);
    private static final Position AUX_CONTROL_PANEL_POSITION = pos(0, 1);
    
    @Nonnull
    /* package FIXME */ UniformityTestPresentation presentation;

    private final int columns = 3;
    private final int rows = 3;

    private final List<Position> positions = new ArrayList<Position>();

    private Iterator<Position> cursor;
    
    private Position currentPosition;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    /* package */ final Action continueAction = new AbstractAction("Continue") 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            presentation.renderWhite(currentPosition);
            new MeasurementRequest().sendLater(500, TimeUnit.MILLISECONDS);
            delay(); // FIXME: drop this when you connect to the messagebus
            receiveMeasure(null);
          }
      };
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @MessageListener
    public void receiveMeasure (final @Nonnull MeasurementMessage message)
      {
        presentation.renderMeasurement(currentPosition, "Luminance: 1 cd/m2", "White point: 2420 K");

        if (currentPosition.equals(DEFAULT_CONTROL_PANEL_POSITION))
          {
            presentation.renderControlPanel(currentPosition);
            presentation.renderEmpty(AUX_CONTROL_PANEL_POSITION);
          }
        
        prepareNextMeasurement();  
      }
        
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void initialize()
      {
        computePositions();
        presentation.bind(continueAction);
        presentation.setGridSize(columns, rows);
        presentation.renderControlPanel(DEFAULT_CONTROL_PANEL_POSITION);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void prepareNextMeasurement()
      {
        if (cursor.hasNext())
          {
            currentPosition = cursor.next();   

            if (currentPosition.equals(DEFAULT_CONTROL_PANEL_POSITION))
              {
                presentation.renderControlPanel(AUX_CONTROL_PANEL_POSITION);
              }

            presentation.renderInvitation(currentPosition);
//            waitForNextPressed(); // FIXME
          }
      }
 
    /*******************************************************************************************************************
     * 
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
        
        positions.add(0, positions.remove(4));
        cursor = positions.iterator();
      }
     
    private void delay()
      {
        try
          {
            Thread.sleep(300);
          } 
        catch (InterruptedException e) 
          {
          }
      }
    
//    private void waitForNextPressed()
//      {
//        try
//          {
//            Thread.sleep(300);
//            continueAction.actionPerformed(null);
//          } 
//        catch (InterruptedException e) 
//          {
//          }
//      }
  }
