package org.integrityrater.service.user;

import org.catamarancode.util.ThreadLocalUtils;

public class UserContextLocator {

    public UserContext locate() {
        UserContext userContext = (UserContext) ThreadLocalUtils.get(UserContext.KEY);
        return userContext;
    }
}
