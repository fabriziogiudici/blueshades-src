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
package it.tidalwave.uniformity.archive.impl;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.concurrent.NotThreadSafe;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.archive.UniformityArchiveContentMessage;
import it.tidalwave.uniformity.archive.UniformityArchiveQuery;
import it.tidalwave.uniformity.archive.UniformityArchiveUpdatedMessage;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import static it.tidalwave.role.Marshallable.Marshallable;
import static it.tidalwave.role.Unmarshallable.Unmarshallable;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Actor(threadSafe=false) @NotThreadSafe @Slf4j
public class UniformityArchiveActor
  {
    private FileObject persistenceFile;
    
    private UniformityArchive archive = new UniformityArchive();
    
    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    @PostConstruct
    public void initialize() // FIXME: private
      throws IOException
      {
        log.info("initialize()");  
        persistenceFile = FileUtil.createData(FileUtil.getConfigRoot(), "Archive/UniformityMeasurements.txt");
        log.info(">>>> persistenceFile: {}", persistenceFile.getPath());
        loadArchive();
      }
    
    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    public void onNewMeasurement (final @ListensTo @Nonnull UniformityMeasurementMessage message)
      throws IOException
      {
        log.info("onNewMeasurement({})", message);
        archive.add(message.getMeasurements());
        storeArchive();
        new UniformityArchiveUpdatedMessage(archive.findMeasurementsByDisplay(message.getMeasurements().getDisplay())).send();
      }
    
    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    public void onQuery (final @ListensTo @Nonnull UniformityArchiveQuery message)
      {
        log.info("onQuery({})", message);
        new UniformityArchiveContentMessage(archive.findMeasurementsByDisplay(message.getDisplay())).send();
      }
    
    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    private void loadArchive()
      throws IOException
      {
        log.info("loadArchive()");  
        archive.clear();
        
        final @Cleanup InputStream is = persistenceFile.getInputStream();
        archive = archive.as(Unmarshallable).unmarshal(is);
        is.close();
      }
    
    /*******************************************************************************************************************
     * 
     *
     * 
     ******************************************************************************************************************/
    private void storeArchive() 
      throws IOException
      {
        log.info("storeArchive()"); 
        
        final @Cleanup OutputStream os = persistenceFile.getOutputStream();
        archive.as(Marshallable).marshal(os);
        os.close();
      }
  }
