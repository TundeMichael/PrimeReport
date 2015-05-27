package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.Report;
import com.tundemichael.powerreporter.entities.ReportMenu;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(ReportSubMenu.class)
public class ReportSubMenu_ { 

    public static volatile SingularAttribute<ReportSubMenu, Long> id;
    public static volatile ListAttribute<ReportSubMenu, Report> reports;
    public static volatile SingularAttribute<ReportSubMenu, String> createdBy;
    public static volatile SingularAttribute<ReportSubMenu, String> description;
    public static volatile SingularAttribute<ReportSubMenu, String> name;
    public static volatile SingularAttribute<ReportSubMenu, ReportMenu> menu;
    public static volatile SingularAttribute<ReportSubMenu, Date> dateCreated;

}