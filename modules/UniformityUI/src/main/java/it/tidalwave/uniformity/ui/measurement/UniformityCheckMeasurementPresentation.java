/***********************************************************************************************************************
 *
 * blueShades - a Java UI for Argyll
 * Copyright (C) 2011-2016 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://blueshades.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueshades-src
 *
 **********************************************************************************************************************/
package it.tidalwave.uniformity.ui.measurement;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.GraphicsDevice;
import javax.swing.Action;
import it.tidalwave.uniformity.Position;

/***********************************************************************************************************************
 *
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface UniformityCheckMeasurementPresentation 
  {
    /*******************************************************************************************************************
     *
     * Binds to some controller {@link Action}s.
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull Action continueAction, @Nonnull Action cancelAction);
    
    /*******************************************************************************************************************
     * 
     * Sets the grid size.
     *
     ******************************************************************************************************************/
    public void setGridSize (@Nonnegative int rows, @Nonnegative int columns);
    
    /*******************************************************************************************************************
     *
     * Makes the UI visible.
     *
     ******************************************************************************************************************/
    public void showUp (@Nonnull GraphicsDevice graphicsDevice);
    
    /*******************************************************************************************************************
     *
     * Renders an empty cell at the given position.
     *
     ******************************************************************************************************************/
    public void renderEmptyCellAt (@Nonnull Position position);

    /*******************************************************************************************************************
     * 
     * Renders a cell with an invitation to place the sensor at the given position.
     *
     ******************************************************************************************************************/
    public void renderSensorPlacementInvitationCellAt (@Nonnull Position position);

    /*******************************************************************************************************************
     * 
     * Renders the control panel at the given position.
     *
     ******************************************************************************************************************/
    public void renderControlPanelAt (@Nonnull Position position);

    /*******************************************************************************************************************
     * 
     * Renders a white cell for the measurement at the given position.
     *
     ******************************************************************************************************************/
    public void renderWhiteCellAt (@Nonnull Position position);

    /*******************************************************************************************************************
     * 
     * Renders a cell with the measurement results at the given position.
     *
     ******************************************************************************************************************/
    public void renderMeasurementCellAt (@Nonnull Position position,
                                         @Nonnull String luminance, 
                                         @Nonnull String whitePoint);
    
    /*******************************************************************************************************************
     * 
     * Shows that a measure is in progress.
     *
     ******************************************************************************************************************/
    public void showMeasureInProgress();
    
    /*******************************************************************************************************************
     * 
     * Hides the progress indicator.
     *
     ******************************************************************************************************************/
    public void hideMeasureInProgress();
    
    /*******************************************************************************************************************
     *
     * Shows an invitation to do something to the sensor.
     *
     ******************************************************************************************************************/
    public void showInvitationToOperateOnTheSensor (@Nonnull String invitation);

    /*******************************************************************************************************************
     *
     * Hides an invitation to do something to the sensor.
     *
     ******************************************************************************************************************/
    public void hideInvitationToOperateOnTheSensor();
    
    /*******************************************************************************************************************
     *
     * Dismisses the UI.
     *
     ******************************************************************************************************************/
    public void dismiss();
  }
