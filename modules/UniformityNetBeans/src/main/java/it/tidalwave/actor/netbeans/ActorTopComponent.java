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
package it.tidalwave.actor.netbeans;

import javax.annotation.Nonnull;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import org.openide.windows.TopComponent;
import it.tidalwave.actor.spi.ActorGroupActivator;
import lombok.Getter;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class ActorTopComponent<T extends Component> extends TopComponent
  {
    private final ActorGroupActivator activator;
            
    @Getter
    private final T content;
    
    protected ActorTopComponent (final @Nonnull Class<? extends ActorGroupActivator> actorGroupActivatorClass,
                                 final @Nonnull Class<T> contentClass) 
      {
        assert EventQueue.isDispatchThread();
        activator = instantiate(actorGroupActivatorClass);
        content = instantiate(contentClass);
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
      }

    @Override
    public void componentOpened()
      {
        super.componentOpened();
        activator.activate();
      }

    @Override
    public void componentClosed() 
      {
        activator.deactivate();
        super.componentClosed();
      }
    
    @Nonnull
    private <X> X instantiate (final @Nonnull Class<X> clazz)
      {
        try
          {
            return clazz.newInstance();
          } 
        catch (InstantiationException e)
          {
            throw new RuntimeException(e);
          } 
        catch (IllegalAccessException e) 
          {
            throw new RuntimeException(e);
          }
      }
  }
