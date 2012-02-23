package org.integrityrater.entity.support;

import org.integrityrater.service.user.UserContextLocator;


public class EntityServices {

	private Repository repository;
	private UserContextLocator userContextLocator;
	
	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

    public UserContextLocator getUserContextLocator() {
        return userContextLocator;
    }

    public void setUserContextLocator(UserContextLocator userContextLocator) {
        this.userContextLocator = userContextLocator;
    }
}
