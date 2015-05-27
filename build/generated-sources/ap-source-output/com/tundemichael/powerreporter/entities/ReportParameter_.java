package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.DropDown;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(ReportParameter.class)
public class ReportParameter_ { 

    public static volatile SingularAttribute<ReportParameter, Long> id;
    public static volatile SingularAttribute<ReportParameter, String> sqlDatePattern;
    public static volatile SingularAttribute<ReportParameter, String> dataType;
    public static volatile SingularAttribute<ReportParameter, String> queryPosition;
    public static volatile SingularAttribute<ReportParameter, String> calendarPattern;
    public static volatile SingularAttribute<ReportParameter, String> userPropertyName;
    public static volatile SingularAttribute<ReportParameter, Boolean> userProperty;
    public static volatile SingularAttribute<ReportParameter, Boolean> appParameter;
    public static volatile SingularAttribute<ReportParameter, String> appParameterName;
    public static volatile SingularAttribute<ReportParameter, String> label;
    public static volatile ListAttribute<ReportParameter, DropDown> dropDowns;
    public static volatile SingularAttribute<ReportParameter, Boolean> required;
    public static volatile SingularAttribute<ReportParameter, String> componentType;

}