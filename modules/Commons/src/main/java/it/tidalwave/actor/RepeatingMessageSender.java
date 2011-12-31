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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.TimerTask;
import java.util.Timer;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class RepeatingMessageSender
  {
    @CheckForNull
    private MessageSupport message;
    
    @CheckForNull
    private Timer timer;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public synchronized void start (final @Nonnull MessageSupport message)
      {
        this.message = message;
        
        if (timer == null)
          {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask()
              {
                @Override
                public void run() 
                  {
                    message.send();
                  }              
              }, 0, 500);
          }
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public synchronized void stop() 
      {
        if (timer != null)
          {
            timer.cancel();
            timer = null;
            message = null;
          }
      }
  }
