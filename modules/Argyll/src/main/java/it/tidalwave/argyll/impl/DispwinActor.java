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
package it.tidalwave.argyll.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.blueargyle.util.Executor; 
import it.tidalwave.argyll.DisplayDiscoveryMessage;
import it.tidalwave.argyll.DisplayDiscoveryQueryMessage;
import it.tidalwave.argyll.Display;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * This actor wraps the functions provided by the {@code dispwin} executable.
 * 
 * @stereotype Actor
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Actor(threadSafe=false) @NotThreadSafe @Slf4j
public class DispwinActor 
  {
    /*******************************************************************************************************************
     * 
     * Answers to the query for the existing displays.
     * 
     ******************************************************************************************************************/
    public void discoverDisplays (final @ListensTo @Nonnull DisplayDiscoveryQueryMessage message)
      throws IOException, InterruptedException
      {
        log.trace("discoverDisplays({})", message);

        final Executor executor = Executor.forExecutable("dispwin").withArgument("--");
        final List<Display> displays = new ArrayList<Display>();
        int screenDeviceIndex = 0;
        
        for (final String displayName : executor.start().waitForCompletion().getStderr().filteredBy("^ *[1-9] = '([^,]*),.*$"))
          {
            // FIXME: is it safe to assume that Argyll enumerates displays in the same order of Java ScreenDevices?
            displays.add(new Display(displayName, screenDeviceIndex++)); 
          }
        
        new DisplayDiscoveryMessage(new SimpleFinderSupport<Display>() 
          {
            @Override
            protected List<? extends Display> computeNeededResults() 
              {
                return displays;
              }  
              
          }).send();
      }
  }
