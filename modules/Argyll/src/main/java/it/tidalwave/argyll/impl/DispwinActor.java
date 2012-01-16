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
import javax.annotation.Nonnegative;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import it.tidalwave.util.spi.SimpleFinderSupport;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.argyll.*;
import it.tidalwave.blueargyle.util.Executor; 
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
    public void onDisplayDiscoveryQuery (final @ListensTo @Nonnull DisplayDiscoveryQueryMessage message)
      throws IOException, InterruptedException
      {
        log.trace("onDisplayDiscoveryQuery({})", message);

        final Executor executor = Executor.forExecutable("dispwin").withArgument("--");
        final List<ProfiledDisplay> displays = new ArrayList<ProfiledDisplay>();
        int screenDeviceIndex = 0;
        
        final List<FileObject> profileFiles = getProfileFiles();
        
        for (final String displayName : executor.start().waitForCompletion().getStderr().filteredBy("^ *[1-9] = '([^,]*),.*$"))
          {
            // FIXME: is it safe to assume that Argyll enumerates displays in the same order of Java ScreenDevices?
            final int index = screenDeviceIndex++;
            final Profile profile = getInstalledProfile(profileFiles, index);
            displays.add(new ProfiledDisplay(new Display(displayName, index), profile)); 
          }
        
        log.info(">>>> {}", displays);
        
        new DisplayDiscoveryMessage(new SimpleFinderSupport<ProfiledDisplay>() 
          {
            @Override
            protected List<? extends ProfiledDisplay> computeNeededResults() 
              {
                return displays;
              }  
              
          }).send();
      }
    
    @Nonnull
    private Profile getInstalledProfile (final @Nonnull List<FileObject> profileFiles, final @Nonnegative int index) 
      throws IOException, InterruptedException
      {
        for (final FileObject profileFile : profileFiles)
          {
            final Executor executor = Executor.forExecutable("dispwin")
                                              .withArgument("-d")
                                              .withArgument(Integer.toString(index + 1))
                                              .withArgument("-V")
                                              .withArgument(profileFile.getPath());
            if (!executor.start().waitForCompletion().getStdout().filteredBy("(.* IS loaded .*)").isEmpty())
              {
                return new Profile(profileFile.getName()); 
              }
          } 
        
        return new Profile("?");
      }
    
    @Nonnull
    private List<FileObject> getProfileFiles()
      {
        final FileObject root = FileUtil.toFileObject(new File("/Users/fritz/Library/ColorSync/Profiles")); // FIXME
        final List<FileObject> result = new ArrayList<FileObject>();
        
        for (final FileObject f : Collections.list(root.getChildren(true)))
          {
            if (f.hasExt("icc"))
              {
                result.add(f);  
              }
          }
        
        return result;
      }
  }
