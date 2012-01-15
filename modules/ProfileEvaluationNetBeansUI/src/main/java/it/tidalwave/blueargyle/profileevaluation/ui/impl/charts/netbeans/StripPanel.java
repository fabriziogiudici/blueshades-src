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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
public class StripPanel extends JPanel 
  {
    public StripPanel (final @Nonnull Color background, final @Nonnegative int firstShade, final @Nonnegative int lastShade)
      {
        setOpaque(true);
        setBackground(background);
        setLayout(new GridLayout(1, Math.abs(lastShade - firstShade + 1) * 2));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setMaximumSize(new Dimension(600, 200));
        
        final int direction = (int)Math.signum(lastShade - firstShade);
        
        for (int shade = firstShade; shade != lastShade + direction; shade += direction)
          {
            final JPanel filler = new JPanel();
            filler.setOpaque(true);
            filler.setBackground(background);
            add(filler);
            
            final JPanel strip = new JPanel();
            strip.setOpaque(true);
            strip.setBackground(new Color(shade, shade, shade));
            add(strip);
            final JLabel label = new JLabel(toString(shade));
            label.setForeground(inverse(background));
            strip.add(label);
          }
      }

    @Nonnull
    private static String toString (final int shade) 
      {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("<html>");
        String separator = "";
        
        for (final char c : ("" + shade).toCharArray())
          {
            buffer.append(separator).append(c); 
            separator ="<br>";
          }
        
        buffer.append("</html>");
        
        return buffer.toString();
      }
    
    @Nonnull
    private static Color inverse (final @Nonnull Color c)
      {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());  
      }
  }
