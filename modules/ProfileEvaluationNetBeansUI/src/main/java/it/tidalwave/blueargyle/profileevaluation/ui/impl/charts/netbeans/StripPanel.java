/***********************************************************************************************************************
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 **********************************************************************************************************************/
package it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.GridLayout;
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
        
        for (int shade = firstShade; shade != lastShade; shade += Math.signum(lastShade - firstShade))
          {
            final JPanel strip = new JPanel();
            strip.setOpaque(true);
            strip.setBackground(new Color(shade, shade, shade));
            add(strip);
          }
      }
  }
