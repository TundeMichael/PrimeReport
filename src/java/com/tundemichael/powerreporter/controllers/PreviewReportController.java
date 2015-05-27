package com.tundemichael.powerreporter.controllers;

import com.tundemichael.powerreporter.entities.DropDown;
import com.tundemichael.powerreporter.entities.ReportParameter;
import com.tundemichael.powerreporter.entities.ReportColumn;
import com.tundemichael.powerreporter.util.Message;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.component.spacer.Spacer;

/**
 *
 * @author michael.orokola
 */
@Named
@ViewScoped
public class PreviewReportController implements Serializable {

    private HtmlPanelGroup panelGroup;
    @Inject
    ReportSetupController setupController;

    private List<String[]> values;
    List<ReportColumn> queryColumns;
    List<ReportParameter> queryParams;
    private String[] headings;
    private Map<String, Object> parameters = new HashMap<>();
    private String query;

    @PostConstruct
    public void init() {

        queryColumns = setupController.getQueryColumns();
        queryParams = this.setupController.getReportParameters();
        values = setupController.getReportData();
        this.query = this.setupController.getQuery();

        if (queryColumns != null) {
            this.headings = new String[queryColumns.size()];
            for (int i = 0; i < queryColumns.size(); i++) {
                this.headings[i] = queryColumns.get(i).getPrefferedName();
            }
        }else {
           queryColumns = new ArrayList<>();
        }

        Application application = FacesContext.getCurrentInstance().getApplication();
        panelGroup = (HtmlPanelGroup) application.createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        PanelGrid panelGrid = (PanelGrid) application.createComponent(PanelGrid.COMPONENT_TYPE);
        panelGrid.setColumns(2);

        HtmlForm searchForm = (HtmlForm) application.createComponent(HtmlForm.COMPONENT_TYPE);
        searchForm.setId("searchForm");

        for (ReportParameter param : queryParams) {

            String parameterLabel = param.getLabel();
            String componentType = param.getComponentType();
            String calendarPattern = param.getCalendarPattern();
            boolean required = param.isRequired();
            boolean userProperty = param.isUserProperty();
            String queryPosition = param.getQueryPosition();
            String sqlDatePattern = param.getSqlDatePattern();
            String dataType = param.getDataType();
            String userPropertyName = param.getUserPropertyName();
            List<DropDown> dropDowns = param.getDropDowns();

            if ((userProperty)) {
                Message.addSuccessMessage(parameterLabel + " is to be retrieved from the logged in user!");
            } else {

                OutputLabel label = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);

                label.setId(parameterLabel.replaceAll("\\s+", "") + "label");
                label.setValue(parameterLabel + ":");

                if (componentType.equalsIgnoreCase("Calendar")) {
                    Calendar cal = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);

                    cal.setId(parameterLabel.replaceAll("\\s+", ""));
                    cal.setShowButtonPanel(true);
                    cal.setPattern(calendarPattern);
                    cal.setRequired(required);

                    cal.setValueExpression("value",
                            getExpressionFactory()
                            .createValueExpression(getELContext(), "#{previewReportController.parameters['"
                                    + (parameterLabel.replaceAll("\\s+", "")) + "']}", Date.class));

                    label.setFor(parameterLabel.replaceAll("\\s+", ""));
                    panelGrid.getChildren().add(label);

                    panelGrid.getChildren().add(cal);

                } else if (componentType.equalsIgnoreCase("TextField")) {
                    InputText input = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
                    input.setRequired(required);
                    input.setId(parameterLabel.replaceAll("\\s+", ""));

                    input.setValueExpression("value",
                            getExpressionFactory()
                            .createValueExpression(getELContext(), "#{previewReportController.parameters['"
                                    + (parameterLabel.replaceAll("\\s+", "")) + "']}", String.class));

                    label.setFor(parameterLabel.replaceAll("\\s+", ""));
                    panelGrid.getChildren().add(label);

                    panelGrid.getChildren().add(input);
                } else if (componentType.equalsIgnoreCase("DropDown")) {

                    SelectOneMenu selectOneMenu = (SelectOneMenu) application.createComponent(SelectOneMenu.COMPONENT_TYPE);
                    UISelectItems selectItems = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
                    selectOneMenu.setRequired(required);
                    selectOneMenu.setId(parameterLabel.replaceAll("\\s+", ""));

                    selectItems.setId(parameterLabel.replaceAll("\\s+", "") + "selectItems");

                    List<SelectItem> items = new ArrayList<>();

                    items.add(new SelectItem("", "Select..."));
                    if (dropDowns != null && !dropDowns.isEmpty()) {
                        for (DropDown d : dropDowns) {
                            items.add(new SelectItem(d.getValue(), d.getLabel()));
                        }
                    }

                    selectItems.setValue(items);
                    selectOneMenu.getChildren().add(selectItems);

                    selectOneMenu.setValueExpression("value",
                            getExpressionFactory()
                            .createValueExpression(getELContext(), "#{previewReportController.parameters['"
                                    + (parameterLabel.replaceAll("\\s+", "")) + "']}", String.class));

                    label.setFor(parameterLabel.replaceAll("\\s+", ""));
                    panelGrid.getChildren().add(label);

                    panelGrid.getChildren().add(selectOneMenu);

                } else if (componentType.equalsIgnoreCase("RadioButton")) {

                    SelectOneRadio selectOneRadio = (SelectOneRadio) application.createComponent(SelectOneRadio.COMPONENT_TYPE);
                    UISelectItems selectItems = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
                    selectOneRadio.setRequired(required);
                    selectOneRadio.setId(parameterLabel.replaceAll("\\s+", ""));

                    selectItems.setId(parameterLabel.replaceAll("\\s+", "") + "selectItems");

                    List<SelectItem> items = new ArrayList<>();

                    items.add(new SelectItem("", "Select..."));
                    if (dropDowns != null && !dropDowns.isEmpty()) {
                        for (DropDown d : dropDowns) {
                            items.add(new SelectItem(d.getValue(), d.getLabel()));
                        }
                    }

                    selectItems.setValue(items);
                    selectOneRadio.getChildren().add(selectItems);

                    selectOneRadio.setValueExpression("value",
                            getExpressionFactory()
                            .createValueExpression(getELContext(), "#{previewReportController.parameters['"
                                    + (parameterLabel.replaceAll("\\s+", "")) + "']}", String.class));

                    label.setFor(parameterLabel.replaceAll("\\s+", ""));
                    panelGrid.getChildren().add(label);

                    panelGrid.getChildren().add(selectOneRadio);

                }
            }
        }
        searchForm.getChildren().add(panelGrid);

        CommandButton searchBtn = (CommandButton) application.createComponent(CommandButton.COMPONENT_TYPE);
        searchBtn.setId("searchBtnID");
        searchBtn.setValue("Search");
        searchBtn.setIcon("icon ui-icon-search");
        searchBtn.setAjax(false);

        ExpressionFactory ef = getApplication().getExpressionFactory();
        MethodExpression performSearch = ef.createMethodExpression(getELContext(),
                "#{previewReportController.performSearch}", null, new Class[]{ActionEvent.class});
        MethodExpressionActionListener meal = new MethodExpressionActionListener(performSearch);
        searchBtn.addActionListener(meal);
        searchBtn.setType("submit");

        searchForm.getChildren().add(searchBtn);
        panelGroup.getChildren().add(searchForm);

        Spacer spacer = (Spacer) application.createComponent(Spacer.COMPONENT_TYPE);
        spacer.setHeight("30");
        panelGroup.getChildren().add(spacer);

        HtmlForm downloadForm = (HtmlForm) application.createComponent(HtmlForm.COMPONENT_TYPE);
        downloadForm.setId("downloadForm");

        CommandLink downloadBtn = (CommandLink) application.createComponent(CommandLink.COMPONENT_TYPE);
        downloadBtn.setId("downloadBtnID");
        downloadBtn.setValue("Test Download");
        //downloadBtn.setIcon("icon ui-icon-search");
        downloadBtn.setAjax(false);

        MethodExpression downloadAnyReport = ef.createMethodExpression(getELContext(),
                "#{previewReportController.downloadAnyReport(previewReportController.values, "
                + "previewReportController.headings,'Report_Name')}", null, // Replace report name
                new Class[]{List.class, Collection.class, String.class});
        downloadBtn.setType("submit");
        downloadBtn.setActionExpression(downloadAnyReport);

        downloadForm.getChildren().add(downloadBtn);
        panelGroup.getChildren().add(downloadForm);

        HtmlForm dataTableForm = (HtmlForm) application.createComponent(HtmlForm.COMPONENT_TYPE);
        dataTableForm.setId("dataTableForm");

        DataTable dynamicTable = (DataTable) application.createComponent(DataTable.COMPONENT_TYPE);
        dynamicTable.setValueExpression("value", getExpressionFactory()
                .createValueExpression(getELContext(), "#{previewReportController.values}", List.class));
        dynamicTable.setVar("val");
        dynamicTable.setRows(20);

        List cols = new ArrayList<>();

        for (int i = 0; i < queryColumns.size(); i++) {
            Column col = (Column) application.createComponent(Column.COMPONENT_TYPE);
            col.setHeaderText(queryColumns.get(i).getPrefferedName());
            if (queryColumns.get(i).isLink()) {
                HtmlForm linkForm = (HtmlForm) application.createComponent(HtmlForm.COMPONENT_TYPE);
                linkForm.setId("linkForm");
                CommandLink link = (CommandLink) application.createComponent(CommandLink.COMPONENT_TYPE);

                if (queryColumns.get(i).isDataMasked()) {
                    link.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{previewReportController.maskStringValue(val[" + i + "]," + queryColumns.get(i).getLeftDigit() + "," + queryColumns.get(i).getRightDigit() + ")}", String.class));
                } else {
                    link.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{val[" + i + "]}", String.class));
                }

                link.setStyle("text-decoration: underline; color: blue;");
                MethodExpression action = ef.createMethodExpression(getELContext(),
                        "customizeColumns?faces-redirect=true", null, new Class[]{String.class});
                link.setAjax(false);
                link.setActionExpression(action);
                linkForm.getChildren().add(link);
                col.getChildren().add(linkForm);
            } else {
                HtmlOutputText out = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
                if (queryColumns.get(i).getFormatAs() != null && queryColumns.get(i).getFormatAs().equals("Amount")) {
                    out.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{previewReportController.formatAsAmount(val[" + i + "])}", String.class));
                } else if (queryColumns.get(i).getFormatAs() != null && queryColumns.get(i).getFormatAs().equals("Number")) {
                    out.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{previewReportController.formatAsNumber(val[" + i + "])}", String.class));
                } else if (queryColumns.get(i).isDataMasked()) {
                    out.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{previewReportController.maskStringValue(val[" + i + "]," + queryColumns.get(i).getLeftDigit() + "," + queryColumns.get(i).getRightDigit() + ")}", String.class));
                } else {
                    out.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{val[" + i + "]}", String.class));
                }

                col.getChildren().add(out);
            }
            cols.add(col);
        }

        dynamicTable.setColumns(cols);

        dynamicTable.setPaginator(true);
        dynamicTable.setRowsPerPageTemplate("20,50,100");

        dataTableForm.getChildren().add(dynamicTable);
        panelGroup.getChildren().add(dataTableForm);

    }

    public HtmlPanelGroup getPanelGroup() {
        return panelGroup;
    }

    public void setPanelGroup(HtmlPanelGroup panelGroup) {
        this.panelGroup = panelGroup;
    }

    public static ELContext getELContext() {
        return FacesContext.getCurrentInstance().getELContext();
    }

    public static ExpressionFactory getExpressionFactory() {
        return getApplication().getExpressionFactory();
    }

    public static Application getApplication() {
        return FacesContext.getCurrentInstance().getApplication();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getHeadings() {
        return headings;
    }

    public void setHeadings(String[] headings) {
        this.headings = headings;
    }

    public List<String[]> getValues() {
        return values;
    }

    public void setValues(List<String[]> values) {
        this.values = values;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    // All ArrayList of String Array
    public void downloadAnyReport(List<String[]> reports, String[] head, String reportName) {

        String[] headers = head;
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE_MM_yyyy_hh_mm_ss");
        String date = dateFormat.format(new Date());

        String fileName = reportName + "_" + date + ".xls";

        // Create Excel Workbook and Sheet
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(fileName);
        HSSFHeader header = sheet.getHeader();
        header.setCenter(fileName);

        //Setup the output
        String contentType = "application/vnd.ms-excel";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        response.setContentType(contentType);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        int col = 0;
        HSSFRow r = sheet.createRow(0);
        for (String h : headers) {
            r.createCell(col++).setCellValue(h);
        }

        for (int i = 0; i <= headers.length; i++) {
            sheet.setColumnWidth(i, 6000);
        }

        if (reports.size() < 1) {

            try {
                wb.write(out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            fc.responseComplete();

            return;

        }

        int rowIndex = 1;

        for (String[] rep : reports) {

            HSSFRow row = sheet.createRow(rowIndex);
            int columnIndex = 0;

            for (String rp : rep) {
                row.createCell(columnIndex++).setCellValue(rp);
            }

            rowIndex++;
        }

        try {
            wb.write(out);
            out.close();
            fc.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String formatAsAmount(String amount) {
        String newAmount;
        try {
            if (amount == null || amount.equals("null")) {
                return "-";
            }
            DecimalFormatSymbols format = DecimalFormatSymbols.getInstance();
            DecimalFormat formatter = new DecimalFormat("###,###.######", format);
            newAmount = formatter.format(Double.parseDouble(amount));
        } catch (NumberFormatException e) {
            return amount;
        }
        return newAmount;
    }

    public String formatAsNumber(String number) {
        String newNumber;
        try {
            if (number == null || number.equals("null")) {
                return "-";
            }
            newNumber = NumberFormat.getInstance().format(Long.parseLong(number));
        } catch (NumberFormatException e) {
            return number;
        }
        return newNumber;
    }

    public String maskStringValue(String value, int begining, int end) {
        String message;
        try {
            int count = value.length() - (begining + end);
            String hash = "";
            for (int i = 0; i < count; i++) {
                hash = hash + "#";
            }
            message = value.substring(0, begining) + hash + value.substring(value.length() - end);
        } catch (Exception ex) {
            message = value;
            ex.printStackTrace();
        }
        return message;
    }

    public String performSearch() {
        String value;
        String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        // we'll use this 'user' to retrieve other things 'bout the logged in user and use in the query.
        for (ReportParameter p : queryParams) {
            if (p.isUserProperty()) {
                value = "userproperty";
            } else {
                value = parameters.get(p.getLabel().replaceAll("\\s+", "")).toString();
            }

            String dataType = p.getDataType();
            if ("String".equals(dataType) || "Date".equals(dataType)) {
                value = "'" + value.trim() + "'";
            }
            this.query = this.query.replaceAll(p.getQueryPosition(), value);
        }
        Message.addSuccessMessage("Query: " + this.query);
        Object[] keys = parameters.keySet().toArray();

        return "";
    }

}
