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
 * WWW: http://blueargyle.tidalwave.it
 * SCM: https://bitbucket.org/tidalwave/blueargyle-src
 *
 **********************************************************************************************************************/
package it.tidalwave.uniformity.archive.impl.io;

import javax.annotation.Nonnegative;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import it.tidalwave.uniformity.FakeUniformityMeasurementsGenerator;
import it.tidalwave.uniformity.archive.impl.UniformityArchive;
import it.tidalwave.netbeans.util.test.TestLoggerSetup;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class UniformityArchiveUnmarshallableTest 
  {
    @BeforeMethod
    public void setupLogging()
      {
        TestLoggerSetup.setupLogging(UniformityArchiveUnmarshallableTest.class);  
      }
    
    @Test(dataProvider="testCaseProvider")
    public void must_properly_unmarshall (final @Nonnegative int size, final long seed)
      throws FileNotFoundException, IOException
      {
        final UniformityArchiveUnmarshallable unmarshallable = new UniformityArchiveUnmarshallable();
        final File file = new File("src/test/resources/expected-results/" + seed + ".txt");
        final InputStream is = new FileInputStream(file);
        final UniformityArchive archive = unmarshallable.unmarshal(is);
        is.close();
        
        final UniformityArchive expectedArchive = new UniformityArchive();
        final Random r = new Random(seed);
        
        for (int i = 0; i < size; i++)
          {
            expectedArchive.add(FakeUniformityMeasurementsGenerator.createMeasurements("display1", r));  
          }
        
        assertThat(archive, is(expectedArchive));
      }
    
    @DataProvider(name="testCaseProvider")
    public Object[][] testCaseProvider()
      {
        return TestDataProvider.pr2();
      }
  }
