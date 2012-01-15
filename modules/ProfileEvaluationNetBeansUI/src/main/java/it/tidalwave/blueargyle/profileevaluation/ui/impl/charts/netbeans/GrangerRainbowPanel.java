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

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.SwingWorker;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;
import it.tidalwave.image.EditableImage;
import it.tidalwave.blueargyle.profileevaluation.TestImageFactory;
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
    public GrangerRainbowPanel()
      {
        assert EventQueue.isDispatchThread();
      }
    
    @Override
    public void paint (final @Nonnull Graphics g) 
      {
        assert EventQueue.isDispatchThread();
        
        if (getBackgroundPainter() == null)
          {
            new SwingWorker<EditableImage, Void>() 
              {
                @Override @Nonnull
                protected EditableImage doInBackground() 
                  {
//                    return TestImageFactory.createGrangerRainbow(getWidth(), getHeight(), "MelissaRGB");
                    return TestImageFactory.createGrangerRainbow(getWidth(), getHeight(), "Adobe98");
                  }

                @Override
                protected void done()   
                  {
                    try 
                      {
                        final ImagePainter painter = new ImagePainter(get().getInnerProperty(BufferedImage.class));
                        painter.setScaleToFit(true);
                        setOpaque(false);
                        setBackgroundPainter(painter);
                        repaint();
                      }
                    catch (InterruptedException e) 
                      {
                        e.printStackTrace();
                      }
                    catch (ExecutionException e)
                      {
                        e.printStackTrace();
                      }
                  }
              }.execute();
          }
        
        super.paint(g);
      }
  }
