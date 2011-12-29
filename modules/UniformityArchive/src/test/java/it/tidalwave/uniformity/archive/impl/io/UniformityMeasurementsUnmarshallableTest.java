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
package it.tidalwave.uniformity.archive.impl.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import it.tidalwave.uniformity.UniformityMeasurements;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class UniformityMeasurementsUnmarshallableTest 
  {
    private UniformityMeasurementsUnmarshallable fixture;
    
    @BeforeTest
    public void setupFixture()
      {
        fixture = new UniformityMeasurementsUnmarshallable(); 
      }
            
    @Test
    public void must_properly_unmarshal() 
      throws IOException
      {
        final String s = "2011-12-29T02:21:02+01:00 ; D='\\Display0' ; "
                       + "L[0,0]= 67 ; T[0,0]= 2753 ; L[1,0]=  6 ; T[1,0]= 6507 ; L[2,0]= 31 ; T[2,0]= 7284 "
                       + "L[0,1]= 37 ; T[0,1]= 4102 ; L[1,1]= 63 ; T[1,1]= 5925 ; L[2,1]= 81 ; T[2,1]= 6456 "
                       + "L[0,2]= 97 ; T[0,2]= 3813 ; L[1,2]= 33 ; T[1,2]= 2879 ; L[2,2]= 19 ; T[2,2]= 6071\n";  
        
        final InputStream is = new ByteArrayInputStream(s.getBytes());
        final UniformityMeasurements measurements = fixture.unmarshal(is);
        
        log.info("{}", measurements); // FIXME: assert
      }
  }
