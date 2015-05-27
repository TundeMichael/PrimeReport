package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.ReportUser;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(UserProperty.class)
public class UserProperty_ { 

    public static volatile SingularAttribute<UserProperty, Long> id;
    public static volatile SingularAttribute<UserProperty, String> propertyName;
    public static volatile SingularAttribute<UserProperty, ReportUser> reportUser;
    public static volatile SingularAttribute<UserProperty, String> propertyValue;

}