
package com.tundemichael.powerreporter.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author michael.orokola
 * 
 */

@Entity
@Table(name = "connection_profile")
@XmlRootElement
public class ConnectionProfile implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String profileName;
    private String portNo;
    private String ipAdd;
    private String databaseType;
    private String databaseName;
    private String username;
    private String password;
    private String profileType; // database connection or web service endpoint
    private String webEndPoint; // rest endpoint
    private String webMethod; // post or get
    private String urlParamName; // i.e sql and datasourceName
    @OneToMany(mappedBy = "connectionProfile")
    private List<Report> reports;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getIpAdd() {
        return ipAdd;
    }

    public void setIpAdd(String ipAdd) {
        this.ipAdd = ipAdd;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }
    
    public String getUrl(){
    
        String dbType = "";

        if ("mysql".equals(getDatabaseType())) {
            dbType = "jdbc:mysql://";
        }
        
        if ("sybase".equals(getDatabaseType())) {
            dbType = "jdbc:sybase:Tds:";
        }
        
        String url = dbType + getIpAdd() + ":" + getPortNo() + "/" + getDatabaseName(); 
              
        return url;
    }

    public String getWebEndPoint() {
        return webEndPoint;
    }

    public void setWebEndPoint(String webEndPoint) {
        this.webEndPoint = webEndPoint;
    }

    public String getWebMethod() {
        return webMethod;
    }

    public void setWebMethod(String webMethod) {
        this.webMethod = webMethod;
    }

    public String getUrlParamName() {
        return urlParamName;
    }

    public void setUrlParamName(String urlParamName) {
        this.urlParamName = urlParamName;
    }

    @XmlTransient
    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
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
        if (!(object instanceof ConnectionProfile)) {
            return false;
        }
        ConnectionProfile other = (ConnectionProfile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tundemichael.powerreporter.entities.PowerReporterProfile[ id=" + id + " ]";
    }
    
}
