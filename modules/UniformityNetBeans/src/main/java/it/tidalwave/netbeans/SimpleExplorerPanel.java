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
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.DefaultEditorKit;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
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

    public SimpleExplorerPanel() 
      {
        final ActionMap actionMap = getActionMap();
        actionMap.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(explorerManager));
        actionMap.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(explorerManager));
        actionMap.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(explorerManager));
        actionMap.put("delete", ExplorerUtils.actionDelete(explorerManager, true));
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

