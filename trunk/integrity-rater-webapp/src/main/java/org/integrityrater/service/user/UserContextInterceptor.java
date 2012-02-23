package org.integrityrater.service.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.catamarancode.spring.mvc.UserNotSignedInException;
import org.catamarancode.util.ThreadLocalUtils;
import org.integrityrater.entity.Person;
import org.integrityrater.entity.support.Repository;
import org.integrityrater.web.support.HttpSessionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * Creates a UserContext and binds it to ThreadLocal for later use in domain objects
 * @author mkvalsvik
 *
 */
public class UserContextInterceptor implements HandlerInterceptor {

    private Repository repository;
    
	private static Log log = LogFactory
			.getLog(UserContextInterceptor.class);

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
	    
	    Person loggedInUser = null;
	    try {
	        loggedInUser = HttpSessionUtils.retrieveLoggedInUser(request, repository);
	    } catch (UserNotSignedInException e) {
	        // ignore
	    }
	    
	    if (loggedInUser != null) {
	        UserContext userContext = new UserContext();
	        userContext.setLoggedInUser(loggedInUser);
	        ThreadLocalUtils.set(UserContext.KEY, userContext);
	    }
	    
	    return true;
	}

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

}
