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

import javax.annotation.Nonnegative;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import it.tidalwave.netbeans.util.test.TestLoggerSetup;
import it.tidalwave.uniformity.archive.impl.UniformityArchive;
import it.tidalwave.uniformity.FakeUniformityMeasurementsGenerator;
import it.tidalwave.util.test.FileComparisonUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
public class UniformityArchiveMarshallableTest 
  {
    @BeforeMethod
    public void setupLogging()
      {
        TestLoggerSetup.setupLogging(UniformityArchiveMarshallableTest.class);  
      }
    
    @Test(dataProvider="testCaseProvider")
    public void must_properly_marshall (final @Nonnegative int size, final long seed)
      throws FileNotFoundException, IOException
      {
        final UniformityArchive archive = new UniformityArchive();
        final Random r = new Random(seed);
        
        for (int i = 0; i < size; i++)
          {
            archive.add(FakeUniformityMeasurementsGenerator.createMeasurements("display1", r));  
          }
        
        final UniformityArchiveMarshallable marshallable = new UniformityArchiveMarshallable(archive);
        final File targetFolder = new File("target/test-artifacts");
        final File actualFile = new File(targetFolder, "" + seed + ".txt");
        final File expectedFile = new File("src/test/resources/expected-results/" + seed + ".txt");
        targetFolder.mkdirs();
        final OutputStream os = new FileOutputStream(actualFile);
        marshallable.marshal(os);
        os.close();
        
        FileComparisonUtils.assertSameContents(actualFile, expectedFile);
      }
    
    @DataProvider(name="testCaseProvider")
    public Object[][] testCaseProvider()
      {
        return TestDataProvider.pr2();
      }
  }
