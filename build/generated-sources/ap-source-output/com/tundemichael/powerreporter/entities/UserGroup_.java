package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.ReportMenu;
import com.tundemichael.powerreporter.entities.ReportUser;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(UserGroup.class)
public class UserGroup_ { 

    public static volatile SingularAttribute<UserGroup, Long> id;
    public static volatile ListAttribute<UserGroup, ReportMenu> menus;
    public static volatile SingularAttribute<UserGroup, String> groupName;
    public static volatile ListAttribute<UserGroup, ReportUser> users;
    public static volatile SingularAttribute<UserGroup, String> createdBy;
    public static volatile SingularAttribute<UserGroup, String> description;
    public static volatile SingularAttribute<UserGroup, Date> dateCreated;

}