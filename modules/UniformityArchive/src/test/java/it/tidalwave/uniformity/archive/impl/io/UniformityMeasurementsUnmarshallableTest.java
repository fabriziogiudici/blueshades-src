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

import javax.annotation.Nonnull;
import java.util.Random;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import it.tidalwave.netbeans.util.test.TestLoggerSetup;
import it.tidalwave.uniformity.FakeUniformityMeasurementsGenerator;
import it.tidalwave.uniformity.UniformityMeasurements;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class UniformityMeasurementsUnmarshallableTest 
  {
    @BeforeMethod
    public void setupLogging()
      {
        TestLoggerSetup.setupLogging(UniformityMeasurementsUnmarshallableTest.class);  
      }
    
    @Test(dataProvider="testCaseProvider")
    public void must_properly_unmarshall (final long seed, final @Nonnull String marshalledData) 
      throws IOException
      {
        final UniformityMeasurementsUnmarshallable fixture = new UniformityMeasurementsUnmarshallable();    
        final ByteArrayInputStream os = new ByteArrayInputStream(marshalledData.getBytes());
        final UniformityMeasurements measurements = fixture.unmarshal(os);
        os.close();
        
        final Random r = new Random(seed);
        final UniformityMeasurements expectedMeasurements = FakeUniformityMeasurementsGenerator.createMeasurements("display1", r);
        assertThat(measurements, is(expectedMeasurements));
      }
    
    @DataProvider(name="testCaseProvider")
    public Object[][] testCaseProvider()
      {
        return TestDataProvider.pr1();
      }
  }
