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
package it.tidalwave.colorimetry;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable
public abstract class ColorPoint
  {
    public static enum ColorSpace
      {
        XYZ, Lab
      }
    
    @Nonnull
    public static XYZColorPoint colorXYZ (final double x, final double y, final double z)
      {
        return new XYZColorPoint(x, y, z, ColorSpace.XYZ);
      }
    
    @Nonnull
    public static ColorPoint colorLab (final double l, final double a, final double b)
      {
        return new LabColorPoint(l, a, b, ColorSpace.Lab);
      }
    
    @Nonnull
    public abstract ColorSpace getColorSpace();
  }
