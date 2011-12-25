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
package it.tidalwave.uniformity.ui.impl.swing;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;
import java.awt.EventQueue;
import org.openide.util.lookup.ServiceProvider;
import it.tidalwave.uniformity.ui.UniformityCheckPresentation;
import it.tidalwave.uniformity.ui.spi.UniformityCheckPresentationBuilder;
import lombok.extern.slf4j.Slf4j;

/***********************************************************************************************************************
 * 
 * @stereotype Factory
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@ServiceProvider(service=UniformityCheckPresentationBuilder.class) @Slf4j
public class UniformityCheckPresentationPanelBuilder implements UniformityCheckPresentationBuilder
  {
    private WeakReference<UniformityCheckPresentationPanel> presentationRef = new WeakReference<UniformityCheckPresentationPanel>(null);
    
    @Override @Nonnull
    public UniformityCheckPresentation buildUI()
      {
        log.info("buildUI()");
        UniformityCheckPresentationPanel presentation = presentationRef.get();
        
        if (presentation == null)
          {
            presentation = buildUI2();
            presentationRef = new WeakReference<UniformityCheckPresentationPanel>(presentation);
          }
        
        return presentation;
      }
              
    @Nonnull
    private UniformityCheckPresentationPanel buildUI2()
      {
        final AtomicReference<UniformityCheckPresentationPanel> reference = new AtomicReference<UniformityCheckPresentationPanel>();
        
        try  
          {
            EventQueue.invokeAndWait(new Runnable() 
              {
                @Override
                public void run() 
                  {
                    reference.set(new UniformityCheckPresentationPanel());
                  }
              });
          } 
        catch (InterruptedException e)
          {
            log.warn("", e);
          }
        catch (InvocationTargetException e) 
          {
            log.warn("", e);
          }
        
        return reference.get();
      }
  }
