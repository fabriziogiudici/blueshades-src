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
import lombok.EqualsAndHashCode;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * @stereotype Presentation
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public interface UniformityTestPresentation 
  {
    @EqualsAndHashCode @ToString
    public static class Position
      {
        public int column;
        public int row;
      }
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void setGridSize (@Nonnegative int rows, @Nonnegative int columns);
    
    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderEmpty (@Nonnegative int row, @Nonnegative int column);

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderInvitation (@Nonnegative int row, @Nonnegative int column);

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderControlPanel (@Nonnegative int row, @Nonnegative int column);

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderWhite (@Nonnegative int row, @Nonnegative int column);

    /*******************************************************************************************************************
     * 
     *
     ******************************************************************************************************************/
    public void renderMeasurement (@Nonnegative int row, 
                                   @Nonnegative int column, 
                                   @Nonnull String luminance, 
                                   @Nonnull String whitePoint);
  }
