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
 * WWW: http://blueargyle.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueargyle-src
 *
 **********************************************************************************************************************/
package it.tidalwave.blueargyle.profileevaluation.ui.impl.charts.netbeans;

import javax.annotation.Nonnull;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import javax.swing.JFrame;
import it.tidalwave.swing.SwingSafeRunner;
import org.testng.annotations.Test;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public abstract class PanelTestSupport
  {
    @Test
    public void run() 
      throws InterruptedException
      {
        SwingSafeRunner.runSafely(new Runnable() 
          {
            @Override
            public void run() 
              {
                final JFrame frame = new JFrame();
                final DeferredCreationEditableImageRenderer presentation = createPresentation(ICC_Profile.getInstance(ColorSpace.CS_sRGB));
//                presentation.setDeviceProfileName("MBP 10.7.2 ColorLCD D65 140cdm² b-3 DarkRoom 2012-01-14 11.20 gm.icc"); // FIXME
                frame.add(presentation);
                frame.setSize(1024, 768);
                frame.setVisible(true);
              }
          });
        Thread.sleep(10000);
      }

    @Nonnull
    protected abstract DeferredCreationEditableImageRenderer createPresentation (@Nonnull ICC_Profile deviceProfile);
  }
