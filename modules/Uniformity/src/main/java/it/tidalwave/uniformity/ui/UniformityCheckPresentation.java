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
package it.tidalwave.uniformity.ui;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.swing.Action;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface UniformityCheckPresentation 
  {
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    @RequiredArgsConstructor(staticName="pos") @EqualsAndHashCode @ToString
    public static class Position
      {
        @Nonnegative
        public final int column;
        
        @Nonnegative
        public final int row;
      }

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void bind (@Nonnull Action continueAction, @Nonnull Action cancelAction);
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void setGridSize (@Nonnegative int rows, @Nonnegative int columns);
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderEmpty (@Nonnull Position position);

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderInvitation (@Nonnull Position position);

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderControlPanel (@Nonnull Position position);

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderWhite (@Nonnull Position position);

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderMeasurement (@Nonnull Position position,
                                   @Nonnull String luminance, 
                                   @Nonnull String whitePoint);
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void dismiss();
  }
