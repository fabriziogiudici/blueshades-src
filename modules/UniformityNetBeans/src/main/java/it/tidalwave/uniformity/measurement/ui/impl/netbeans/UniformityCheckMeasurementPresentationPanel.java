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
package it.tidalwave.uniformity.measurement.ui.impl.netbeans;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import it.tidalwave.uniformity.Position;
import it.tidalwave.uniformity.measurement.ui.UniformityCheckMeasurementPresentation;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.blueargyle.util.SwingSafeRunner.*;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class UniformityCheckMeasurementPresentationPanel extends JPanel implements UniformityCheckMeasurementPresentation
  {
    /* package */ final static Color DEFAULT_BACKGROUND = new Color(100, 100, 100);
    /* package */ final static Color CONTROL_PANEL_BACKGROUND = new Color(80, 80, 80);
    /* package */ final static Color BORDER_COLOR = new Color(120, 120, 120);
    
    private JPanel[][] cell;
    
    private ControlPanel controlPanel;
    
    private Action continueAction;
    
    private Action cancelAction;

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private static class Empty extends JPanel
      {
        public Empty()
          {
            setOpaque(true);
            setBackground(DEFAULT_BACKGROUND);
          }
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private static class White extends JPanel
      {
        public White()
          {
            setOpaque(true);
            setBackground(Color.WHITE);
          }
      }
    
//    /*******************************************************************************************************************
//     * 
//     *
//     ******************************************************************************************************************/
//    public UniformityCheckPresentationPanel()
//      {
//        setOpaque(true);
//        setBackground(new Color(235, 235, 235));
//      }
//    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void bind (final @Nonnull Action continueAction, final @Nonnull Action cancelAction)
      {
        this.continueAction = continueAction;
        this.cancelAction = cancelAction;
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void setGridSize (final @Nonnegative int rows, final @Nonnegative int columns)
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                removeAll();
                setLayout(new GridLayout(rows, columns));

                cell = new JPanel[rows][columns];

                for (int row = 0; row < rows; row++)
                  {
                    for (int column = 0; column < columns; column++)
                      {
                        add(cell[row][column] = new JPanel(new BorderLayout()));
                        cell[row][column].setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                        cell[row][column].add(new Empty(), BorderLayout.CENTER);
                      }
                  }
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void showUp()
      {
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderEmptyCellAt (final @Nonnull Position position) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(position, new Empty());
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderSensorPlacementInvitationCellAt (final @Nonnull Position position) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(position, new SensorPlacementInvitationComponent());
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderControlPanelAt (final @Nonnull Position position) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(position, controlPanel = new ControlPanel(continueAction, cancelAction));
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderWhiteCellAt (final @Nonnull Position position) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(position, new White());
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderMeasurementCellAt (final @Nonnull Position position, 
                                   final @Nonnull String luminance, 
                                   final @Nonnull String whitePoint) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(position, new MeasurementPanel(luminance, whitePoint));
              }
          });
      }
    
    /*******************************************************************************************************************
     * 
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void showMeasureInProgress()
      {
        setCursor(createBlankCursor());
        
        if (controlPanel != null)
          { 
            controlPanel.setProgressIndicatorVisible(true);  
          }
      }  
    
    /*******************************************************************************************************************
     * 
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void hideMeasureInProgress()
      {
        if (controlPanel != null)
          { 
            controlPanel.setProgressIndicatorVisible(false);  
          }
        
        setCursor(Cursor.getDefaultCursor());
      }  
      
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void dismiss() 
      {
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void setCell (final @Nonnull Position position, final @Nonnull Component component)
      {
        log.info("setCell({}, {})", position, component);
        cell[position.row][position.column].removeAll();
        cell[position.row][position.column].add(component, BorderLayout.CENTER);
        validate();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    private static Cursor createBlankCursor()
      {
        final BufferedImage blank = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        return Toolkit.getDefaultToolkit().createCustomCursor(blank, new Point(0, 0), "blank cursor");
      }
  }