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
 * WWW: http://blueargyle.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueargyle-src
 *
 **********************************************************************************************************************/
package it.tidalwave.netbeans;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultEditorKit;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;
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
public class SimpleExplorerPanel extends JXPanel implements ExplorerManager.Provider 
  {
    @Getter
    private final ExplorerManager explorerManager = new ExplorerManager();
    
    @Getter
    private boolean busy;
    
    private final JXBusyLabel busyLabel = new JXBusyLabel();

    private final JXPanel shadingPanel = new JXPanel(new BorderLayout());
            
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
        assert EventQueue.isDispatchThread();
        setLayout(new OverlayLayout(this));
        busyLabel.setText("Loading...");
        busyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        final MattePainter painter = new MattePainter(new Color(255, 255, 255, 128));
//        painter.setFilters(new GaussianBlurFilter(10));
        shadingPanel.setVisible(false);
        shadingPanel.setOpaque(false);
        shadingPanel.setBackgroundPainter(painter);
        shadingPanel.add(busyLabel, BorderLayout.CENTER);
        add(shadingPanel);
        
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
        add(component);
      }  

    @Override
    public void addNotify() 
      {
        assert EventQueue.isDispatchThread();
        super.addNotify();
        ExplorerUtils.activateActions(explorerManager, true);
      }
    
    @Override
    public void removeNotify() 
      {
        assert EventQueue.isDispatchThread();
        ExplorerUtils.activateActions(explorerManager, false);
        super.removeNotify();
      }
    
    public void setBusy (final boolean busy)
      {
        assert EventQueue.isDispatchThread();
          // FIXME: it doesn't block interactions
        this.busy = busy;
        busyLabel.setBusy(busy);
        shadingPanel.setVisible(busy);
      }
  }

