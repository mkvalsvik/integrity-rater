package org.integrityrater.web.support;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.catamarancode.spring.mvc.UserNotSignedInException;
import org.integrityrater.entity.Person;
import org.integrityrater.entity.support.Repository;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;


public class HttpSessionUtils {

    private HttpSessionUtils() {
    }

    public static final String SESSION_ATTRIBUTE_USER_ID = "userId";
    public static final String SESSION_ATTRIBUTE_TARGET_PATH = "targetPath";
    public static final String LOGGED_IN_USER = "loggedInUser";

    public static void removeLoggedInUser(HttpServletRequest request) {
        request.getSession().setAttribute(SESSION_ATTRIBUTE_USER_ID, null);
    }
    
    private static void storeKeyValue(HttpServletRequest request, String key, Object value) {
        request.getSession().setAttribute(key, value);
    }
    
    private static Object loadKeyValue(HttpServletRequest request, String key) {
        return request.getSession().getAttribute(key);
    }
    
    private static void removeKeyValue(HttpServletRequest request, String key) {
        storeKeyValue(request, key, null);
    }
    
    /**
     * Used during login as a workaround for frequent timeout bug
     * @param request
     */
    public static void invalidateExistingSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    
    public static void storeLoggedInUser(HttpServletRequest request, Person user) {
        request.getSession().setAttribute(SESSION_ATTRIBUTE_USER_ID, user.getId());
    }
    
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Long userId = (Long) session.getAttribute(
                SESSION_ATTRIBUTE_USER_ID);
        if (userId == null) {
            return false;
        }
        return true;
    }
    
    public static Serializable retrieveLoggedInUserId(HttpServletRequest request) {
        Serializable userId = (Serializable) request.getSession().getAttribute(
                SESSION_ATTRIBUTE_USER_ID);
        /*
        if (userId == null) {
            throw new RuntimeException(
                    "Logged in user not found in http session.");
        }
        */
        return userId;
    }
    
    /**
     * Retrieve user from session and then re-load that user to ensure he is
     * properly bound to Hibernate session
     * 
     * @throws UserNotSignedInException if a user is not logged in. (Note this method therefore does double duty as an authorization check)
     */
    public static Person retrieveLoggedInUser(
            HttpServletRequest request, Repository repository) {
        Long userId = (Long) retrieveLoggedInUserId(request);
        if (userId == null) {
            
            // Throw an exception since the user SHOULD have been logged in to access this page
            throw new UserNotSignedInException();            
        }
        Person freshUser = repository.loadPerson(userId);
        return freshUser;
    }
    
    public static void addLoggedInUserToModel(
            HttpServletRequest request, Repository repository, ModelMap model) {
        Person user = retrieveLoggedInUser(request, repository);
        model.addAttribute(LOGGED_IN_USER, user);
    }
    
    public static void addLoggedInUserToModel(
            HttpServletRequest request, Repository repository, ModelAndView mv) {
        addLoggedInUserToModel(request, repository, mv.getModelMap());
    }
  

}
