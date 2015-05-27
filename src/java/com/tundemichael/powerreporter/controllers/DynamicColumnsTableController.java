package com.tundemichael.powerreporter.controllers;

import com.tundemichael.powerreporter.entities.DropDown;
import com.tundemichael.powerreporter.entities.ReportParameter;
import com.tundemichael.powerreporter.entities.ReportColumn;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.selectonemenu.SelectOneMenu;

/**
 *
 * @author michael.orokola
 *
 */
@ManagedBean(name = "dynamicColumnsTableController")//Not Really used in this project. Kept for Version2
@ViewScoped
public class DynamicColumnsTableController implements Serializable {

    @Inject
    ReportSetupController setupController;
    private HtmlPanelGroup panelGroup;
    private Map<String, Object> values = new HashMap<>();
    private static final long serialVersionUID = 20111020L;
    private List<Message> messages;
    private List<String> expectedColumns;
    private List<ColumnModel> columns = new ArrayList<>();

    public DynamicColumnsTableController() {

    }

    @PostConstruct
    public void init() {
        // Retrieve the report from the database here
        expectedColumns = new ArrayList<>();
        //Set<String> downloadFormats = setupController.getPowerReport().getDownloadFormats();

        List<ReportParameter> reportParameters = setupController.getReport().getReportParameters();
        Application application = FacesContext.getCurrentInstance().getApplication();
        PanelGrid panelGrid = (PanelGrid) application.createComponent(PanelGrid.COMPONENT_TYPE);
        panelGroup = (HtmlPanelGroup) application.createComponent(HtmlPanelGroup.COMPONENT_TYPE);
        panelGrid.setColumns(reportParameters.size());
        
        panelGrid.getChildren().clear();

        for (ReportParameter param : reportParameters) {

            String parameterName = param.getLabel();
            String componentType = param.getComponentType();
            List<DropDown> dropDowns = param.getDropDowns();

            OutputLabel label = (OutputLabel) application.createComponent(OutputLabel.COMPONENT_TYPE);

            label.setId(parameterName.replaceAll("\\s+", "") + "label");
            label.setValue(parameterName + ":");

            if (componentType.equalsIgnoreCase("Calendar")) {
                Calendar cal = (Calendar) application.createComponent(Calendar.COMPONENT_TYPE);

                cal.setId(parameterName.replaceAll("\\s+", "") + "calendar");
                cal.setShowButtonPanel(true);
                cal.setPattern("EEEEEE, MM, yyyy hh:mm:ss");

                cal.setValueExpression("value",
                        getExpressionFactory()
                        .createValueExpression(getELContext(), "#{dynamicColumnsTableController.values['"
                                + (parameterName.replaceAll("\\s+", "") + "calendar") + "']}", Date.class));

                label.setFor(parameterName.replaceAll("\\s+", "") + "calendar");
                panelGrid.getChildren().add(label);

                panelGrid.getChildren().add(cal);

            } else if (componentType.equalsIgnoreCase("TextField")) {
                InputText input = (InputText) application.createComponent(InputText.COMPONENT_TYPE);

                input.setId(parameterName.replaceAll("\\s+", "") + "input");

                input.setValueExpression("value",
                        getExpressionFactory()
                        .createValueExpression(getELContext(), "#{dynamicColumnsTableController.values['"
                                + (parameterName.replaceAll("\\s+", "") + "input") + "']}", String.class));

                label.setFor(parameterName.replaceAll("\\s+", "") + "input");
                panelGrid.getChildren().add(label);

                panelGrid.getChildren().add(input);
            } else if (componentType.equalsIgnoreCase("DropDown")) {

                SelectOneMenu selectOneMenu = (SelectOneMenu) application.createComponent(SelectOneMenu.COMPONENT_TYPE);
                UISelectItems selectItems = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);

                selectOneMenu.setId(parameterName.replaceAll("\\s+", "") + "dropdown");

                selectItems.setId(parameterName.replaceAll("\\s+", "") + "dropdown" + "selectItems");

                List<SelectItem> items = new ArrayList<>();

                items.add(new SelectItem("", "Select..."));
                for (DropDown d : dropDowns) {

                    items.add(new SelectItem(d.getValue(), d.getLabel()));

                }

                selectItems.setValue(items);
                selectOneMenu.getChildren().add(selectItems);

                selectOneMenu.setValueExpression("value",
                        getExpressionFactory()
                        .createValueExpression(getELContext(), "#{dynamicColumnsTableController.values['"
                                + (parameterName.replaceAll("\\s+", "") + "dropdown") + "']}", String.class));

                label.setFor(parameterName.replaceAll("\\s+", "") + "dropdown");
                panelGrid.getChildren().add(label);

                panelGrid.getChildren().add(selectOneMenu);

            }

        }

        panelGroup.getChildren().add(panelGrid);

        CommandButton searchBtn = (CommandButton) application.createComponent(CommandButton.COMPONENT_TYPE);
        searchBtn.setId("searchBtnID");
        searchBtn.setValue("Search");
        searchBtn.setIcon("icon ui-icon-search");
        searchBtn.setAjax(false);

        ExpressionFactory ef = getApplication().getExpressionFactory();
        MethodExpression me = ef.createMethodExpression(getELContext(),
                "#{dynamicColumnsTableController.performSearch}", null, new Class[]{ActionEvent.class});
        MethodExpressionActionListener meal = new MethodExpressionActionListener(me);
        searchBtn.addActionListener(meal);
        searchBtn.setType("submit");

        panelGroup.getChildren().add(searchBtn);

        List<ReportColumn> repCols = setupController.getReport().getColumns();
        for (ReportColumn col : repCols) {
            expectedColumns.add(col.getOriginalName());
        }
        createDynamicColumns();
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public String performSearch() {

        Object[] keys = values.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            addSuccessMessage("Key" + i + ":  " + keys[i]);
            addSuccessMessage("Value" + i + ":  " + values.get(keys[i].toString()));
        }

        return "";
    }

    public void createDynamicColumns() {
        //String[] columnKeys = columnTemplate.split(" ");

        if (columns != null) {
            columns.clear();
        }

        for (String col : expectedColumns) {

            columns.add(new ColumnModel(col.toUpperCase(), col.toLowerCase()));

        }
    }

    public void removeColumn() {
        String columnToRemove = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap().get("columnToRemove");
        expectedColumns.remove(columnToRemove);
        createDynamicColumns();
    }

    public void updateColumns() {
        createDynamicColumns();
    }

    public HtmlPanelGroup getPanelGroup() {
        return panelGroup;
    }

    public void setPanelGroup(HtmlPanelGroup panelGroup) {
        this.panelGroup = panelGroup;
    }

    private String getRandomModel() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(final List<Message> messages) {
        this.messages = messages;
    }

    public static class ColumnModel implements Serializable {

        private String header;
        private String property;

        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }
    }

    public class Message implements Serializable {

        private String subject;
        private String text;
        private long time;
        private String textLength;
        private String country;
        private String deliveryStatus;

        public Message() {
            time = System.currentTimeMillis() + (long) (Math.random() * 10);
            textLength = Math.random() * 10 + "";
        }

        public final String getSubject() {
            return subject;
        }

        public final void setSubject(String subject) {
            this.subject = subject;
        }

        public final String getText() {
            return text;
        }

        public final void setText(String text) {
            this.text = text;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getTextLength() {
            return textLength;
        }

        public void setTextLength(String textLength) {
            this.textLength = textLength;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public void setDeliveryStatus(String deliveryStatus) {
            this.deliveryStatus = deliveryStatus;
        }
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

    private class Report {

        private final Map<String, String> properties = new HashMap<>();

        public String getProperty(String key) {
            return properties.get(key);
        }

        public void setProperty(String key, String value) {
            properties.put(key, value);
        }

    }

    public void addErrorMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        context.addMessage(msg, message);
    }

    public void addSuccessMessage(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        context.addMessage(msg, message);
    }

    public ReportSetupController getSetupController() {
        return setupController;
    }

    public void setSetupController(ReportSetupController setupController) {
        this.setupController = setupController;
    }

}
