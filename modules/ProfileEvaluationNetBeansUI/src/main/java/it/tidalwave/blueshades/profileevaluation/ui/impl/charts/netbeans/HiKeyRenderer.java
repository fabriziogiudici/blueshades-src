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
import java.awt.Color;
import java.awt.EventQueue;
import it.tidalwave.netbeans.util.Locator;
import org.imajine.image.EditableImage;
import it.tidalwave.blueshades.profileevaluation.TestImageFactory;
import java.awt.color.ICC_Profile;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class HiKeyRenderer extends DeferredCreationEditableImageRenderer
  {
    private final String profileName;
    
    private final TestImageFactory testImageFactory = Locator.find(TestImageFactory.class);
    
    public HiKeyRenderer (final @Nonnull String profileName, final @Nonnull ICC_Profile deviceProfile)
      {
        super(deviceProfile);
        this.profileName = profileName;  
      }
    
    @Override @Nonnull
    protected EditableImage createEditableImage()
      {
        assert !EventQueue.isDispatchThread();
        return testImageFactory.createBandChart(getWidth(), getHeight(), profileName, Color.WHITE, 254, 239);
      }
  }
