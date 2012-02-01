/***********************************************************************************************************************
 *
 * blueShades - a Java UI for Argyll
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
 * WWW: http://blueshades.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueshades-src
 *
 **********************************************************************************************************************/
package it.tidalwave.blueshades.profileevaluation.ui.impl.charts.netbeans;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutionException;
import java.awt.color.ICC_Profile;
import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.SwingWorker;
import it.tidalwave.image.EditableImage;
import it.tidalwave.image.op.ConvertColorProfileOp;
import it.tidalwave.image.render.EditableImageRenderer;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public abstract class DeferredCreationEditableImageRenderer extends EditableImageRenderer
  {
    private boolean imageLoaded = false;
    
    @Nonnull 
    private final ICC_Profile deviceProfile;
    
    /*******************************************************************************************************************
     *
     * .
     *
     ******************************************************************************************************************/
    public DeferredCreationEditableImageRenderer (final @Nonnull ICC_Profile deviceProfile)
      {
        assert EventQueue.isDispatchThread();
        this.deviceProfile = deviceProfile;
      }

    /*******************************************************************************************************************
     *
     * .
     *
     ******************************************************************************************************************/
    @Override
    public void paint (final @Nonnull Graphics g) 
      {
        assert EventQueue.isDispatchThread();
        
        if (!imageLoaded)
          {
            imageLoaded = true;
            
            new SwingWorker<EditableImage, Void>() 
              {
                @Override @Nonnull
                protected EditableImage doInBackground() 
                  {
                    return profileConverted(createEditableImage());
                  }

                @Override
                protected void done()   
                  {
                    try 
                      {
                        setFitToDisplaySize(true);
                        setImage(get());
                      }
                    catch (InterruptedException e) 
                      {
                        log.error("", e);
                      }
                    catch (ExecutionException e)
                      {
                        log.error("", e);
                      }
                  }
              }.execute();
          }
        
        super.paint(g);
      }
    
    /*******************************************************************************************************************
     *
     * .
     *
     ******************************************************************************************************************/
    @Nonnull
    protected abstract EditableImage createEditableImage();
    
    /*******************************************************************************************************************
     *
     * FIXME: profile conversion should be moved to EditableImageRenderer, but the problem is how to get the screen
     * profile.
     *
     ******************************************************************************************************************/
    @Nonnull
    private EditableImage profileConverted (final @Nonnull EditableImage image)
      {
//        final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        final GraphicsDevice device = environment.getScreenDevices()[0];
//        final GraphicsConfiguration configuration = device.getDefaultConfiguration();
//        final ICC_ColorSpace colorSpace = (ICC_ColorSpace)configuration.getColorModel().getColorSpace();
//
//          System.err.println("DEVICE PROFILE " + ImageUtils.getICCProfileName(colorSpace.getProfile()));
        return image.execute2(new ConvertColorProfileOp(deviceProfile));
////        return image.execute2(new ConvertColorProfileOp(loadProfile())).execute2(new AssignColorProfileOp(ICC_Profile.getInstance(ColorSpace.CS_sRGB)));
////        return image.execute2(new ConvertColorProfileOp(loadProfile())).execute2(new AssignColorProfileOp(colorSpace.getProfile()));
////        return image.execute2(new ConvertColorProfileOp(colorSpace.getProfile()));
      }
  }
