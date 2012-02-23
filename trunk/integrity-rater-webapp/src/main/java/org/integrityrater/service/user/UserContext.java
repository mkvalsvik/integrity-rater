package org.integrityrater.service.user;

import org.integrityrater.entity.Person;

public class UserContext {
    
    public static final String KEY = "userContext";
    
    private Person loggedInUser;

    public Person getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Person loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
    
}
