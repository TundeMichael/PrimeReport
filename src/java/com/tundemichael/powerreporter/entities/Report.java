package com.tundemichael.powerreporter.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author michael.orokola
 *
 */
@Entity
@Table(name = "report")
@XmlRootElement
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String reportName;
    private String reportDescription;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateCreated;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private ReportUser createdBy;
    @Column(length = 5000)
    private String reportQuery;
    private Boolean downloadable;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReportColumn> columns;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ReportParameter> reportParameters;
    @ManyToOne(fetch = FetchType.EAGER)
    private ConnectionProfile connectionProfile;
    private Boolean confidential;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ReportUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ReportUser createdBy) {
        this.createdBy = createdBy;
    }

    public String getReportQuery() {
        return reportQuery;
    }

    public void setReportQuery(String reportQuery) {
        this.reportQuery = reportQuery;
    }

    public Boolean getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public List<ReportColumn> getColumns() {

        List<ReportColumn> c1 = new ArrayList<>();
        if (columns != null && !columns.isEmpty()) {
            int marker = 1;
            ReportColumn found;
            do {
                found = this.findByPosition(marker, columns);
                marker++;
                if (found != null) {
                    c1.add(found);
                }
            } while (found != null);
        }
        if (c1.size() == columns.size()) {
            return c1;
        }
        return columns;
    }

    public void setColumns(List<ReportColumn> columns) {
        this.columns = columns;
    }

    public List<ReportParameter> getReportParameters() {
        return reportParameters;
    }

    public void setReportParameters(List<ReportParameter> reportParameters) {
        this.reportParameters = reportParameters;
    }

    public ConnectionProfile getConnectionProfile() {
        return connectionProfile;
    }

    public void setConnectionProfile(ConnectionProfile connectionProfile) {
        this.connectionProfile = connectionProfile;
    }

    public Boolean getConfidential() {
        return confidential;
    }

    public void setConfidential(Boolean confidential) {
        this.confidential = confidential;
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
        if (!(object instanceof Report)) {
            return false;
        }
        Report other = (Report) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.etranzact.powerreporter.beans.PowerReport[ id=" + id + " ]";
    }

    public ReportColumn findByPosition(int position, List<ReportColumn> cols) {

        for (ReportColumn r : cols) {
                if (r.getPosition() != null && r.getPosition() == position) {
                    return r;
                }
        }
        return null;
    }

}
