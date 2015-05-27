package com.tundemichael.powerreporter.controllers;

import com.tundemichael.powerreporter.entities.ReportParameter;
import com.tundemichael.powerreporter.util.Message;
import com.tundemichael.powerreporter.entities.DropDown;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class ConfigureParameterController implements Serializable {

    @Inject
    ReportSetupController setupController;

    private List<ReportParameter> queryParams;
    private String query;
    private List<String> params;
    private ReportParameter selectedParam;
    private DropDown dropdown;
    private boolean renderDrodownPanel;

    @PostConstruct
    public void init() {
        queryParams = this.setupController.getReportParameters();
        query = this.setupController.getQuery();
        dropdown = new DropDown();
    }

    public List<ReportParameter> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<ReportParameter> queryParams) {
        this.queryParams = queryParams;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public ReportParameter getSelectedParam() {
        return selectedParam;
    }

    public void setSelectedParam(ReportParameter selectedParam) {
        this.selectedParam = selectedParam;
    }

    public DropDown getDropdown() {
        return dropdown;
    }

    public void setDropdown(DropDown dropdown) {
        this.dropdown = dropdown;
    }

    public boolean getRenderDrodownPanel() {
        return renderDrodownPanel;
    }

    public void setRenderDrodownPanel(boolean renderDrodownPanel) {
        this.renderDrodownPanel = renderDrodownPanel;
    }

    public String setRenderDrodown(boolean render) {
        renderDrodownPanel = render;
        return "";
    }

    public String prepareAddDropDown(ReportParameter param) {
        selectedParam = param;
        if (selectedParam.getDropDowns() == null) {
            selectedParam.setDropDowns(new ArrayList<DropDown>());
        }
        if (dropdown != null && !dropdown.getLabel().trim().isEmpty() && !dropdown.getValue().trim().isEmpty()) {
            selectedParam.getDropDowns().add(dropdown);
            queryParams.set(queryParams.indexOf(selectedParam), selectedParam);
        }
        dropdown = new DropDown();
        return "";
    }

    public String analyzeQuery() {

        this.setupController.setReportParameters(new ArrayList<ReportParameter>());
        String[] elements = query.split("\\s+");
        params = new ArrayList<>();

        for (String e : elements) {
            if (e.startsWith("@") && e.endsWith("@") && !params.contains(e)) {
                params.add(e);
            } else if ((e.startsWith("@") && !e.endsWith("@")) || (!e.startsWith("@") && e.endsWith("@"))) {
                Message.addErrorMessage("Invalid parameter: " + e
                        + "... Pease remove white spaces.",
                        "Invalid parameter", null);
                break;
            }
        }
        if (params == null || params.isEmpty()) {
            Message.addWarningMessage("No Parameters set in the query! "
                    + "Please re-write the query if parameters are required for the report.",
                    "Some parameters not present in the query yet", null);
        } else {
            for (String e : params) { // ensure no duplicate param, ensure p is used
                String a = e.substring(1);
                a = a.substring(0, a.length() - 1);
                if (e.equalsIgnoreCase("@startDate@")) {
                    this.setupController.getReportParameters().add(new ReportParameter("Start Date",
                            "Calendar", e, true, "Date"));
                } else if (e.equalsIgnoreCase("@endDate@")) {
                    this.setupController.getReportParameters().add(new ReportParameter("End Date",
                            "Calendar", e, true, "Date"));
                } else {
                    this.setupController.getReportParameters().add(new ReportParameter(a,
                            "Select...", e, true, "Select..."));
                }
            }
        }
        this.setupController.setQuery(this.query);
        queryParams = this.setupController.getReportParameters();
        return "";
    }

}
