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
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.ImagePainter;
import it.tidalwave.image.EditableImage;
import it.tidalwave.blueargyle.profileevaluation.TestImageFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @Slf4j
public class GrangerRainbowPanel extends DeferredCreationPainterPanel
  {
    private final String profileName;
    
    @Override @Nonnull
    protected Painter createPainter()
      {
        assert !EventQueue.isDispatchThread();
        final EditableImage image = TestImageFactory.createGrangerRainbow(getWidth(), getHeight(), profileName);
        final ImagePainter painter = new ImagePainter(image.getInnerProperty(BufferedImage.class));
        painter.setScaleToFit(true);
        return painter;
      }
  }
