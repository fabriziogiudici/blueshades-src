/***********************************************************************************************************************
 *
 * blueShades - a Java UI for Argyll
 * Copyright (C) 2011-2016 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
package it.tidalwave.colorimetry;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.netbeans.util.AsLookupSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ToString(callSuper=false) @EqualsAndHashCode(callSuper=false, of="displayName")
public class Display extends AsLookupSupport
  {
    @Nonnull @Getter
    private final String displayName;
    
    @Nonnegative
    private final int screenDeviceIndex; // FIXME: try to get rid of this, since it should be computed on demand
    
    public Display (final @Nonnull String displayName, final @Nonnegative int screenDeviceIndex)
      {
        super(new Object[] { new DefaultDisplayable(displayName, displayName) });  
        this.displayName = displayName;
        this.screenDeviceIndex = screenDeviceIndex;
      }    
    
    @Nonnull
    public GraphicsDevice getGraphicsDevice()
      {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return ge.getScreenDevices()[screenDeviceIndex];
      }
  }
