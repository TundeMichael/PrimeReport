package com.tundemichael.powerreporter.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author michael.orokola
 */
@ManagedBean   // NOT USED IN THIS PROJECT
@ViewScoped 
public class TableBean implements Serializable {

    private List<ColumnModel> columns;
    private List<People> people;
    private String columnName;

    public TableBean() {
        createDynamicColumns();
        addPeople();
    }

    @PostConstruct
    public void init() {
        createDynamicColumns();
        addPeople();
    }

    private void addPeople() {
        people = new ArrayList<>();

        People w1 = new People("Homer Simpson", "The father", 48);
        People w2 = new People("Marge Simpson", "The mother", 46);
        People w3 = new People("Bart Simpson", "Oldest child", 11);
        People w4 = new People("Lisa Simpson", "Sister of Bart", 8);

        people.add(w1);
        people.add(w2);
        people.add(w3);
        people.add(w4);
    }

    private void createDynamicColumns() {

        columns = new ArrayList<>();
        columns.add(new ColumnModel("Name", "name"));
        columns.add(new ColumnModel("Role", "role"));
        columns.add(new ColumnModel("Age", "age"));
    }

    public List<People> getPeople() {
        return people;
    }

    public void setPeople(List<People> people) {
        this.people = people;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    static public class ColumnModel implements Serializable {

        private String header;
        private String property;

        public ColumnModel(String header, String property) {
            this.header = header;
            this.property = property;
        }

        public String getHeader() {
            return header;
        }

        public String getProperty() {
            return property;
        }
    }

    private static class People {

        private String name;
        private String role;
        private int age;

        public People(String name, String role, int age) {
            this.name = name;
            this.role = role;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }
}
