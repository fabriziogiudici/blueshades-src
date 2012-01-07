/***********************************************************************************************************************
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 **********************************************************************************************************************/

package it.tidalwave.argyll;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import it.tidalwave.role.spi.DefaultDisplayable;
import it.tidalwave.netbeans.util.AsLookupSupport;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/***********************************************************************************************************************
 *
 * @author  fritz
 * @version $Id$
 *
 **********************************************************************************************************************/
@Immutable @Getter @EqualsAndHashCode(callSuper=false) @ToString(callSuper=false) 
public class ProfiledDisplay extends AsLookupSupport
  {
    @Nonnull
    private final Display display;
    
    @Nonnull
    private final String profileName;

    public ProfiledDisplay (final @Nonnull Display display, final @Nonnull String profileName) 
      {
        super(new Object[] { new DefaultDisplayable(display.getDisplayName(), display.getDisplayName()) });  
        this.display = display;
        this.profileName = profileName;
      }
  }
