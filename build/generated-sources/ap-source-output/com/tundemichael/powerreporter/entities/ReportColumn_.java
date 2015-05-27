package com.tundemichael.powerreporter.entities;

import com.tundemichael.powerreporter.entities.ColumnInterpretation;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-01-12T11:36:24")
@StaticMetamodel(ReportColumn.class)
public class ReportColumn_ { 

    public static volatile SingularAttribute<ReportColumn, Long> id;
    public static volatile SingularAttribute<ReportColumn, Integer> leftDigit;
    public static volatile SingularAttribute<ReportColumn, Integer> position;
    public static volatile SingularAttribute<ReportColumn, String> formatAs;
    public static volatile SingularAttribute<ReportColumn, Boolean> dataMasked;
    public static volatile SingularAttribute<ReportColumn, Integer> width;
    public static volatile SingularAttribute<ReportColumn, Boolean> link;
    public static volatile ListAttribute<ReportColumn, ColumnInterpretation> interpretations;
    public static volatile SingularAttribute<ReportColumn, String> originalName;
    public static volatile SingularAttribute<ReportColumn, String> prefferedName;
    public static volatile SingularAttribute<ReportColumn, Integer> rightDigit;

}