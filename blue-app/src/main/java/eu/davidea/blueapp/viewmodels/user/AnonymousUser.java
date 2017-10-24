package eu.davidea.blueapp.viewmodels.user;

import java.util.Date;

import eu.davidea.blueapp.viewmodels.enums.EnumAuthority;
import eu.davidea.blueapp.viewmodels.enums.EnumUserStatus;

public class AnonymousUser extends UserToken {

    public static final String ANONYMOUS = "Anonymous";

    public AnonymousUser() {
        setUsername(ANONYMOUS);
        setAuthority(EnumAuthority.ROLE_USER);
        setCreDate(new Date());
        setStatus(EnumUserStatus.ACTIVE);
    }

}