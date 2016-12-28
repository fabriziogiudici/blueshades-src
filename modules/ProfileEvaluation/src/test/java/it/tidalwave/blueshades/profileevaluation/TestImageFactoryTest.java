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
package it.tidalwave.blueshades.profileevaluation;

import javax.annotation.Nonnull;
import org.imajine.image.EditableImage;
import org.imajine.image.op.ConvertToBufferedImageOp;
import org.imajine.image.op.WriteOp;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/***********************************************************************************************************************
 *
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class TestImageFactoryTest 
  {
    private TestImageFactory fixture;
    
    @BeforeTest
    public void setupFixture()
      {
        fixture = new TestImageFactory();
      }

    @Test(dataProvider="profileNames")
    public void must_create_a_valid_Granger_rainbow (final @Nonnull String profileName) 
      throws Exception
      {
        final EditableImage image = fixture.createGrangerRainbow(1024, 768, profileName);
        image.execute2(new ConvertToBufferedImageOp()).execute(new WriteOp("TIFF", "/tmp/grangersynth " + profileName + ".tif"));
        // FIXME: assertion        
      }  
    
    @DataProvider(name="profileNames")
    public Object[][] profileNameProvider()
      {
        return new String[][] {{ "Adobe98" }, { "ProPhoto" }, { "MelissaRGB" }};
      }
  }
