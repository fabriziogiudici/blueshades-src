/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 **********************************************************************************************************************/

package it.tidalwave.blueargyle.profileevaluation.ui;

import it.tidalwave.image.EditableImage;
import it.tidalwave.image.op.AssignColorProfileOp;
import it.tidalwave.image.op.CreateOp;
import it.tidalwave.image.op.WriteOp;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.swing.SwingConstants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor @Slf4j
public final class TestImageFactory 
  {
    // See http://www.openphotographyforums.com/forums/showthread.php?t=12336
    
    @Nonnull
    public static EditableImage createGrangerRainbow (final @Nonnegative int width, final @Nonnegative int height)
      {
        log.info("createGrangerRainbow({}, {})", width, height);
        final EditableImage image = EditableImage.create(new CreateOp(width, height, EditableImage.DataType.BYTE, Color.WHITE));
        final BufferedImage bImage = image.getInnerProperty(BufferedImage.class);
        final Graphics g = bImage.createGraphics();
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
                
                bImage.setRGB(x + margin, y + margin, Color.HSBtoRGB(hue, saturation, brightness));
              }
          }
        
        // FIXME: it seems that it doesn't work
        image.execute(new AssignColorProfileOp(ICC_Profile.getInstance(ColorSpace.CS_sRGB)));
        
//        image.execute(new WriteOp("JPG", "/tmp/grangersynth.jpg"));
        
        return image;
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
