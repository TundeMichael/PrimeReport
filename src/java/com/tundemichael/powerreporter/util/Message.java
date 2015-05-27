package com.tundemichael.powerreporter.util;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author michael.orokola
 */
public class Message {
    
    public static ELContext getELContext() {
        return FacesContext.getCurrentInstance().getELContext();
    }

    public static ExpressionFactory getExpressionFactory() {
        return getApplication().getExpressionFactory();
    }

    public static Application getApplication() {
        return FacesContext.getCurrentInstance().getApplication();
    }
    
    public static void addErrorMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        context.addMessage(msg, message);
    }

    public static void addSuccessMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        context.addMessage(msg, message);
    }
    
    public static void addErrorMessage(String summary, String details, String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();
        //context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, details);
        context.addMessage(clientId, message);
    }

    public static void addSuccessMessage(String summary, String details, String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();
        //context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, details);
        context.addMessage(clientId, message);
    }
    
    public static void addWarningMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        context.addMessage(msg, message);
    }
    
    public static void addWarningMessage(String summary, String details, String clientId) {
        FacesContext context = FacesContext.getCurrentInstance();
        //context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, details);
        context.addMessage(clientId, message);
    }
    
}
