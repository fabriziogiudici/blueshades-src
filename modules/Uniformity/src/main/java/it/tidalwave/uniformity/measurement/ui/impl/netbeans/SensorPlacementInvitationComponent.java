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

import javax.annotation.Nonnull;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import static java.awt.RenderingHints.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class SensorPlacementInvitationComponent extends JComponent
  {
    public SensorPlacementInvitationComponent()
      {
        setBackground(UniformityCheckMeasurementPresentationPanel.DEFAULT_BACKGROUND);
        setOpaque(true);
      }
    
    @Override
    public void paint (final @Nonnull Graphics graphics)
      {
        final int w = getWidth();
        final int h = getHeight();
        final Graphics2D g2 = (Graphics2D)graphics;
        g2.setColor(getBackground());
        g2.fillRect(0, 0, w, h);
        g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        final int cx = w / 2;
        final int cy = h / 2;
        final int r = Math.min(w, h);
        final int er = (int)(r * 0.8) / 2;
        final int cr = (int)(r * 0.6) / 2;
        
        g2.setColor(Color.WHITE);
        g2.fillOval(cx - cr, cy - cr, cr * 2, cr * 2);
        g2.setStroke(new BasicStroke(32));
        g2.drawOval(cx - cr, cy - cr, cr * 2, cr * 2);
        g2.drawLine(cx, cy - er, cx, cy + er);
        g2.drawLine(cx - er, cy, cx + er, cy);
        
        g2.setStroke(new BasicStroke(16));
        g2.setColor(Color.ORANGE);
        g2.drawOval(cx - cr, cy - cr, cr * 2, cr * 2);
        g2.drawLine(cx, cy - er, cx, cy - cr);
        g2.drawLine(cx, cy + er, cx, cy + cr);
        g2.drawLine(cx - er, cy, cx - cr, cy);
        g2.drawLine(cx + er, cy, cx + cr, cy);

      }
  }
