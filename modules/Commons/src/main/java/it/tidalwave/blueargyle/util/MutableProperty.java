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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(exclude="pcs") @ToString(exclude="pcs")
public class MutableProperty<T>
  {
    private transient final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public static final String PROP_VALUE = "value";

    @Getter
    private T value;

    public void setValue (final T value) 
      {
        final T oldValue = this.value;
        this.value = value;
        pcs.firePropertyChange(PROP_VALUE, oldValue, value);
      }

    public void addPropertyChangeListener (final @Nonnull PropertyChangeListener listener) 
      {
        pcs.addPropertyChangeListener(listener);
      }

    public void removePropertyChangeListener (final @Nonnull PropertyChangeListener listener)
      {
        pcs.removePropertyChangeListener(listener);
      }
  }
