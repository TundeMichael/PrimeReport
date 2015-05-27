
package com.tundemichael.powerreporter.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This class serves 2 functions:
 * 1. Keep properties of d components to be rendered 
 * 2. Keeps properties of d parameters to include in query 
 * @author michael.orokola
 * 
 */

@Entity
@Table(name = "report_parameter")
public class ReportParameter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    private String componentType;  // e.g Textfield, Calendar, Dropdown, Radio botton
    private String queryPosition; // d position of this parameter in d query e. @p1 or @startDate
    @OneToMany(cascade = CascadeType.ALL)
    private List<DropDown> dropDowns; // if its a dropdown, this holds d 'itemLabel' & 'itemValue' values
    private boolean required; // wether d compement is required, and secondly the SQL will not be run without it
    private boolean userProperty; // wether d param is to be retrieved from d logged-in user
    private String userPropertyName; // d logged-in user ppty name
    private String dataType; // helps tell if d param is to be quoted or not. E.g string = 'name', Integer = 1
    private String sqlDatePattern; // date sqlDatePattern e. 2014-02-02 in d SQL 
    private String calendarPattern; // Calendar component pattern
    private boolean appParameter;
    private String appParameterName;
    
    public ReportParameter() {
        
    }

    public ReportParameter(String label, String componentType) {
        this.label = label;
        this.componentType = componentType;
    }

    public ReportParameter(String label, String dataType, List<DropDown> dropDowns) {
        this.label = label;
        this.componentType = dataType;
        this.dropDowns = dropDowns;
    }

    public ReportParameter(String label, String componentType, String queryPosition, boolean required, String dataType) {
        this.label = label;
        this.componentType = componentType;
        this.queryPosition = queryPosition;
        this.required = required;
        this.dataType = dataType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * OutputLabel value.
     * @return label
     */
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public List<DropDown> getDropDowns() {
        return dropDowns;
    }

    public void setDropDowns(List<DropDown> dropDowns) {
        this.dropDowns = dropDowns;
    }

    public String getQueryPosition() {
        return queryPosition;
    }

    public void setQueryPosition(String queryPosition) {
        this.queryPosition = queryPosition;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isUserProperty() {
        return userProperty;
    }

    public void setUserProperty(boolean userProperty) {
        this.userProperty = userProperty;
    }

    public String getUserPropertyName() {
        return userPropertyName;
    }

    public void setUserPropertyName(String userPropertyName) {
        this.userPropertyName = userPropertyName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSqlDatePattern() {
        return sqlDatePattern;
    }

    public void setSqlDatePattern(String sqlDatePattern) {
        this.sqlDatePattern = sqlDatePattern;
    }

    public String getCalendarPattern() {
        return calendarPattern;
    }

    public void setCalendarPattern(String calendarPattern) {
        this.calendarPattern = calendarPattern;
    }

    public boolean isAppParameter() {
        return appParameter;
    }

    public void setAppParameter(boolean appParameter) {
        this.appParameter = appParameter;
    }

    public String getAppParameterName() {
        return appParameterName;
    }

    public void setAppParameterName(String appParameterName) {
        this.appParameterName = appParameterName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReportParameter other = (ReportParameter) obj;
        if (!Objects.equals(this.queryPosition, other.queryPosition)) {
            return false;
        }
        return true;
    }

}
