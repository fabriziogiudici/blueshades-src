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
package it.tidalwave.actor;

import javax.annotation.Nonnull;
import java.util.TimerTask;
import java.util.Timer;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class RepeatingMessageSender
  {
    @Nonnull
    private final MessageSupport message;
    
    @Nonnull
    private final Class<? extends MessageSupport> messageClass;
    
    private final Timer timer = new Timer();
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private final TimerTask messageSender = new TimerTask()
      {
        @Override
        public void run() 
          {
            try 
              {
                getMessage().send();
              }
            catch (InstantiationException e) 
              {
                log.error("", e);
              }
            catch (IllegalAccessException e) 
              {
                log.error("", e);
              }
          }              
      };
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public RepeatingMessageSender (final @Nonnull Class<? extends MessageSupport> messageClass) 
      {
        this.messageClass = messageClass;
        this.message = null;
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public RepeatingMessageSender (final @Nonnull MessageSupport message) 
      {
        this.messageClass = null;
        this.message = message;
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void start()
      {
        timer.scheduleAtFixedRate(messageSender, 0, 500);
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void stop() 
      {
        timer.cancel();
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    private MessageSupport getMessage()
      throws InstantiationException, IllegalAccessException
      {
        return (message != null) ? message : messageClass.newInstance();
      }
  }
