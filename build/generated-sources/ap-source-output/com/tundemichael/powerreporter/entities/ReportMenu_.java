package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.Report;
import com.tundemichael.powerreporter.entities.ReportSubMenu;
import com.tundemichael.powerreporter.entities.UserGroup;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(ReportMenu.class)
public class ReportMenu_ { 

    public static volatile SingularAttribute<ReportMenu, Long> id;
    public static volatile ListAttribute<ReportMenu, Report> reports;
    public static volatile SingularAttribute<ReportMenu, String> createdBy;
    public static volatile SingularAttribute<ReportMenu, String> description;
    public static volatile SingularAttribute<ReportMenu, String> name;
    public static volatile ListAttribute<ReportMenu, ReportSubMenu> reportSubMenus;
    public static volatile SingularAttribute<ReportMenu, UserGroup> userGroup;
    public static volatile SingularAttribute<ReportMenu, Date> dateCreated;

}