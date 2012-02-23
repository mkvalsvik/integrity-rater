package org.integrityrater.web.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

public class Message {
    
    private boolean success;
    private String text;
    
    public Message(String text, boolean success) {
        this.success = success;
        this.text = text;        
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getText() {
        return text;
    }

    public void setTest(String text) {
        this.text = text;
    }
    
    public String getSuccessString() {
        String prefix = "error";
        if (success) {
            prefix = "success";
        }
        return prefix;
    }
    
    public static final String KEY = "message";
    
    /**
     * Show any added messages by adding them to the given ModelAndView
     * @param request
     * @param mv
     */
    public static void addPendingToView(HttpServletRequest request, ModelMap model) {
        Message sessionMessage = (Message) request.getSession().getAttribute(KEY);
        if (sessionMessage != null) {
            addToModel(model, sessionMessage);
            request.getSession().setAttribute(KEY, null);
        }
    }
    
    public static void addToModel(ModelMap model, String message, boolean success) {
        addToModel(model, new Message(message, success));
    }
    
    public static void addToModel(ModelMap model, Message message) {
        model.addAttribute(KEY, message);
    }
    
    /**
     * Add a message to be shown next time we display a View
     * @param request
     */
    public void addPending(HttpServletRequest request) {
        addPending(request, this);
    }
    
    /**
     * Add a message to be shown next time we display a View
     * @param request
     * @param message
     */
    public static void addPending(HttpServletRequest request, Message message) {
        request.getSession().setAttribute(KEY, message);
    }
    
    /**
     * Add a message to be shown next time we display a View
     * @param request
     * @param message
     * @param success
     */
    public static void addPending(HttpServletRequest request, String message, boolean success) {
        request.getSession().setAttribute(KEY, new Message(message, success));
    }
    
    
    public String toString() {        
        return String.format("%s: %s", this.getSuccessString(), text);
    }

}
