package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.ConnectionProfile;
import com.tundemichael.powerreporter.entities.ReportColumn;
import com.tundemichael.powerreporter.entities.ReportParameter;
import com.tundemichael.powerreporter.entities.ReportUser;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(Report.class)
public class Report_ { 

    public static volatile SingularAttribute<Report, Long> id;
    public static volatile SingularAttribute<Report, ReportUser> createdBy;
    public static volatile SingularAttribute<Report, String> reportDescription;
    public static volatile ListAttribute<Report, ReportColumn> columns;
    public static volatile SingularAttribute<Report, Date> dateCreated;
    public static volatile ListAttribute<Report, ReportParameter> reportParameters;
    public static volatile SingularAttribute<Report, String> reportQuery;
    public static volatile SingularAttribute<Report, ConnectionProfile> connectionProfile;
    public static volatile SingularAttribute<Report, String> reportName;
    public static volatile SingularAttribute<Report, Boolean> downloadable;
    public static volatile SingularAttribute<Report, Boolean> confidential;

}