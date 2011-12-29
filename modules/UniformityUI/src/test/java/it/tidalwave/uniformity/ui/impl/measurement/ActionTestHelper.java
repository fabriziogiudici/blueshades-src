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
package it.tidalwave.uniformity.ui.impl.measurement;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import lombok.Delegate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/***********************************************************************************************************************
 *
 * A facility class for helping with tests involving {@link Action}s.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor
class ActionProxy implements Action
  {
    @Nonnull @Delegate(types=Action.class)
    protected final Action delegate;
  }

@Slf4j
public class ActionTestHelper
  {
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public static interface Verifier
      {
        public void setEnabled (boolean enabled);
        
        public void actionPerformed (@Nonnull ActionEvent event);
      }
    
    private Action action;
    
    @Getter
    private Verifier verifier;
    
    /*******************************************************************************************************************
     * 
     * Enablement can't be captured with the Action proxy since it's originated from a controller which directly calls
     * the original Action instance.
     *
     ******************************************************************************************************************/
    private final PropertyChangeListener actionPropertyTracker = new PropertyChangeListener() 
      {
        @Override
        public void propertyChange (final @Nonnull PropertyChangeEvent event) 
          {
            if ("enabled".equals(event.getPropertyName()))
              {
                final Boolean enabled = (Boolean)event.getNewValue();
                log.info("action \"{}\".setEnabled({})", action.getValue(Action.NAME), enabled);
                verifier.setEnabled(enabled);   
              }
          }
      };
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public ActionTestHelper()
      {
        verifier = mock(Verifier.class);
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public Action attach (final @Nonnull Action action)
      {
        action.addPropertyChangeListener(actionPropertyTracker);
        
        return this.action = new ActionProxy(action)
          {
            @Override
            public void actionPerformed (final @Nonnull ActionEvent event) 
              {
                log.info("Performing action on \"{}\" ...", delegate.getValue(Action.NAME));
                verifier.actionPerformed(event);
                delegate.actionPerformed(event);
              }
          };
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void dispose()
      {
        action.removePropertyChangeListener(actionPropertyTracker);  
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public Answer<Void> performActionWithDelay (final @Nonnegative long delay)
      {
        return new Answer<Void>()
          {
            @Override
            public Void answer (final @Nonnull InvocationOnMock invocation) 
              {
                new Timer().schedule(new TimerTask() 
                  {
                    @Override
                    public void run() 
                      {
                        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "action"));
                      }
                  }, delay);

                return null;
              }
          };
      }
  }
