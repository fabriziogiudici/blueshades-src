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
package it.tidalwave.uniformity.ui.impl.main.netbeans;

import javax.annotation.Nonnull;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;
import it.tidalwave.swing.FixedAspectRatioLayout;
import it.tidalwave.swing.ProportionalLayout;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class UniformityMeasurementsPanel extends JPanel
  {
    private final static String IMAGE_RESOURCE = "/it/tidalwave/uniformity/ui/impl/netbeans/2000px-Flat_monitor.svg.png";
    
    private final BufferedImage backgroundImage;
    
    private final GridLayout gridLayout = new GridLayout();
    
    private final JXPanel displayPanel = new JXPanel(new ProportionalLayout(0.035, 0.035, 0.045, 0.30));
    
    private final JPanel innerPanel = new JPanel(gridLayout);
    
    private final ImagePainter painter;
    
    public UniformityMeasurementsPanel()
      {
        try 
          {
            assert EventQueue.isDispatchThread();
            backgroundImage = ImageIO.read(getClass().getResource(IMAGE_RESOURCE));
            painter = new ImagePainter(backgroundImage);
            painter.setScaleToFit(true);
            displayPanel.setBackgroundPainter(painter);
            final double imageAspectRatio = (double)backgroundImage.getWidth() / backgroundImage.getHeight();
            setLayout(new FixedAspectRatioLayout(imageAspectRatio));
            add(displayPanel);
            displayPanel.add(innerPanel);
            setOpaque(false);
          }
        catch (IOException e)
          {
            throw new RuntimeException(e);
          }
      }
        
    public void renderMeasurements (final @Nonnull String[][] measurements)
      {
        assert EventQueue.isDispatchThread();
        innerPanel.removeAll();
        gridLayout.setColumns(measurements[0].length);
        gridLayout.setRows(measurements.length);
        gridLayout.setHgap(1);
        gridLayout.setVgap(1);
        
        for (int row = 0; row < gridLayout.getRows(); row++)
          {
            for (int column = 0; column < gridLayout.getColumns(); column++)
              {
                final JLabel label = new JLabel("<html>" + measurements[row][column].replaceAll("\n", "<br>") + "</html>");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                innerPanel.add(label);
              }
          }
        
        innerPanel.validate();
      }
  }
