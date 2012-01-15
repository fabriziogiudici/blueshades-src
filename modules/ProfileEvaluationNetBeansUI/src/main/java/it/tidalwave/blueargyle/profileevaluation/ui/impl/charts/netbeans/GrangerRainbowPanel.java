/***********************************************************************************************************************
 *
 * blueArgyle - a Java UI for Argyll
 * Copyright (C) 2011-2012 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class GrangerRainbowPanel extends JXPanel
  {
    @CheckForNull
    private BufferedImage image;

    public GrangerRainbowPanel()
      {
        assert EventQueue.isDispatchThread();
      }
    
    @Override
    public void paint (final @Nonnull Graphics g) 
      {
        assert EventQueue.isDispatchThread();
        
        if (image == null)
          {
            new SwingWorker<Void, Void>() 
              {
                @Override
                protected Void doInBackground() throws Exception 
                  {
                    try
                      {
                        createBackgroundImage();
                      }
                    catch (Exception e)
                      {
                        e.printStackTrace();
                      }
                    return null;
                  }

                @Override
                protected void done()   
                  {
                    final ImagePainter painter = new ImagePainter(image);
                    painter.setScaleToFit(true);
                    setOpaque(false);
                    setBackgroundPainter(painter);
                    repaint();
                  }
            }.execute();
          }
        
        super.paint(g);
      }
    
    // See http://www.openphotographyforums.com/forums/showthread.php?t=12336
    
    private void createBackgroundImage()
      {
        assert !EventQueue.isDispatchThread();
        log.info("createBackgroundImage()");
        final int width = getWidth();
        final int height = getHeight();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        final int margin = 64;
        final int trimmedWidth = width - margin * 2;
        final int trimmedHeight = height - margin * 2;
        g.drawRect(margin - 1, margin - 1, trimmedWidth + 1, trimmedHeight + 1);
        
        drawString(g, "H", margin + trimmedWidth / 2, margin - 20 - 20, 0, SwingConstants.CENTER);
        drawString(g, "B", 4, margin + trimmedHeight / 2, 0, SwingConstants.LEADING);
        drawString(g, "S", width - 4, margin + trimmedHeight / 2, 0, SwingConstants.TRAILING);
        
        float xLabelDelta = (1f / 6) * 0.9999f;
        float yLabelDelta = (1f / 8) * 0.9999f;
        float xLabelNext = 0;
        float yLabelNext = 0;
        
        for (int y = 0; y < trimmedHeight; y++)
          {
            final float yf = (float)y / trimmedHeight;
            final float saturation = Math.min(1f, 2f * yf);
            final float brightness = Math.min(1f, 2f * (1f - yf));
            
            if (yf >= yLabelNext)
              {
                drawString(g, String.format("%.0f %%", brightness * 100), 0,              y + margin, margin, SwingConstants.TRAILING);  
                drawString(g, String.format("%.0f %%", saturation * 100), width - margin, y + margin, margin, SwingConstants.LEADING);  
                yLabelNext += yLabelDelta;
              }  
            
            for (int x = 0; x < trimmedWidth; x++)
              {
                final float xf = (float)x / trimmedWidth;
                final float hue = 1.0f - xf;
                
                if ((y == 0) && (xf >= xLabelNext))
                  {
                    drawString(g, String.format("%.0f °", (1f - hue) * 360), x + margin, margin - 20, 0, SwingConstants.CENTER);  
                    xLabelNext += xLabelDelta;
                  }
                
                image.setRGB(x + margin, y + margin, Color.HSBtoRGB(hue, saturation, brightness));
              }
          }
      }
    
    private static void drawString (final @Nonnull Graphics g, 
                                    final @Nonnull String string,
                                    final @Nonnull int x,
                                    final @Nonnull int y,
                                    final @Nonnull int w,
                                    final @Nonnull int alignment)
      {
        final FontMetrics fm = g.getFontMetrics();
        final int yy = y + fm.getHeight() / 2;  
        final int ww = fm.stringWidth(string);
        int xx = x;
        
        switch (alignment)
          {
            case SwingConstants.LEADING:  
              xx += 4;
              break;
                
            case SwingConstants.TRAILING:  
              xx += w - 4 - ww;
              break;
                
            case SwingConstants.CENTER:  
              xx -= ww / 2;
              break;
          }
                    
        g.drawString(string, xx, yy);
      }
  }
