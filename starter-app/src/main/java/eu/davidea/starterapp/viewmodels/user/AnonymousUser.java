package eu.davidea.starterapp.viewmodels.user;

import java.util.Date;

import eu.davidea.starterapp.viewmodels.enums.EnumAuthority;
import eu.davidea.starterapp.viewmodels.enums.EnumUserStatus;

public class AnonymousUser extends UserToken {

    public static final String ANONYMOUS = "Anonymous";

    public AnonymousUser() {
        setUsername(ANONYMOUS);
        setAuthority(EnumAuthority.ROLE_USER);
        setCreDate(new Date());
        setStatus(EnumUserStatus.ACTIVE);
    }

}