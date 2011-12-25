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
import it.tidalwave.uniformity.ui.UniformityTestController;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import it.tidalwave.uniformity.ui.UniformityTestPresentation.Position;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.uniformity.ui.UniformityTestPresentation.Position.pos;

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
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void run()
      {
        final int columns = 3;
        final int rows = 3;
        presentation.setGridSize(columns, rows);
        
        final List<UniformityTestPresentation.Position> pp = new ArrayList<UniformityTestPresentation.Position>();
        
        for (int row = 0; row < rows; row++)
          {
            for (int column = 0; column < columns; column++)
              {
                pp.add(pos(column, row));
              }
          }
        
        pp.add(0, pp.remove(4));

        presentation.renderControlPanel(DEFAULT_CONTROL_PANEL_POSITION.row, DEFAULT_CONTROL_PANEL_POSITION.column);
        
        final Iterator<UniformityTestPresentation.Position> ii = pp.iterator();
        
        for (int i = 0; i < 9; i++)
          {
            UniformityTestPresentation.Position p = ii.next();   
            
            if (p.equals(DEFAULT_CONTROL_PANEL_POSITION))
              {
                presentation.renderControlPanel(AUX_CONTROL_PANEL_POSITION.row, AUX_CONTROL_PANEL_POSITION.column);
              }

            presentation.renderInvitation(p.row, p.column);
            waitForNextPressed();
            presentation.renderWhite(p.row, p.column);
            measure();
            presentation.renderMeasurement(p.row, p.column, "Luminance: 1 cd/m2", "White point: 2420 K");

            if (p.equals(DEFAULT_CONTROL_PANEL_POSITION))
              {
                presentation.renderControlPanel(p.row, p.column);
                presentation.renderEmpty(AUX_CONTROL_PANEL_POSITION.row, AUX_CONTROL_PANEL_POSITION.column);
              }
          }
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
