package com.tundemichael.powerreporter.controllers;

import com.tundemichael.powerreporter.dao.ConnectionProfileFacade;
import com.tundemichael.powerreporter.dao.ReportFacade;
import com.tundemichael.powerreporter.dao.ReportMenuFacade;
import com.tundemichael.powerreporter.dao.ReportSubMenuFacade;
import com.tundemichael.powerreporter.dao.ReportUserFacade;
import com.tundemichael.powerreporter.dao.SetupDAO;
import com.tundemichael.powerreporter.dao.UserGroupFacade;
import com.tundemichael.powerreporter.dao.UserPropertyFacade;
import com.tundemichael.powerreporter.entities.ConnectionProfile;
import com.tundemichael.powerreporter.entities.DropDown;
import com.tundemichael.powerreporter.entities.Report;
import com.tundemichael.powerreporter.entities.ReportColumn;
import com.tundemichael.powerreporter.entities.ReportParameter;
import com.tundemichael.powerreporter.util.Message;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.model.SelectItem;
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
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.menubar.Menubar;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.spacer.Spacer;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSeparator;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author michael.orokola
 *
 */
@Named(value = "userController")
@SessionScoped
public class ReportUserController implements Serializable {

    private HtmlPanelGroup panelGroup;
    @Inject
    ConnectionProfileFacade profileFacade;
    @Inject
    ReportFacade reportFacade;
    @Inject
    ReportMenuFacade menuFacade;
    @Inject
    ReportSubMenuFacade subMenuFacade;
    @Inject
    UserGroupFacade userGroupFacade;
    @Inject
    ReportUserFacade userFacade;
    @Inject
    UserPropertyFacade userPropertyFacade;
    @Inject
    ReportSetupController setupController;
    private Report selectedReport;
    private List<String[]> values;
    private String query;
    private String[] headings;
    private HtmlPanelGroup reportPanelGroup;
    private Map<String, Object> parameters = new HashMap<>();
    List<ReportColumn> queryColumns;
    List<ReportParameter> queryParams;
    private SetupDAO pdao;
    private boolean renderReport;    

    @PostConstruct
    public void init() {
        values = new ArrayList<>();
        // Retrieve user's details and reports here
        // 
        renderReport = true;   
        MenuModel model = new DefaultMenuModel();
        Application application = FacesContext.getCurrentInstance().getApplication();
        panelGroup = (HtmlPanelGroup) application.createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        PanelGrid panelGrid = (PanelGrid) application.createComponent(PanelGrid.COMPONENT_TYPE);
        panelGrid.setColumns(2);
   
        // Instead of all, we retrieve reports for d group the logged-in user belong
        /// Retrieve d menus and submenus. 
        List<Report> repts = setupController.getExistingReports();

        DefaultSubMenu reports = new DefaultSubMenu("Reports");
        for (Report rep : repts) {
            DefaultMenuItem menuItem = new DefaultMenuItem(rep.getReportName());
            menuItem.setId(rep.getReportName().replaceAll("\\s+", "")); 
            menuItem.setOncomplete("clickAgain()");
            menuItem.setAjax(true);
            menuItem.setUpdate(":reportForm");
            menuItem.setProcess("@form");
            menuItem.setCommand("#{userController.loadReport(" + rep.getId() + ")}");
            reports.addElement(menuItem);
            reports.addElement(new DefaultSeparator());

        }
        model.addElement(reports);
        Menubar menubar = (Menubar) application.createComponent(Menubar.COMPONENT_TYPE);
        menubar.setModel(model);

        panelGroup.getChildren().add(menubar);

    }

    public boolean getRenderReport() {
        return renderReport;
    }

    public void setRenderReport(boolean renderReport) {
        this.renderReport = renderReport;
    }

    public HtmlPanelGroup getPanelGroup() {
        return panelGroup;
    }

    public void setPanelGroup(HtmlPanelGroup panelGroup) {
        this.panelGroup = panelGroup; 
    }

    public Report getSelectedReport() {    
        return selectedReport;
    }

    public void setSelectedReport(Report selectedReport) {
        this.selectedReport = selectedReport;
    }

    public List<ReportColumn> getQueryColumns() {
        return queryColumns;
    }

    public void setQueryColumns(List<ReportColumn> queryColumns) {
        this.queryColumns = queryColumns;
    }

    public List<ReportParameter> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<ReportParameter> queryParams) {
        this.queryParams = queryParams;
    }

    public List<String[]> getValues() {
        return values;
    }

    public void setValues(List<String[]> values) {
        this.values = values;
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

    public HtmlPanelGroup getReportPanelGroup() {
        return reportPanelGroup;
    }

    public void setReportPanelGroup(HtmlPanelGroup reportPanelGroup) {
        this.reportPanelGroup = reportPanelGroup;
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

    public void createDAO(ConnectionProfile profile) throws SQLException {
        String dbType = "";

        if ("mysql".equals(profile.getDatabaseType())) {
            dbType = "jdbc:mysql://";
        }

        if ("sybase".equals(profile.getDatabaseType())) {
            dbType = "jdbc:sybase:Tds:";
        }

        pdao = new SetupDAO(dbType + profile.getIpAdd() + ":" + profile.getPortNo() + "/"
                + profile.getDatabaseName(), profile.getUsername(), profile.getPassword(),
                profile.getDatabaseName());

    }

    public void loadReport(Long id) {

        renderReport = false;
        selectedReport = reportFacade.find(id);
        if (parameters != null) {
            parameters.clear();
        }

        if (values != null) {
            values.clear();
        }

        queryColumns = selectedReport.getColumns();

        for (ReportColumn r : queryColumns) {
            System.out.println(" Col :  " + r.getOriginalName());
        }

        queryParams = selectedReport.getReportParameters();
        query = selectedReport.getReportQuery();

        this.headings = new String[queryColumns.size()];
        for (int i = 0; i < queryColumns.size(); i++) {
            this.headings[i] = queryColumns.get(i).getPrefferedName();
        }
        buildPage();

        //return "";
    }

    public void buildPage() {

        Application application = FacesContext.getCurrentInstance().getApplication();
        reportPanelGroup = (HtmlPanelGroup) application.createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        PanelGrid panelGrid = (PanelGrid) application.createComponent(PanelGrid.COMPONENT_TYPE);
        panelGrid.setColumns(2);

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
                            .createValueExpression(getELContext(), "#{userController.parameters['"
                                    + queryPosition.trim() + "']}", Date.class));

                    label.setFor(parameterLabel.replaceAll("\\s+", ""));
                    panelGrid.getChildren().add(label);

                    panelGrid.getChildren().add(cal);

                } else if (componentType.equalsIgnoreCase("TextField")) {
                    InputText input = (InputText) application.createComponent(InputText.COMPONENT_TYPE);
                    input.setRequired(required);
                    input.setId(parameterLabel.replaceAll("\\s+", ""));

                    input.setValueExpression("value",
                            getExpressionFactory()
                            .createValueExpression(getELContext(), "#{userController.parameters['"
                                    + queryPosition.trim() + "']}", String.class));

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
                            .createValueExpression(getELContext(), "#{userController.parameters['"
                                    + queryPosition.trim() + "']}", String.class));

                    label.setFor(parameterLabel.replaceAll("\\s+", ""));
                    panelGrid.getChildren().add(label);

                    panelGrid.getChildren().add(selectOneMenu);

                }
            }
        }
        reportPanelGroup.getChildren().add(panelGrid);

        HtmlCommandButton searchBtn = (HtmlCommandButton) application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
        searchBtn.setId("searchBtnID");
        searchBtn.setValue("Search");
        //searchBtn.setIcon("icon ui-icon-search");
        //searchBtn.setAjax(false);

        ExpressionFactory ef = getApplication().getExpressionFactory();
        MethodExpression performSearch = ef.createMethodExpression(getELContext(),
                "#{userController.performSearch}", null, new Class[]{ActionEvent.class});
        MethodExpressionActionListener meal = new MethodExpressionActionListener(performSearch);
        searchBtn.addActionListener(meal);
        searchBtn.setType("submit");

        //searchForm.getChildren().add(searchBtn);
        reportPanelGroup.getChildren().add(searchBtn);

        Spacer spacer = (Spacer) application.createComponent(Spacer.COMPONENT_TYPE);
        spacer.setHeight("30");
        reportPanelGroup.getChildren().add(spacer);

        HtmlForm downloadForm = (HtmlForm) application.createComponent(HtmlForm.COMPONENT_TYPE);
        downloadForm.setId("downloadForm");

        CommandLink downloadBtn = (CommandLink) application.createComponent(CommandLink.COMPONENT_TYPE);
        downloadBtn.setId("downloadBtnID");
        downloadBtn.setValue("Test Download");
        //downloadBtn.setIcon("icon ui-icon-search");
        downloadBtn.setAjax(false);

        String reportName = this.selectedReport.getReportName().trim().replaceAll("\\s+", "").trim();

        MethodExpression downloadAnyReport = ef.createMethodExpression(getELContext(),
                "#{userController.downloadAnyReport(userController.values, "
                + "userController.headings, '" + reportName + "')}", null, // Replace report name
                new Class[]{List.class, Collection.class, String.class});
        downloadBtn.setType("submit");
        downloadBtn.setActionExpression(downloadAnyReport);

        downloadForm.getChildren().add(downloadBtn);
        reportPanelGroup.getChildren().add(downloadForm);

        DataTable dynamicTable = (DataTable) application.createComponent(DataTable.COMPONENT_TYPE);
        dynamicTable.setValueExpression("value", getExpressionFactory()
                .createValueExpression(getELContext(), "#{userController.values}", List.class));
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
                            "#{userController.maskStringValue(val[" + i + "]," + queryColumns.get(i).getLeftDigit() + "," + queryColumns.get(i).getRightDigit() + ")}", String.class));
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
                            "#{userController.formatAsAmount(val[" + i + "])}", String.class));
                } else if (queryColumns.get(i).getFormatAs() != null && queryColumns.get(i).getFormatAs().equals("Number")) {
                    out.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{userController.formatAsNumber(val[" + i + "])}", String.class));
                } else if (queryColumns.get(i).isDataMasked()) {
                    out.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{userController.maskStringValue(val[" + i + "]," + queryColumns.get(i).getLeftDigit() + "," + queryColumns.get(i).getRightDigit() + ")}", String.class));
                } else {
                    out.setValueExpression("value", getExpressionFactory().createValueExpression(getELContext(),
                            "#{val[" + i + "]}", String.class));
                }

                col.getChildren().add(out);
            }
            cols.add(col);
        }

        dynamicTable.setColumns(cols); 
        dynamicTable.setValueExpression("rendered", getExpressionFactory()
                .createValueExpression(getELContext(), "#{userController.values.size() > 0}", Boolean.class));
        
        dynamicTable.setPaginator(true);
        dynamicTable.setRowsPerPageTemplate("20,50,100");

        reportPanelGroup.getChildren().add(dynamicTable);

    }
    
    public void changeRender(){
        this.renderReport = true;
    }

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
                value = parameters.get(p.getQueryPosition().trim()).toString();
            }
            String dataType = p.getDataType();
            if ("String".equals(dataType) || "Date".equals(dataType)) {
                value = "'" + value.trim() + "'";
            }
            query = query.replaceAll(p.getQueryPosition().trim(), value);
        }

        try {
            createDAO(selectedReport.getConnectionProfile());
            pdao.open();
            values = pdao.getQueryData(query);
            queryColumns = pdao.getQueryColumns(query);

            buildPage();

            System.out.println(" Size === " + this.values.size());
            for (ReportColumn r : queryColumns) {
                System.out.println(" Col :  " + r.getOriginalName());
            }
            pdao.close();
            Message.addSuccessMessage("Query Ok", "", null);
        } catch (SQLException | ClassNotFoundException ex) {
            Message.addErrorMessage(ex.getMessage(), "", null);
            Logger.getLogger(ReportSetupController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Message.addSuccessMessage("SQL === \n " + this.query);
        Object[] keys = parameters.keySet().toArray();

        return "";
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

}
