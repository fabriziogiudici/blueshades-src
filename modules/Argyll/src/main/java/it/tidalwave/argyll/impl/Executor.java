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
package it.tidalwave.argyll.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable @RequiredArgsConstructor(access=AccessLevel.PRIVATE) @Slf4j
public class Executor 
  {
    private final static String argyllPath = "/Users/fritz/Applications/Argyll_V1.3.5/bin";
    
    private final List<String> arguments;

    private final String filter;
    
    @Getter
    private final List<String> filteredOutput;
    
    @Nonnull
    public static Executor forExecutable (final @Nonnull String executable)
      {
        return new Executor(Arrays.asList(argyllPath + File.separator + executable), "", Collections.<String>emptyList());
      }
    
    @Nonnull
    public Executor withArgument (final @Nonnull String argument)
      {
        final List<String> tmp = new ArrayList<String>(arguments);
        tmp.add(argument);
        return new Executor(tmp, filter, Collections.<String>emptyList());
      }
    
    @Nonnull
    public Executor withFilter (final @Nonnull String filter)
      {
        return new Executor(arguments, filter, Collections.<String>emptyList());
      }
    
    @Nonnull
    public Executor execute()
      throws IOException, InterruptedException
      {
        log.debug(">>>> executing {} ...", arguments);
        final Process process = Runtime.getRuntime().exec(arguments.toArray(new String[0]));
        process.waitFor();

        final Pattern p = Pattern.compile(filter);
        final List<String> output = new ArrayList<String>();
        final @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        
        for (;;)
          {
            final String s = br.readLine();
            
            if (s == null)
              { 
                break;   
              }  
            
            log.debug(">>>>>>>> {}", s);
            
            final Matcher m = p.matcher(s);
            
            if (m.matches())
              {
                output.add(m.group(1));  
              }
          }
        
        br.close();
        return new Executor(arguments, filter, output);
      }
  }
