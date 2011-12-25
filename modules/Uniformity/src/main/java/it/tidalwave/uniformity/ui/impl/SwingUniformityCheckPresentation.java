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
import it.tidalwave.uniformity.ui.UniformityCheckPresentation;
import javax.swing.Action;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class SwingUniformityCheckPresentation extends JPanel implements UniformityCheckPresentation
  {
    private JPanel[][] cell;
    
    private Action continueAction;

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
    @Override
    public void bind (final @Nonnull Action continueAction)
      {
        this.continueAction = continueAction;
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
    public void renderEmpty (final @Nonnull Position position) 
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
    public void renderInvitation (final @Nonnull Position position) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(position, new Invitation());
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderControlPanel (final @Nonnull Position position) 
      {
        runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                setCell(position, new ControlPanel(continueAction));
              }
          });
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Override
    public void renderWhite (final @Nonnull Position position) 
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
    public void renderMeasurement (final @Nonnull Position position, 
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
     ******************************************************************************************************************/
    @Override
    public void dispose() 
      {
        log.info("dispose()");
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
    private static void runSafely (final @Nonnull Runnable runnable)
      {
        final Runnable guardedRunnable = new Runnable() 
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
          };

        if (EventQueue.isDispatchThread())
          {
            guardedRunnable.run();
          }
        else
          {
            try 
              {
                EventQueue.invokeAndWait(guardedRunnable);
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
  }
