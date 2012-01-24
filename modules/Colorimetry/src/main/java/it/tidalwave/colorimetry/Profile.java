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
package it.tidalwave.colorimetry;

import java.awt.color.ICC_Profile;
import java.io.IOException;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @ToString(exclude={"iccProfile"}) @EqualsAndHashCode(exclude={"iccProfile"}) @Slf4j
public class Profile 
  {
    @Getter
    private final String name;
    
    @CheckForNull
    private ICC_Profile iccProfile;
    
    @Nonnull
    public synchronized ICC_Profile getIccProfile()
      {
        if (iccProfile == null)
          {
            try 
              { 
                // FIXME: works for Mac OS X only
                final String resourceName = String.format("%s/Library/ColorSync/Profiles/%s.icc",
                                                          System.getProperty("user.home"), name);
                log.info("Loading profile from {} ...", resourceName);
    //            final String resourceName = "/Users/fritz/Settings/Argyll/Changeset 94d4e5c5ec8e/"
    //                    + "MBP 10.7.2 ColorLCD D65 140cdm² b-3 DarkRoom 2012-01-14 11.20/"
    //                    + "MBP 10.7.2 ColorLCD D65 140cdm² b-3 DarkRoom 2012-01-14 11.20 gm.icc";
    //            final String resourceName = "/Users/fritz/Settings/Argyll/"
    //                      + "MBP 10.7.2 PLB2403WS D65 140cdm² b82 c90 r95 g87 b93 DarkRoom 2012-01-07 13.18/"
    //                      + "MBP 10.7.2 PLB2403WS D65 140cdm² b82 c90 r95 g87 b93 DarkRoom 2012-01-07 13.18 cLUTxyz.icc";
                iccProfile = ICC_Profile.getInstance(resourceName);
              }
            catch (IOException e)
              {
                throw new IllegalArgumentException(e);
              }
          }
         
        return iccProfile;
      }
  }
