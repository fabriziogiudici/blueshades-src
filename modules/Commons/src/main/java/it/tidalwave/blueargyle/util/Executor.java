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
package it.tidalwave.blueargyle.util;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import lombok.AccessLevel;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ThreadSafe @NoArgsConstructor(access=AccessLevel.PRIVATE) @Slf4j
public class Executor 
  {
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor(access=AccessLevel.PACKAGE)
    public class ConsoleOutput
      {
        @Nonnull
        private final InputStream input;
        
        @Getter
        private final List<String> content = Collections.synchronizedList(new ArrayList<String>());
        
        /***************************************************************************************************************
         * 
         *
         ***************************************************************************************************************/
        @Nonnull
        public ConsoleOutput start()
          {
            Executors.newSingleThreadExecutor().submit(runnable); 
            return this;
          }
        
        /***************************************************************************************************************
         * 
         *
         ***************************************************************************************************************/
        private final Runnable runnable = new Runnable() 
          {
            @Override
            public void run() 
              {
                try
                  {
                    read();  
                  }
                catch (IOException e)
                  {
                    log.warn("while reading from process console", e); 
                  }
              }
          };
        
        /***************************************************************************************************************
         * 
         *
         ***************************************************************************************************************/
        @Nonnull
        public Scanner filteredAndSplitBy (final @Nonnull String filterRegexp, final @Nonnull String delimiterRegexp)
          {
            final String string = filteredBy(filterRegexp).get(0);
            return new Scanner(string).useDelimiter(Pattern.compile(delimiterRegexp));
          }
        
        /***************************************************************************************************************
         * 
         *
         ***************************************************************************************************************/
        @Nonnull
        public List<String> filteredBy (final @Nonnull String filter)
          {
            final Pattern p = Pattern.compile(filter);
            final List<String> result = new ArrayList<String>();

            for (final String s : new ArrayList<String>(content))
              {
                final Matcher m = p.matcher(s);
                
                if (m.matches())
                  {
                    result.add(m.group(1));  
                  }
              }
            
            return result;
          }
        
        /***************************************************************************************************************
         * 
         *
         ***************************************************************************************************************/
        @Nonnull
        public ConsoleOutput waitFor (final @Nonnull String regexp)
          throws InterruptedException, IOException
          {
            log.info("waitFor({})", regexp);
            
            while (filteredBy(regexp).isEmpty())
              {
                try
                  {
                    final int exitValue = process.exitValue();
                    throw new IOException("Process exited with " + exitValue);  
                  }
                catch (IllegalThreadStateException e) // ok, process not terminated yet
                  {
                    synchronized (this)
                      {
                        wait(50); // FIXME: polls because it doesn't get notified
                      }
                  }
              }
            
            return this;
          } 
        
        /***************************************************************************************************************
         * 
         *
         ***************************************************************************************************************/
        public void clear()
          {
            content.clear();  
          }
    
        /***************************************************************************************************************
         * 
         *
         ***************************************************************************************************************/
        private void read()
          throws IOException
          {
            final @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(input));

            for (;;)
              {
                final String s = br.readLine();

                if (s == null)
                  { 
                    break;   
                  }  

                log.info(">>>>>>>> {}", s);
                content.add(s);  
                
                synchronized (this)
                  {
                    notifyAll();  
                  }
              }

            br.close();
          }
      }
    
    private final List<String> arguments = new ArrayList<String>();

    private Process process;
    
    @Getter
    private ConsoleOutput stdout;
    
    @Getter
    private ConsoleOutput stderr;
    
    private PrintWriter stdin;
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public static Executor forExecutable (final @Nonnull String executable)
      {
        final String path = System.getenv("ARGYLL_HOME");
        final Executor executor = new Executor();
        executor.arguments.add(path + File.separator + "bin" + File.separator + executable);
        return executor;
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public Executor withArgument (final @Nonnull String argument)
      {
        arguments.add(argument);
        return this;
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public Executor start()
      throws IOException
      {
        log.info(">>>> executing {} ...", arguments);
        
        final List<String> environment = new ArrayList<String>();
        
//        for (final Entry<String, String> e : System.getenv().entrySet())
//          {
//            environment.add(String.format("%s=%s", e.getKey(), e.getValue()));  
//          }
        
        environment.add("ARGYLL_NOT_INTERACTIVE=true");
        log.info(">>>> environment: {}", environment);
        process = Runtime.getRuntime().exec(arguments.toArray(new String[0]),
                                            environment.toArray(new String[0]));
        
        stdout = new ConsoleOutput(process.getInputStream()).start();
        stderr = new ConsoleOutput(process.getErrorStream()).start();
        stdin  = new PrintWriter(process.getOutputStream(), true);
        
        return this;
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public Executor waitForCompletion()
      throws IOException, InterruptedException
      {
        if (process.waitFor() != 0)
          {
//            throw new IOException("Process exited with " + process.exitValue());  
          }
        
        return this;
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @Nonnull
    public Executor send (final @Nonnull String string) 
      throws IOException
      {
        log.info(">>>> sending '{}'...", string);
        stdin.println(string);
        return this;
      }
  }

