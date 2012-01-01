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
package it.tidalwave.uniformity.ui.impl.measurement;

import javax.annotation.Nonnull;
import it.tidalwave.uniformity.Position;
import it.tidalwave.uniformity.ui.measurement.UniformityCheckMeasurementPresentation;
import lombok.extern.slf4j.Slf4j;
import static org.mockito.Mockito.*;

/***********************************************************************************************************************
 * 
 * @author  Fabrizio Giudici
 * @version $Id$
 *
 **********************************************************************************************************************/
@Slf4j
public class UniformityCheckMeasurementControllerActorTest extends UniformityCheckMeasurementControllerActorTestSupport
  {
    @Override @Nonnull
    protected UniformityCheckMeasurementPresentation createPresentation()
      {
        final UniformityCheckMeasurementPresentation presentation = mock(UniformityCheckMeasurementPresentation.class);
        doAnswer(actions.performActionWithDelay("Continue", 500)).when(presentation).renderSensorPlacementInvitationCellAt(any(Position.class));
        return presentation;
      }
  }
