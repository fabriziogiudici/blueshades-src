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
package it.tidalwave.uniformity.ui.main;

import javax.annotation.Nonnull;
import javax.swing.Action;
import it.tidalwave.role.ui.PresentationModel;
import it.tidalwave.blueargyle.util.MutableProperty;

/***********************************************************************************************************************
 * 
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface UniformityCheckMainPresentation
  {
    public void showUp();
    
    public void dismiss();

    public void bind (@Nonnull Action startAction, @Nonnull MutableProperty<Integer> selectedMeasurement);
    
    public void renderMeasurements (@Nonnull String[][] measurements);

    public void populateDisplays (@Nonnull PresentationModel presentationModel);

    public void populateMeasurementsArchive (@Nonnull PresentationModel presentationModel);
  }
