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
package it.tidalwave.swing;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nonnull;
import java.awt.EventQueue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j @NoArgsConstructor(access=AccessLevel.PRIVATE)
public final class SwingSafeRunner 
  {
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public static void runSafely (final @Nonnull Runnable runnable)
      {
        final Runnable guardedRunnable = new Runnable() 
          {
            @Override
            public void run() 
              {
                try
                  {
                    runnable.run();
                  }
                catch (Throwable t)
                  {
                        t.printStackTrace();
                    log.warn("", t);
                  }
              }
          };

        if (EventQueue.isDispatchThread())
          {
            guardedRunnable.run();
          }
        else
          {
            try 
              {
                EventQueue.invokeAndWait(guardedRunnable);
              } 
            catch (InterruptedException e) 
              {
                log.warn("", e);
              } 
            catch (InvocationTargetException e) 
              {
                log.warn("", e);
              }
          }
      }
  }
