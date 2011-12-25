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
package it.tidalwave.colorimetry;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static lombok.AccessLevel.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable @RequiredArgsConstructor(access=PRIVATE) @Getter @EqualsAndHashCode
public class ColorPoint
  {
    public static enum ColorSpace
      {
        XYZ
          {
            @Override @Nonnull 
            public String toString (final @Nonnull ColorPoint colorPoint)
              {
                return String.format("(x=%f, y=%f, z=%f)", colorPoint.c1, colorPoint.c2, colorPoint.c3);  
              }
          },
        
        Lab
          {
            @Override @Nonnull 
            public String toString (final @Nonnull ColorPoint colorPoint)
              {
                return String.format("(L=%f, a*=%f, b*=%f)", colorPoint.c1, colorPoint.c2, colorPoint.c3);  
              }
          };
        
        public abstract String toString (@Nonnull ColorPoint colorPoint);
      }
    
    private final double c1;
    
    private final double c2;
    
    private final double c3;
    
    @Nonnull
    private final ColorSpace colorSpace;
    
    @Nonnull
    public static ColorPoint colorXYZ (final double x, final double y, final double z)
      {
        return new ColorPoint(x, y, z, ColorSpace.XYZ);
      }
    
    @Nonnull
    public static ColorPoint colorLab (final double l, final double a, final double b)
      {
        return new ColorPoint(l, a, b, ColorSpace.Lab);
      }
    
    @Override @Nonnull
    public String toString()
      {
        return colorSpace.toString(this);  
      }
  }
