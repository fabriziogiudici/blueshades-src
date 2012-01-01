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
package it.tidalwave.util.test.swing;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import it.tidalwave.util.test.swing.ActionTestHelper.Verifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 *
 * A facility class for helping with tests involving {@link Action}s.
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class ActionsTestHelper
  {
    @RequiredArgsConstructor
    public static class On<T> // syntactic sugar
      { 
        @Nonnull
        private final T object;
        
        @Nonnull
        public T on()
          {
            return object;   
          }
      }
    
    private final Map<String, ActionTestHelper> helperMapByName = new HashMap<String, ActionTestHelper>();
    
    private final List<ActionTestHelper> testHelperPool = new ArrayList<ActionTestHelper>();
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public ActionsTestHelper()
      {
        for (int i = 0; i < 100; i++)
          {
            testHelperPool.add(new ActionTestHelper());  
          }
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    private final Answer<Void> storeActionReferences = new Answer<Void>()
      {
        @Override
        public Void answer (final @Nonnull InvocationOnMock invocation)
          throws Throwable 
          {
            for (int i = 0; i < invocation.getArguments().length; i++)
              {
                final Action originalAction = (Action)invocation.getArguments()[i];
                final ActionTestHelper testHelper = testHelperPool.get(i);
                final Action decorator = testHelper.attach(originalAction);
                helperMapByName.put((String)originalAction.getValue(Action.NAME), testHelper);
            // FIXME: below is tricky because we also call some Mockito stuff not part of the public API
//            invocation.getMethod().invoke(invocation.getMock(), continueActionDecorator, cancelActionDecorator);
                ((Invocation)invocation).getRawArguments()[i] = decorator;
              }
            
            try
              {
                invocation.callRealMethod();
              }
            catch (Exception e) // when the original object is a mock, this throws an exception
              {
                // FIXME:
              }
            
            return null;
          }
      };
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void dispose()
      {  
        for (final ActionTestHelper actionTestHelper : helperMapByName.values())
          {
            actionTestHelper.dispose();  
          }
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public <T> On<T> register (final @Nonnull T presentation)
      {
        return new On<T>(doAnswer(storeActionReferences).when(presentation)); 
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public List<Object> getVerifiers()
      {
        final List<Object> result = new ArrayList<Object>();
        
        for (final ActionTestHelper actionTestHelper : testHelperPool)
          {
            result.add(actionTestHelper.getVerifier());
          }
        
        return result;
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public Verifier getVerifierFor (final @Nonnull String actionName)
      {
        return findHelperByName(actionName).getVerifier();  
      }    
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public Answer<Void> performActionWithDelay (final @Nonnull String actionName, final @Nonnegative long delay)
      {
        return new Answer<Void>() 
          {
            @Override
            public Void answer (final @Nonnull InvocationOnMock invocation) 
              throws Throwable   
              {
                return findHelperByName(actionName).performActionWithDelay(delay).answer(invocation);
              }
          };
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    private ActionTestHelper findHelperByName (final @Nonnull String actionName)
      {
        final ActionTestHelper actionTestHelper = helperMapByName.get(actionName);
        
        if (actionTestHelper == null)
          {
            throw new IllegalArgumentException("Can't find action: " + actionName + " - available ones are: " + helperMapByName.keySet());    
          }
        
        return actionTestHelper;
      }
  }
