package it.tidalwave.argyll;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class Profile 
  {
    @Getter
    private final String name;
  }
