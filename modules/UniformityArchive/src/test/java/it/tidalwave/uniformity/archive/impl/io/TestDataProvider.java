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
package it.tidalwave.uniformity.archive.impl.io;

import javax.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public final class TestDataProvider 
  {
    @Nonnull
    public static Object[][] pr1()
      {
        return new Object[][]
          {
            { 3234342562354698254L, "2010-08-20T06:47:26+02:00 ; D='display1' ; L[0,0]=100 ; T[0,0]= 3086"
                                                                          + " ; L[1,0]=106 ; T[1,0]= 5851"
                                                                          + " ; L[2,0]=  1 ; T[2,0]= 4851"
                                                                          + " ; L[0,1]= 63 ; T[0,1]= 4231"
                                                                          + " ; L[1,1]=105 ; T[1,1]= 3651"
                                                                          + " ; L[2,1]=177 ; T[2,1]= 4022"
                                                                          + " ; L[0,2]=145 ; T[0,2]= 5887"
                                                                          + " ; L[1,2]= 72 ; T[1,2]= 5433"
                                                                          + " ; L[2,2]= 31 ; T[2,2]= 5960\n"},
            { 4534634566765855245L, "2010-05-22T14:24:48+02:00 ; D='display1' ; L[0,0]=122 ; T[0,0]= 7954"
                                                                          + " ; L[1,0]=191 ; T[1,0]= 6678"
                                                                          + " ; L[2,0]= 86 ; T[2,0]= 2552"
                                                                          + " ; L[0,1]= 67 ; T[0,1]= 4690"
                                                                          + " ; L[1,1]= 26 ; T[1,1]= 7082"
                                                                          + " ; L[2,1]=130 ; T[2,1]= 2029"
                                                                          + " ; L[0,2]=108 ; T[0,2]= 3773"
                                                                          + " ; L[1,2]= 38 ; T[1,2]= 3053"
                                                                          + " ; L[2,2]= 77 ; T[2,2]= 5560\n"}  
          };
      }
    
    @Nonnull
    public static Object[][] pr2()
      {
        return new Object[][]
          {
            {  20, 3455476584532L },
            {  40, 7846724352547L },
            {  60, 9876355155425L },
            {  80, 2343235765789L },
            { 100, 6367648768745L }
          };
      }
  }
