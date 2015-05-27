package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.Report;
import com.tundemichael.powerreporter.entities.UserGroup;
import com.tundemichael.powerreporter.entities.UserProperty;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(ReportUser.class)
public class ReportUser_ { 

    public static volatile SingularAttribute<ReportUser, Long> id;
    public static volatile ListAttribute<ReportUser, Report> reports;
    public static volatile SingularAttribute<ReportUser, String> username;
    public static volatile SingularAttribute<ReportUser, String> defaultPassword;
    public static volatile SingularAttribute<ReportUser, String> phone;
    public static volatile SingularAttribute<ReportUser, String> email;
    public static volatile SingularAttribute<ReportUser, UserGroup> userGroup;
    public static volatile SingularAttribute<ReportUser, Date> regDate;
    public static volatile SingularAttribute<ReportUser, String> systemRole;
    public static volatile SingularAttribute<ReportUser, String> surname;
    public static volatile SingularAttribute<ReportUser, Boolean> blocked;
    public static volatile SingularAttribute<ReportUser, String> firstname;
    public static volatile ListAttribute<ReportUser, UserProperty> properties;

}