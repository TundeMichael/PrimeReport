
package com.tundemichael.powerreporter.entities;
         
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
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
@Table(name = "report_column")
@XmlRootElement
public class ReportColumn implements Serializable {  
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String originalName;
    private String prefferedName;   
    private Integer width;
    private boolean link;
    private String formatAs;
    private boolean dataMasked;
    private Integer leftDigit;
    private Integer rightDigit;
    private Integer position;
    @OneToMany(mappedBy = "reportColumn",cascade = CascadeType.ALL)
    private List<ColumnInterpretation> interpretations; 

    public ReportColumn() {
        
    }
    
    public ReportColumn(String originalName, String prefferedName) {
        this.originalName = originalName;
        this.prefferedName = prefferedName;
    }
    
    public ReportColumn(String originalName, String prefferedName,Integer position) {
        this.originalName = originalName;
        this.prefferedName = prefferedName;
        this.position = position;
    }

    public ReportColumn(String columnName, String prefferedName, int width, boolean link) {
        this.originalName = columnName;
        this.prefferedName = prefferedName;
        this.width = width;
        this.link = link;
    }

    public ReportColumn(String originalName, String prefferedName, int width, boolean link, String formatAs, boolean dataMasked) {
        this.originalName = originalName;
        this.prefferedName = prefferedName;
        this.width = width;
        this.link = link;
        this.formatAs = formatAs;
        this.dataMasked = dataMasked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getPrefferedName() {
        return prefferedName;
    }

    public void setPrefferedName(String prefferedName) {
        this.prefferedName = prefferedName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }


    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }

    public String getFormatAs() {
        return formatAs;
    }

    public void setFormatAs(String formatAs) {
        this.formatAs = formatAs;
    }

    public boolean isDataMasked() {
        return dataMasked;
    }

    public void setDataMasked(boolean dataMasked) {
        this.dataMasked = dataMasked;
    }
    
    public Integer getLeftDigit() {
        return leftDigit;
    }

    public void setLeftDigit(Integer leftDigit) {
        this.leftDigit = leftDigit;
    }

    public Integer getRightDigit() {
        return rightDigit;
    }

    public void setRightDigit(Integer rightDigit) {
        this.rightDigit = rightDigit;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @XmlTransient
    public List<ColumnInterpretation> getInterpretations() {
        return interpretations;
    }

    public void setInterpretations(List<ColumnInterpretation> interpretations) {
        this.interpretations = interpretations;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReportColumn other = (ReportColumn) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
