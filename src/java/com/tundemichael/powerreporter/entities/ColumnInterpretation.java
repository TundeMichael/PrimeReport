package com.tundemichael.powerreporter.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author michael.orokola
 */
@Entity
@Table(name = "column_interpretation")
public class ColumnInterpretation implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String expected;
    private String replacement;
    @ManyToOne(optional = false)
    private ReportColumn reportColumn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public ReportColumn getReportColumn() {
        return reportColumn;
    }

    public void setReportColumn(ReportColumn reportColumn) {
        this.reportColumn = reportColumn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ColumnInterpretation)) {
            return false;
        }
        ColumnInterpretation other = (ColumnInterpretation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tundemichael.powerreporter.entities.ColumnInterpretation[ id=" + id + " ]";
    }
    
}
