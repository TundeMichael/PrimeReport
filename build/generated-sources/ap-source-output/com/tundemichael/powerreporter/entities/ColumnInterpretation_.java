package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.ReportColumn;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(ColumnInterpretation.class)
public class ColumnInterpretation_ { 

    public static volatile SingularAttribute<ColumnInterpretation, Long> id;
    public static volatile SingularAttribute<ColumnInterpretation, String> replacement;
    public static volatile SingularAttribute<ColumnInterpretation, String> expected;
    public static volatile SingularAttribute<ColumnInterpretation, ReportColumn> reportColumn;

}