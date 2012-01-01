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
package it.tidalwave.uniformity.ui.impl.main.netbeans;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class MeasurementPanel extends JPanel 
  {    
    private final JLabel lbLower = new JLabel();
    private final JLabel lbUpper = new JLabel();
    
    private final ComponentListener componentListener = new ComponentAdapter() 
      {
        @Override
        public void componentResized (final @Nonnull ComponentEvent event) 
          {
            lbUpper.setFont(getFont().deriveFont(getWidth() / 12.0f));
            lbLower.setFont(getFont().deriveFont(getWidth() / 16.0f));
          }
      };
    
    public MeasurementPanel (final @Nonnull String text)
      {
        assert EventQueue.isDispatchThread();
        setBackground(Color.WHITE);
        setOpaque(true);
        lbUpper.setHorizontalAlignment(SwingConstants.CENTER);
        lbLower.setHorizontalAlignment(SwingConstants.CENTER);
        lbUpper.setVerticalAlignment(SwingConstants.BOTTOM);
        lbLower.setVerticalAlignment(SwingConstants.TOP);
        addComponentListener(componentListener);
        final String[] split = text.split("\n");
        final GridLayout gridLayout = new GridLayout(split.length, 1);
        setLayout(gridLayout);
        lbUpper.setText(split[0]);
        add(lbUpper);
        
        if (split.length > 1)
          {
            add(lbLower);
            lbLower.setText(split[1]);
          }
        else
          {
            lbUpper.setVerticalAlignment(SwingConstants.CENTER);
          }
      }

    @Override
    public void addNotify() 
      {
        super.addNotify();
        componentListener.componentResized(null);
      }
  }
