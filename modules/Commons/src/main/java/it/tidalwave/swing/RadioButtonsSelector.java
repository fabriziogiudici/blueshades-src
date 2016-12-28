/***********************************************************************************************************************
 *
 * blueShades - a Java UI for Argyll
 * Copyright (C) 2011-2016 by Tidalwave s.a.s. (http://www.tidalwave.it)
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
 * WWW: http://blueshades.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueshades-src
 *
 **********************************************************************************************************************/
package it.tidalwave.swing;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import it.tidalwave.blueshades.util.MutableProperty;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class RadioButtonsSelector
  {
    private final List<JRadioButton> radioButtons = new ArrayList<JRadioButton>();  
    
    private MutableProperty<Integer> selection;
    
    private final PropertyChangeListener selectionTracker = new PropertyChangeListener() 
      {
        @Override
        public void propertyChange (final @Nonnull PropertyChangeEvent event) 
          {
            radioButtons.get(selection.getValue()).setSelected(true);
          }
      };
    
    private final ActionListener actionListener = new ActionListener() 
      {
        @Override
        public void actionPerformed (final @Nonnull ActionEvent event) 
          {
            if (selection != null)
              { 
                selection.setValue(radioButtons.indexOf(event.getSource()));  
              }
          }
      };

    public RadioButtonsSelector (final @Nonnull JRadioButton ... radioButtons)
      {
        this.radioButtons.addAll(Arrays.asList(radioButtons));  
        
        for (final JRadioButton radioButton : radioButtons)
          {
            radioButton.addActionListener(actionListener);  
          }
      }  
    
    public void bind (final @Nonnull MutableProperty<Integer> selection)
      {
        this.selection = selection;
        this.selection.addPropertyChangeListener(selectionTracker);
        selectionTracker.propertyChange(null);
      }
    
    public void unbind()
      {
        this.selection.removePropertyChangeListener(selectionTracker);
        selection = null;
      }
  }
