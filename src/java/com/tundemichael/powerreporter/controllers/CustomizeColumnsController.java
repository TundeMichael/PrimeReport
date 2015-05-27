package com.tundemichael.powerreporter.controllers;

import com.tundemichael.powerreporter.entities.ReportColumn;
import com.tundemichael.powerreporter.util.Message;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author michael.orokola
 *
 */
@Named
@ViewScoped
public class CustomizeColumnsController implements Serializable {

    private HtmlPanelGroup panelGroup;
    @Inject
    ReportSetupController setupController;

    private List<String[]> values;
    List<ReportColumn> queryColumns;

    @PostConstruct
    public void init() {
        queryColumns = this.setupController.getQueryColumns();
    }

    public HtmlPanelGroup getPanelGroup() {
        return panelGroup;
    }

    public void setPanelGroup(HtmlPanelGroup panelGroup) {
        this.panelGroup = panelGroup;
    }

    public List<String[]> getValues() {
        return values;
    }

    public void setValues(List<String[]> values) {
        this.values = values;
    }

    public List<ReportColumn> getQueryColumns() {
        return queryColumns;
    }

    public void setQueryColumns(List<ReportColumn> queryColumns) {
        this.queryColumns = queryColumns;
    }
     
    public String saveChanges(){
        for (ReportColumn q : this.queryColumns) {
            System.out.println(" Col  ==  " + q.getPrefferedName() + " Mask  ==  " + q.isDataMasked());
        }
        this.setupController.setQueryColumns(queryColumns);
        Message.addSuccessMessage("Columns Update Successful", "Columns Update Successful", null);
        return "";
    }

}


