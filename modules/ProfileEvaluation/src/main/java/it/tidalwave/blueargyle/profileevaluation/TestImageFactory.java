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
package it.tidalwave.blueargyle.profileevaluation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import it.tidalwave.image.EditableImage;
import it.tidalwave.image.op.AssignColorProfileOp;
import it.tidalwave.image.op.CreateOp;
import it.tidalwave.image.op.WriteOp;
import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static javax.swing.SwingConstants.*;
import static it.tidalwave.image.ImageUtils.*;
import static it.tidalwave.image.EditableImage.DataType.*;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor @Slf4j
public final class TestImageFactory 
  {
    /*******************************************************************************************************************
     *
     * See http://www.openphotographyforums.com/forums/showthread.php?t=12336
     *
     ******************************************************************************************************************/
    @Nonnull
    public static EditableImage createGrangerRainbow (final @Nonnegative int width,
                                                      final @Nonnegative int height,
                                                      final @Nonnull String profileName)
      {
        log.info("createGrangerRainbow({}, {}, {})", new Object[] { width, height, profileName });
        
        final ICC_Profile profile = loadProfile(profileName);
        
        final EditableImage image = EditableImage.create(new CreateOp(width, height, BYTE, Color.WHITE));
        final BufferedImage bImage = image.getInnerProperty(BufferedImage.class);
        final Graphics g = bImage.createGraphics();
        final Font font = g.getFont().deriveFont(10.0f);
        final Font largeFont = g.getFont().deriveFont(16.0f).deriveFont(Font.BOLD);
        g.setColor(Color.BLACK);
        final int margin = 64;
        final int trimmedWidth = width - margin * 2;
        final int trimmedHeight = height - margin * 2;
        g.drawRect(margin - 1, margin - 1, trimmedWidth + 1, trimmedHeight + 1);
        
        g.setFont(largeFont);
        drawString(g, "H", margin + trimmedWidth / 2, margin - 20 - 20, 0, CENTER);
        drawString(g, "B", 4, margin + trimmedHeight / 2, 0, LEADING);
        drawString(g, "S", width - 4, margin + trimmedHeight / 2, 0, TRAILING);
        drawString(g, getICCProfileName(profile), margin + trimmedWidth / 2, margin + trimmedHeight + margin / 2, 0, CENTER);
        g.setFont(font);
        
        float xLabelDelta = (1f / 6) * 0.9999f;
        float yLabelDelta = (1f / 8) * 0.9999f;
        float xLabelNext = 0;
        float yLabelNext = 0;
        
        for (int y = 0; y < trimmedHeight; y++)
          {
            final float yf = (float)y / (trimmedHeight - 1);
            final float saturation = Math.min(1f, 2f * yf);
            final float brightness = Math.min(1f, 2f * (1f - yf));
            
            if (yf >= yLabelNext)
              {
                drawString(g, String.format("%.0f %%", brightness * 100), 0,              y + margin, margin, TRAILING);  
                drawString(g, String.format("%.0f %%", saturation * 100), width - margin, y + margin, margin, LEADING);  
                yLabelNext += yLabelDelta;
              }  
            
            for (int x = 0; x < trimmedWidth; x++)
              {
                final float xf = (float)x / (trimmedWidth - 1);
                final float hue = 1.0f - xf;
                
                if ((y == 0) && (xf >= xLabelNext))
                  {
                    drawString(g, String.format("%.0f °", (1f - hue) * 360), x + margin, margin - 20, 0, CENTER);  
                    xLabelNext += xLabelDelta;
                  }
                
                bImage.setRGB(x + margin, y + margin, Color.HSBtoRGB(hue, saturation, brightness));
              }
          }
        
        // execute() in place doesn't work
        final EditableImage image2 = image.execute2(new AssignColorProfileOp(profile));
///        image2.execute(new WriteOp("TIFF", "/tmp/grangersynth " + getICCProfileName(profile) + ".tif"));
        return image2;
      }
    
    /*******************************************************************************************************************
     *
     * .
     *
     ******************************************************************************************************************/
    @Nonnull
    private static ICC_Profile loadProfile (final @Nonnull String profileName)
      {
        try
          {
            final String resourceName = String.format("/it/tidalwave/blueargyle/profileevaluation/profiles/%s.icc", profileName);
            final @Cleanup InputStream is = TestImageFactory.class.getResourceAsStream(resourceName);
            
            if (is == null)
              {
                throw new FileNotFoundException(resourceName);
              }
            
            return ICC_Profile.getInstance(is);
          }
        catch (IOException e)
          {
            throw new IllegalArgumentException(e);
          }
      }
    
    /*******************************************************************************************************************
     *
     * .
     *
     ******************************************************************************************************************/
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
            case LEADING:  
              xx += 4;
              break;
                
            case TRAILING:  
              xx += w - 4 - ww;
              break;
                
            case CENTER:  
              xx -= ww / 2;
              break;
          }
                    
        g.drawString(string, xx, yy);
      }
  }
