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
package it.tidalwave.uniformity.archive.impl.io;

import javax.annotation.Nonnull;
import java.util.Random;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import it.tidalwave.uniformity.FakeUniformityMeasurementsGenerator;
import it.tidalwave.uniformity.UniformityMeasurements;
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
public class UniformityMeasurementsMarshallableTest 
  {
    @BeforeMethod
    public void setupLogging()
      {
        TestLoggerSetup.setupLogging(UniformityMeasurementsMarshallableTest.class);  
      }
    
    @Test(dataProvider="testCaseProvider")
    public void must_properly_marshall (final long seed, final @Nonnull String expectedValue) 
      throws IOException
      {
        final Random r = new Random(seed);
        final UniformityMeasurements measurements = FakeUniformityMeasurementsGenerator.createMeasurements("display1", r);
        final UniformityMeasurementsMarshallable fixture = new UniformityMeasurementsMarshallable(measurements);    
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        fixture.marshal(os);
        os.close();
        final String s = new String(os.toByteArray());
        
        assertThat(s, is(expectedValue));
      }
    
    @DataProvider(name="testCaseProvider")
    public Object[][] testCaseProvider()
      {
        return TestDataProvider.pr1();
      }
  }
