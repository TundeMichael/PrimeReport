package com.tundemichael.powerreporter.controllers;

import com.tundemichael.powerreporter.entities.DropDown;
import com.tundemichael.powerreporter.entities.ReportParameter;
import com.tundemichael.powerreporter.entities.Report;
import com.tundemichael.powerreporter.entities.ReportColumn;
import com.tundemichael.powerreporter.util.Message;
import com.tundemichael.powerreporter.dao.SetupDAO;
import com.tundemichael.powerreporter.dao.ConnectionProfileFacade;
import com.tundemichael.powerreporter.dao.ReportFacade;
import com.tundemichael.powerreporter.dao.ReportUserFacade;
import com.tundemichael.powerreporter.entities.ConnectionProfile;
import com.tundemichael.powerreporter.entities.ReportUser;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.DualListModel;

/**
 *
 * @author michael.orokola
 *
 */
@Named(value = "setupController")
@SessionScoped
public class ReportSetupController implements Serializable {

    private HtmlPanelGroup parameterPanelGroup;
    private boolean pdf;
    private boolean excel;
    private boolean csv;
    private ReportColumn reportColumn;
    private List<ReportColumn> reportColumns;
    private ReportParameter reportParameter;
    private List<ReportParameter> reportParameters;
    private List<DropDown> dropDowns;
    private Report report;
    private Report selectedReport;
    private String dataSource;
    private DropDown dropDown;
    private List<String> tables;
    private String selectedTable;
    private List<ReportColumn> columns;
    private List<ReportColumn> queryColumns;
    private List<ReportColumn> selectedColumns;
    SetupDAO pdao;
    private String query;
    private String testQuery;
    private DualListModel<ReportColumn> columnList;
    private ConnectionProfile reportProfile;
    private ConnectionProfile selectedProfile;
    private boolean showNewProfilePanel;
    private boolean showProfileListPanel;
    private boolean showDatabaseTablePanel;
    private List<ConnectionProfile> profiles;
    private boolean showData;
    private List<String[]> reportData;
    private String backUrl;
    private List<Report> existingReports;

    @EJB
    ConnectionProfileFacade profileFacade;
    @Inject
    ReportFacade reportFacade;
    @Inject
    ReportUserFacade userFacade;
    private ReportUser currentUser;

    public ReportSetupController() {
        columnList = new DualListModel<>(columns, selectedColumns);
        reportProfile = new ConnectionProfile();
        showNewProfilePanel = true;  
        showProfileListPanel = false;
        showDatabaseTablePanel = false;
    }

    @PostConstruct
    public void init() {

        setUp();
        String username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        List list = userFacade.getEntityManager().createNamedQuery("ReportUser.findByUserName")
                .setParameter("username", username).getResultList();
        if(list!=null && !list.isEmpty()){
          currentUser = (ReportUser)list.get(0);
        }else{
         //throw new RuntimeException("Major security break. User not logged in.");
        }
        
    }

    public void setUp() {
        showNewProfilePanel = false;
        showProfileListPanel = true;
        showDatabaseTablePanel = false;
        profiles = new ArrayList<>();
        reportProfile = new ConnectionProfile();
        selectedProfile = new ConnectionProfile();
        selectedProfile.setId(1L);
        tables = new ArrayList<>();
        columns = new ArrayList<>();
        selectedColumns = new ArrayList<>();
        reportColumn = new ReportColumn();
        reportColumns = new ArrayList<>();
        dropDown = new DropDown();
        dropDowns = new ArrayList<>();
        reportParameter = new ReportParameter();
        reportParameter.setDropDowns(dropDowns);

        reportParameters = new ArrayList<>();

        report = new Report();
        report.setColumns(reportColumns);
        report.setReportParameters(reportParameters);
        report.setDateCreated(new Date());

        dataSource = "";
        pdf = true;
        csv = true;
        excel = true;
        
        this.profiles = this.profileFacade.findAll();
        this.existingReports = reportFacade.findAll();
    }

    public List<String[]> getReportData() {
        return reportData;
    }

    public void setReportData(List<String[]> reportData) {
        this.reportData = reportData;
    }

    public boolean getShowData() {
        return showData;
    }

    public void setShowData(boolean showData) {
        this.showData = showData;
    }

    public HtmlPanelGroup getParameterPanelGroup() {
        return parameterPanelGroup;
    }

    public void setParameterPanelGroup(HtmlPanelGroup parameterPanelGroup) {
        this.parameterPanelGroup = parameterPanelGroup;
    }

    public Report getSelectedReport() {
        return selectedReport;
    }

    public void setSelectedReport(Report selectedReport) {
        this.selectedReport = selectedReport;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public List<Report> getExistingReports() {
        return existingReports;
    }

    public void setExistingReports(List<Report> existingReports) {
        this.existingReports = existingReports;
    }
    
    public String editSelectedReport(Report selectedReport){
        this.selectedReport = selectedReport;
        return "/admin/viewReport.xhtml?faces-redirect=true";
    }
    
    public void testRunSelectedReport(){
        query = this.selectedReport.getReportQuery();
        selectedProfile = this.selectedReport.getConnectionProfile();
        
    }
    
    public String updateSelectedReport(){
        try {
            reportFacade.edit(selectedReport);
            Message.addSuccessMessage("Report Updated Successfully!");
            return "/admin/viewReport.xhtml?faces-redirect=true";
        } catch (Exception e) {
            Message.addErrorMessage("Error: Report not updated. " + e.getMessage());
            return "/admin/editReport.xhtml?faces-redirect=true";
        }
        
        
    }

    public String addColumn() {

        reportColumns.add(reportColumn);
        reportColumn = new ReportColumn();

        return "";
    }

    public String addDropDownElement(ReportParameter reportParameter) {

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("sheet title");

        if (reportParameter.getDropDowns() == null) {
            List<DropDown> dds = new ArrayList<>();
            dds.add(dropDown);
            reportParameter.setDropDowns(dds);
        } else {
            reportParameter.getDropDowns().add(dropDown);
        }

        dropDown = new DropDown();

        return "";
    }

    public String addDropDownElement() {

        dropDown = new DropDown();

        return "";
    }

    public String addParameter() {

        reportParameters.add(reportParameter);
        reportParameter = new ReportParameter();

        return "";
    }

    public ConnectionProfile getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(ConnectionProfile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public boolean getPdf() {
        return pdf;
    }

    public void setPdf(boolean pdf) {
        this.pdf = pdf;
    }

    public boolean getExcel() {
        return excel;
    }

    public void setExcel(boolean excel) {
        this.excel = excel;
    }

    public boolean getCsv() {
        return csv;
    }

    public void setCsv(boolean csv) {
        this.csv = csv;
    }

    public ReportColumn getReportColumn() {
        return reportColumn;
    }

    public void setReportColumn(ReportColumn reportColumn) {
        this.reportColumn = reportColumn;
    }

    public ReportParameter getReportParameter() {
        return reportParameter;
    }

    public void setReportParameter(ReportParameter reportParameter) {
        this.reportParameter = reportParameter;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<ReportColumn> getReportColumns() {
        return reportColumns;
    }

    public void setReportColumns(List<ReportColumn> reportColumns) {
        this.reportColumns = reportColumns;
    }

    public List<ReportParameter> getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(List<ReportParameter> reportParameters) {
        this.reportParameters = reportParameters;
    }

    public List<DropDown> getDropDowns() {
        return dropDowns;
    }

    public void setDropDowns(List<DropDown> dropDowns) {
        this.dropDowns = dropDowns;
    }

    public DropDown getDropDown() {
        return dropDown;
    }

    public boolean getShowProfileListPanel() {
        return showProfileListPanel;
    }

    public void setShowProfileListPanel(boolean showProfileListPanel) {
        this.showProfileListPanel = showProfileListPanel;
    }

    public List<ConnectionProfile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ConnectionProfile> profiles) {
        this.profiles = profiles;
    }

    public void setDropDown(DropDown dropDown) {
        this.dropDown = dropDown;
    }

    public String deleteColumn(ReportColumn col) {

        reportColumns.remove(col);

        Message.addErrorMessage("Column deleted!", "", null);
        return "";
    }

    public String deleteDropDown(DropDown drop) {

        //reportColumns.remove(col);
        Message.addErrorMessage("Drop down deleted!", "", null);
        return "";
    }

    public String deleteParameter(ReportParameter param) {

        reportParameters.remove(param);
        Message.addErrorMessage("Parameter deleted!", "", null);
        return "";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return "/admin/index.xhtml?faces-redirect=true";
    }

    public String retrieveTables() {

        try {

            if (reportProfile != null && reportProfile.getProfileName() != null) {
                createDAO(reportProfile);
                pdao.open();
                tables = pdao.getDBTables();
                showDatabaseTablePanel = true;
            } else if (selectedProfile != null && selectedProfile.getProfileName() != null) {
                createDAO(selectedProfile);
                pdao.open();
                tables = pdao.getDBTables(selectedProfile);
                showDatabaseTablePanel = true;
            } else {
                Message.addErrorMessage("No connection profile selected!", "", null);
                showDatabaseTablePanel = false;
            }
            pdao.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    public List<String> getTables() {
        return tables;
    }

    public boolean isShowDatabaseTablePanel() {
        return showDatabaseTablePanel;
    }

    public void setShowDatabaseTablePanel(boolean showDatabaseTablePanel) {
        this.showDatabaseTablePanel = showDatabaseTablePanel;
    }

    public String getSelectedTable() {
        return selectedTable;
    }

    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    public List<ReportColumn> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(List<ReportColumn> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public DualListModel<ReportColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(DualListModel<ReportColumn> columnList) {
        this.columnList = columnList;
    }

    public List<ReportColumn> getColumns() {

        if (pdao != null && selectedTable != null) {
            try {
                //createDAO(reportProfile);
                createDAO(selectedProfile);
                pdao.open();

                columns = pdao.getTableColumns(selectedTable);
                pdao.close();
                selectedColumns = new ArrayList<>();
                columnList = new DualListModel<>(columns, selectedColumns);

                return columns; // = pdao.getTableColumns(selectedTable);
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(ReportSetupController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return columns;
    }

    public String renderPreview() {

        return "preview?faces-redirect=true";
    }

    public String runQuery() {
        setTestQuery(query);
        if (query != null && !"".equals(query.trim())) {
            try {
                createDAO(this.selectedProfile);
                pdao.open();
                this.queryColumns = pdao.getQueryColumns(query);
                if (showData) {
                    this.reportData = pdao.getQueryData(query);
                    System.out.println(" ======== size = " + this.reportData.size());
                } else {
                    this.reportData = new ArrayList<>();
                }
                pdao.close();
                Message.addSuccessMessage("Query Ok", "", null);
            } catch (SQLException | ClassNotFoundException ex) {
                Message.addErrorMessage(ex.getMessage(), "", null);
                Logger.getLogger(ReportSetupController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Message.addErrorMessage("Invalid Query", "", null);
        }

        return "";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTestQuery() {
        return testQuery;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
    }

    public List<ReportColumn> getQueryColumns() {
        return queryColumns;
    }

    public void setQueryColumns(List<ReportColumn> queryColumns) {
        this.queryColumns = queryColumns;
    }

    public boolean getShowNewProfilePanel() {
        return showNewProfilePanel;
    }

    public void setShowNewProfilePanel(boolean showNewProfilePanel) {
        this.showNewProfilePanel = showNewProfilePanel;
    }

    public ConnectionProfile getReportProfile() {
        return reportProfile;
    }

    public void setReportProfile(ConnectionProfile reportProfile) {
        this.reportProfile = reportProfile;
    }

    public ReportUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(ReportUser currentUser) {
        this.currentUser = currentUser;
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

    public void testConnection() {

        try {
            createDAO(reportProfile);
            pdao.open();
            pdao.close();

            Message.addSuccessMessage("Connection Ok", "", null);

        } catch (SQLException | ClassNotFoundException ex) {
            Message.addErrorMessage(ex.getMessage(), "", null);
            Logger.getLogger(ReportSetupController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String saveConnection() {

        if (reportProfile != null) {
            try {
                this.profileFacade.create(reportProfile);
                profiles.add(reportProfile);
                selectedProfile = new ConnectionProfile();
                reportProfile = new ConnectionProfile();
                Message.addSuccessMessage("Connection Saved!", "", null);
                showNewProfilePanel = false;
                showProfileListPanel = true;
                showDatabaseTablePanel = false;
            } catch (Exception e) {
                Logger.getLogger(ReportSetupController.class.getName()).log(Level.SEVERE, null, e);
                Message.addErrorMessage("Error: " + e.getMessage());
            }

        }

        return "";
    }

    public String cancelConnection() {
        showNewProfilePanel = false;
        showProfileListPanel = true;
        showDatabaseTablePanel = false;
        return "";
    }

    public String newConnection() {

        showNewProfilePanel = true;
        showProfileListPanel = true;
        showDatabaseTablePanel = false;

        reportProfile = new ConnectionProfile();

        return "";
    }

    public String editConnection() {

        showNewProfilePanel = true;
        showProfileListPanel = true;
        showDatabaseTablePanel = false;

        reportProfile = selectedProfile;

        return "";
    }

    public String saveReport() {

        try {
            this.report.setReportQuery(query);
            this.report.setColumns(queryColumns);
            this.report.setConnectionProfile(selectedProfile);
            this.report.setReportParameters(reportParameters);
            this.reportFacade.create(report);
            Message.addSuccessMessage("Report Created Successfully!");
            
            setUp();
            
        } catch (Exception e) {
            Logger.getLogger(ReportSetupController.class.getName()).log(Level.SEVERE, null, e);
            Message.addErrorMessage("Error: " + e.getMessage());
        }

        return "/admin/reports?faces-redirect=true";
    }

    public String navigate(String from, String to) {
        setBackUrl(from);
        return to;
    }

}
