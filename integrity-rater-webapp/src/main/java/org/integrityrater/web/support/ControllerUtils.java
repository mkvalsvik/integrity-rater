package org.integrityrater.web.support;

import javax.servlet.http.HttpServletRequest;

import org.integrityrater.entity.support.Repository;
import org.springframework.web.servlet.ModelAndView;


public class ControllerUtils {

    public static ModelAndView createDefaultModelAndView(HttpServletRequest request, Repository repository, String viewName) {
        ModelAndView mv = null;
        if (viewName != null) {
            mv = new ModelAndView(viewName);
        } else {
            mv = new ModelAndView();
        }
        Message.addPendingToView(request, mv.getModelMap());
        if (HttpSessionUtils.isUserLoggedIn(request)) {
            HttpSessionUtils.addLoggedInUserToModel(request, repository, mv);    
        }  
        return mv;
    }
    
    public static ModelAndView createDefaultModelAndView(HttpServletRequest request, Repository repository) {
        return createDefaultModelAndView(request, repository, null);        
    }
}
