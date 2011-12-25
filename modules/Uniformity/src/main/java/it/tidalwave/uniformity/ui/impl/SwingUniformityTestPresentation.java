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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import it.tidalwave.uniformity.ui.UniformityTestPresentation;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class SwingUniformityTestPresentation extends JPanel implements UniformityTestPresentation
  {
    private JPanel[][] cell;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private static class Empty extends JPanel
      {
        public Empty()
          {
            setOpaque(true);
            setBackground(Color.GRAY);
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
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private static class Invitation extends JPanel
      {
        public Invitation()
          {
            add(new JLabel("Put the sensor here"));
          }
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private static class ControlPanel extends JPanel
      {
        public ControlPanel()
          {
            add(new JLabel("Control panel"));
          }
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private static class Measurement extends JPanel
      {
        public Measurement()
          {
            add(new JLabel("Measurement"));
          }
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
                        cell[row][column].setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
    public void renderEmpty (final @Nonnegative int row, final @Nonnegative int column) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(row, column, new Empty());
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderInvitation (final @Nonnegative int row, final @Nonnegative int column) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(row, column, new Invitation());
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderControlPanel (final @Nonnegative int row, final @Nonnegative int column) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(row, column, new ControlPanel());
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderWhite (final @Nonnegative int row, final @Nonnegative int column) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(row, column, new White());
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderMeasurement (final @Nonnegative int row,  
                                   final @Nonnegative int column, 
                                   final @Nonnull String luminance, 
                                   final @Nonnull String whitePoint) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(row, column, new Measurement());
              }
          });
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private void setCell (final @Nonnegative int row, final @Nonnegative int column, final @Nonnull Component component)
      {
        cell[row][column].removeAll();
        cell[row][column].add(component, BorderLayout.CENTER);
        validate();
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private static void runSafely (final @Nonnull Runnable runnable)
      {
        try 
          {
            EventQueue.invokeAndWait(new Runnable() 
              {
                @Override
                public void run() 
                  {
                    try
                      {
                        runnable.run();
                      }
                    catch (Throwable t)
                      {
                          t.printStackTrace();
                        log.warn("", t);
                      }
                  }
              });
          } 
        catch (InterruptedException e) 
          {
            log.warn("", e);
          } 
        catch (InvocationTargetException e) 
          {
            log.warn("", e);
          }
      }
  }
