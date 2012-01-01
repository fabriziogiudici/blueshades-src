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
package it.tidalwave.actor.test;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import it.tidalwave.netbeans.util.Locator;
import it.tidalwave.messagebus.MessageBus.Listener;
import it.tidalwave.actor.Collaboration;
import it.tidalwave.actor.CollaborationCompletedMessage;
import it.tidalwave.actor.CollaborationStartedMessage;
import it.tidalwave.actor.MessageSupport;
import it.tidalwave.actor.spi.CollaborationAwareMessageBus;
import it.tidalwave.messagebus.MessageBus;
import java.lang.reflect.Modifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/***********************************************************************************************************************
 * 
 * A facility that records all the message flowing through the {@link MessageBus} so they can be verified in tests.
 * Note that private messages, as well as their relative {@link CollaborationStartedMessage} and
 * {@link CollaborationCompletedMessage}, are ignored.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class MessageVerifier 
  {
    @RequiredArgsConstructor
    public static class With
      {
        @Nonnull
        private final Object message;
        
        @Nonnull
        public <T> With with (final @Nonnull String fieldName, final @Nonnull Matcher<T> matcher)
          {
            try
              { 
                final Field field = message.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                final T value = (T)field.get(message);
                assertThat(value, matcher);
                return this;  
              }
            catch (Exception e)
              {
                throw new RuntimeException(e);
              }
          }
      }
    
    private final Provider<CollaborationAwareMessageBus> messageBus = Locator.createProviderFor(CollaborationAwareMessageBus.class);

    private final Listener<Object> listener = new Listener<Object>()
      {
        @Override
        public void notify (final @Nonnull Object message) 
          {
            if (isPrivate(message))
              {
                return;  
              }
            
            if ((message instanceof CollaborationStartedMessage) 
                && isPrivate(((CollaborationStartedMessage)message).getCollaboration().getOriginatingMessage()))
              {
                return;  
              }
            
            if ((message instanceof CollaborationCompletedMessage) 
                && isPrivate(((CollaborationCompletedMessage)message).getCollaboration().getOriginatingMessage()))
              {
                return;  
              }
            
            messages.add(message);
          }
        
        private boolean isPrivate (final @Nonnull Object message)
          {
            return Modifier.isPrivate(message.getClass().getModifiers());
          } 
      };
    
    private final List<Object> messages = new ArrayList<Object>();
    
    private Collaboration collaboration;
    
    private Iterator<Object> iterator;
    
    public void initialize()
      {
        messageBus.get().subscribe(Object.class, listener);  
      }
    
    public void dispose()
      {
        messageBus.get().unsubscribe(listener);  
        messages.clear();
        collaboration = null;
      }

    public void verifyCollaborationStarted()
      {
        final ArrayList<Object> messagesCopy = new ArrayList<Object>(messages);
        
        for (final Object message : messagesCopy)
          {  
            log.info("RECEIVED {}", message);  
          }
        
        iterator = messagesCopy.iterator();
        assertThat("Missing CollaborationStartedMessage", iterator.hasNext(), is(true));
        final Object message = iterator.next();
        assertThat("Missing CollaborationStartedMessage", message, instanceOf(CollaborationStartedMessage.class));
        collaboration = ((MessageSupport)message).getCollaboration();
      }
    
    public void verifyCollaborationCompleted()
      {
        assertThat("Missing CollaborationCompletedMessage", iterator.hasNext(), is(true));
        final Object message = iterator.next();
        assertThat("Missing CollaborationCompletedMessage", message, instanceOf(CollaborationCompletedMessage.class));
        assertThat("Collaboration mismatch", collaboration, sameInstance(((MessageSupport)message).getCollaboration()));
      }
    
    @Nonnull
    public With verify (final @Nonnull Class<?> messageClass)
      {
        assertThat("Missing message" + messageClass.getName(), iterator.hasNext(), is(true));
        final Object message = iterator.next();
        assertThat("Unexpected message class", message, instanceOf(messageClass));
        assertThat("Collaboration mismatch", collaboration, sameInstance(((MessageSupport)message).getCollaboration()));
        
        return new With(message);
      } 
  }
