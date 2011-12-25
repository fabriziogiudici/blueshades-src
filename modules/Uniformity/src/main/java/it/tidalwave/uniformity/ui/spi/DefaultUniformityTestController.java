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

import it.tidalwave.uniformity.ui.UniformityTestController;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import javax.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

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
    @Nonnull
    /* package FIXME */ UniformityTestPresentation presentation;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void run()
      {
        final int columns = 3;
        final int rows = 3;
        presentation.setGridSize(columns, rows);
        
        for (int row = 0; row < rows; row++)
          {
            for (int column = 0; column < columns; column++)
              {
                if ((column == 0) && (row == 0))
                  {
                    presentation.renderControlPanel(row, column);
                  }
                else if ((column == 1) && (row == 1))
                  {
                    presentation.renderInvitation(row, column);
                  }
                else
                  {
                    presentation.renderEmpty(row, column);
                  }
              }
          }
        
        int row, column;
        
        waitForNextPressed();
        presentation.renderWhite(1, 1);
        measure();
        presentation.renderMeasurement(1, 1, "Luminance: 1 cd/m2", "White point: 2720 K");
        presentation.renderInvitation(0, 0);
        presentation.renderControlPanel(1, 0);
        
        waitForNextPressed();
        row = 0;
        column = 0;
        presentation.renderWhite(row, column);
        measure();
        presentation.renderMeasurement(row, column, "Luminance: 1 cd/m2", "White point: 2420 K");
        presentation.renderControlPanel(row, column);
        presentation.renderEmpty(row + 1, column);
        presentation.renderInvitation(row, column + 1);
        
        waitForNextPressed();
        column++;
        presentation.renderWhite(row, column);
        measure();
        presentation.renderMeasurement(row, column, "Luminance: 1 cd/m2", "White point: 2420 K");
        presentation.renderInvitation(row, column + 1);
        
        waitForNextPressed();
        column++;
        presentation.renderWhite(row, column);
        measure();
        presentation.renderMeasurement(row, column, "Luminance: 1 cd/m2", "White point: 2420 K");
        presentation.renderInvitation(row + 1, 0);
        
        waitForNextPressed();
        row = 1;
        column = 0;
        presentation.renderWhite(row, column);
        measure();
        presentation.renderMeasurement(row, column, "Luminance: 1 cd/m2", "White point: 2420 K");
        
        column++;
        
        presentation.renderInvitation(row, column + 1);
        
        waitForNextPressed();
        column++;
        presentation.renderWhite(row, column);
        measure();
        presentation.renderMeasurement(row, column, "Luminance: 1 cd/m2", "White point: 2420 K");
        presentation.renderInvitation(row + 1, 0);
        
        waitForNextPressed();
        row = 2;
        column = 0;
        presentation.renderWhite(row, column);
        measure();
        presentation.renderMeasurement(row, column, "Luminance: 1 cd/m2", "White point: 2420 K");
        presentation.renderInvitation(row, column + 1);
        
        waitForNextPressed();
        column++;
        presentation.renderWhite(row, column);
        measure();
        presentation.renderMeasurement(row, column, "Luminance: 1 cd/m2", "White point: 2420 K");
        presentation.renderInvitation(row, column + 1);
        
        column++;
        waitForNextPressed();
        presentation.renderWhite(row, column);
        measure();
        presentation.renderMeasurement(row, column, "Luminance: 1 cd/m2", "White point: 2420 K");
      }
    
    private void measure()
      {
        try
          {
            Thread.sleep(300);
          } 
        catch (InterruptedException e) 
          {
          }
      }
    private void waitForNextPressed()
      {
        try
          {
            Thread.sleep(300);
          } 
        catch (InterruptedException e) 
          {
          }
      }
  }
