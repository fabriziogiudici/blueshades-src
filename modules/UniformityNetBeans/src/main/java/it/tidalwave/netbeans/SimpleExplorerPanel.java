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
package it.tidalwave.netbeans;

import javax.annotation.Nonnull;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.DefaultEditorKit;
import org.openide.nodes.Node;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import it.tidalwave.util.NotFoundException;
import it.tidalwave.netbeans.nodes.role.ActionProvider;
import lombok.Getter;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class SimpleExplorerPanel extends JPanel implements ExplorerManager.Provider 
  {
    @Getter
    private final ExplorerManager explorerManager = new ExplorerManager();
    
    private final PropertyChangeListener pcl = new PropertyChangeListener() 
      {
        @Override
        public void propertyChange (final @Nonnull PropertyChangeEvent event) 
          {
            if (ExplorerManager.PROP_SELECTED_NODES.equals(event.getPropertyName()))
              {
                final Node[] selectedNodes = (Node[])event.getNewValue();
                
                for (final Node selectedNode : selectedNodes)
                  {
                    final ActionProvider actionProvider = selectedNode.getLookup().lookup(ActionProvider.class);
                    
                    if (actionProvider != null)
                      {
                        try
                          {
                            // FIXME: introduce a new specific getSelectionAction()?
                            actionProvider.getPreferredAction().actionPerformed(null);// FIXME: null  
                          }
                        catch (NotFoundException e)
                          {
                            // ok, no selection action 
                          }
                      }
                  }
              }
          }
      };

    public SimpleExplorerPanel() 
      {
        final ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(explorerManager));
        actionMap.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(explorerManager));
        actionMap.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(explorerManager));
        actionMap.put("delete", ExplorerUtils.actionDelete(explorerManager, true));
        
        explorerManager.addPropertyChangeListener(pcl);
      }
    
    public SimpleExplorerPanel (final @Nonnull JComponent component)
      {
        this();
        setLayout(new BorderLayout());
        add(component, BorderLayout.CENTER);
      }  

    @Override
    public void addNotify() 
      {
        super.addNotify();
        ExplorerUtils.activateActions(explorerManager, true);
      }
    
    @Override
    public void removeNotify() 
      {
        ExplorerUtils.activateActions(explorerManager, false);
        super.removeNotify();
      }
  }

