
package com.tundemichael.powerreporter.controllers;

import com.tundemichael.powerreporter.dao.ReportUserFacade;
import com.tundemichael.powerreporter.dao.UserGroupFacade;
import com.tundemichael.powerreporter.entities.ReportUser;
import com.tundemichael.powerreporter.entities.UserGroup;
import com.tundemichael.powerreporter.util.Message;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author michael.orokola
 *
 */

@Named
@SessionScoped
public class AdminController implements Serializable {

    private ReportUser reportUser;
    private ReportUser selectedReportUser;
    private List<ReportUser> reportUsers;
    private boolean renderCreateUserPanel;
    
    private UserGroup userGroup;
    private UserGroup selectedUserGroup;
    private List<UserGroup> userGroups;
    private boolean renderCreateGroupPanel;
    
    @Inject
    ReportUserFacade reportUserFacade;
    @Inject
    UserGroupFacade userGroupFacade;
    

    @PostConstruct
    public void init() {
        initializeMini();
        initializeMajor();
    }

    private void initializeMini() {
        this.reportUser = new ReportUser();
        this.userGroup = new UserGroup();
    }
    
    private void initializeMajor() {
        this.reportUser = new ReportUser();
        this.userGroup = new UserGroup();
        this.reportUsers = this.reportUserFacade.findAll();
        this.userGroups = this.userGroupFacade.findAll();
    }

    public ReportUser getReportUser() {
        return reportUser;
    }

    public void setReportUser(ReportUser reportUser) {
        this.reportUser = reportUser;
    }

    public ReportUser getSelectedReportUser() {
        return selectedReportUser;
    }

    public void setSelectedReportUser(ReportUser selectedReportUser) {
        this.selectedReportUser = selectedReportUser;
    }

    public List<ReportUser> getReportUsers() {
        return reportUsers;
    }

    public void setReportUsers(List<ReportUser> reportUsers) {
        this.reportUsers = reportUsers;
    }
    
    public void viewSelectedReportUser(ReportUser selectedReportUser){
        this.selectedReportUser = selectedReportUser;
    }
    
    public boolean getRenderCreatePanel() {
        return renderCreateUserPanel;
    }

    public void setRenderCreateUserPanel(boolean renderCreateUserPanel) {
        this.renderCreateUserPanel = renderCreateUserPanel;
    }

    public boolean getRenderCreateGroupPanel() {
        return renderCreateGroupPanel;
    }

    public void setRenderCreateGroupPanel(boolean renderCreateGroupPanel) {
        this.renderCreateGroupPanel = renderCreateGroupPanel;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public UserGroup getSelectedUserGroup() {
        return selectedUserGroup;
    }

    public void setSelectedUserGroup(UserGroup selectedUserGroup) {
        this.selectedUserGroup = selectedUserGroup;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }
    
    public void toggleCreateUserPanel(){
        initializeMini();
        this.renderCreateUserPanel = !this.renderCreateUserPanel;
    }
    
    public void toggleCreateGroupPanel(){
        initializeMini();
        this.renderCreateGroupPanel = !this.renderCreateGroupPanel;
    }

    public String createUser() {
        try {
            this.reportUserFacade.create(reportUser);
            initializeMajor();
            Message.addSuccessMessage("User created successfully!");
        } catch (Exception e) {
            Message.addErrorMessage("Error: User not created. " + e.getMessage());
        }
        return "";
    }
    
    public String editUser() {
        try {
            this.reportUserFacade.edit(selectedReportUser);
            initializeMajor();
            Message.addSuccessMessage("User updated successfully!");
        } catch (Exception e) {
            Message.addErrorMessage("Error: User not updated. " + e.getMessage());
        }
        return "";
    }
    
    public String createGroup() {
        try {
            this.userGroupFacade.create(userGroup);
            initializeMajor();
            Message.addSuccessMessage("Group created successfully!");
        } catch (Exception e) {
            Message.addErrorMessage("Error: Group not created. " + e.getMessage());
        }
        return "";
    }
    
    public String editGroup() {
        try {
            this.userGroupFacade.edit(selectedUserGroup);
            initializeMajor();
            Message.addSuccessMessage("Group updated successfully!");
        } catch (Exception e) {
            Message.addErrorMessage("Error: Group not updated. " + e.getMessage());
        }
        return "";
    }
    
    

}




