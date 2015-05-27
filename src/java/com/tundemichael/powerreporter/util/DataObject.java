
package com.tundemichael.powerreporter.util;

import java.io.Serializable;

/**
 *
 * @author michael.orokola
 */
public class DataObject implements Serializable{
    
    private String name;
     
    private String dataType;
     
    private String type;
     
    public DataObject(String name, String dataType, String type) {
        this.name = name;
        this.dataType = dataType;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
