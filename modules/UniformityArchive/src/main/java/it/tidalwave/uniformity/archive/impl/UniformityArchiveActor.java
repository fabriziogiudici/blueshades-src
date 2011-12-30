/***********************************************************************************************************************
 *
 * blueArgyle - a Java UI for Argyll
 * Copyright (C) 2011-2011 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import it.tidalwave.actor.annotation.Actor;
import it.tidalwave.actor.annotation.ListensTo;
import it.tidalwave.uniformity.UniformityMeasurementMessage;
import it.tidalwave.uniformity.archive.UniformityArchiveContentMessage;
import it.tidalwave.uniformity.archive.UniformityArchiveQuery;
import it.tidalwave.uniformity.archive.UniformityArchiveUpdatedMessage;
import it.tidalwave.uniformity.archive.impl.io.UniformityArchiveMarshallable;
import it.tidalwave.uniformity.archive.impl.io.UniformityArchiveUnmarshallable;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Actor @NotThreadSafe @Slf4j
public class UniformityArchiveActor
  {
    private UniformityArchive archive = new UniformityArchive();
    
    public void storeUniformityMeasurement (final @ListensTo @Nonnull UniformityMeasurementMessage message)
      throws IOException
      {
        log.info("storeUniformityMeasurement({})", message);
        archive.add(message.getMeasurements());
        storeArchive();
        new UniformityArchiveUpdatedMessage(archive.findMeasurements()).send();
      }
    
    public void query (final @ListensTo @Nonnull UniformityArchiveQuery message)
      {
        log.info("query({})", message);
        new UniformityArchiveContentMessage(archive.findMeasurements()).send();
      }
    
    @PostConstruct
    public void loadArchive() // FIXME: private
      throws IOException
      {
        log.info("loadArchive()");  
        archive.clear();
        
        final @Cleanup InputStream is = new FileInputStream("/tmp/UniformityMeasurements.txt");
        // FIXME: uniformityArchive.as(Marshallable).unmarshal(is);
        archive = new UniformityArchiveUnmarshallable().unmarshal(is);
        is.close();
      }
    
    private void storeArchive() 
      throws IOException
      {
        log.info("storeArchive()"); 
        
        final @Cleanup OutputStream os = new FileOutputStream("/tmp/UniformityMeasurements.txt");
        // FIXME: uniformityArchive.as(Marshallable).marshal(os);
        new UniformityArchiveMarshallable(archive).marshal(os);
        os.close();
      }
  }
