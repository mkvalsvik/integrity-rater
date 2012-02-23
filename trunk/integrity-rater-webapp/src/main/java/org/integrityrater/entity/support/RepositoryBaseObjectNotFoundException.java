package org.integrityrater.entity.support;

public class RepositoryBaseObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 14252521L;

	public RepositoryBaseObjectNotFoundException(String message) {
		super(message);
	}
	
	public RepositoryBaseObjectNotFoundException(String message, Exception e) {
		super(message, e);
	}
}
