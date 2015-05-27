package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.Report;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(ConnectionProfile.class)
public class ConnectionProfile_ { 

    public static volatile SingularAttribute<ConnectionProfile, String> databaseType;
    public static volatile ListAttribute<ConnectionProfile, Report> reports;
    public static volatile SingularAttribute<ConnectionProfile, String> urlParamName;
    public static volatile SingularAttribute<ConnectionProfile, String> password;
    public static volatile SingularAttribute<ConnectionProfile, String> webMethod;
    public static volatile SingularAttribute<ConnectionProfile, Long> id;
    public static volatile SingularAttribute<ConnectionProfile, String> portNo;
    public static volatile SingularAttribute<ConnectionProfile, String> databaseName;
    public static volatile SingularAttribute<ConnectionProfile, String> username;
    public static volatile SingularAttribute<ConnectionProfile, String> profileType;
    public static volatile SingularAttribute<ConnectionProfile, String> webEndPoint;
    public static volatile SingularAttribute<ConnectionProfile, String> ipAdd;
    public static volatile SingularAttribute<ConnectionProfile, String> profileName;

}