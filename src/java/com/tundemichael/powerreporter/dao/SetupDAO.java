package com.tundemichael.powerreporter.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tundemichael.powerreporter.entities.ReportColumn;  
import com.tundemichael.powerreporter.util.DataObject;
import com.tundemichael.powerreporter.entities.ConnectionProfile;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author michael.orokola
 *
 */
public class SetupDAO implements Serializable {

    private String url;
    private String username;
    private String password;
    private String databaseName;

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    List<String> columnTypes;

    public SetupDAO(String url, String username, String password, String databaseName) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
    }

    public List<String> getDBTables() throws SQLException {

        List<String> tableNames = new ArrayList<>();

        statement = connection
                .prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA='" + databaseName + "'");
        resultSet = statement
                .executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA='" + databaseName + "'");

        while (resultSet.next()) {
            String table = resultSet.getString("table_name");
            tableNames.add(table);
        }

        return tableNames;
    }

    public List<String> getDBTables(ConnectionProfile profile) throws SQLException, ClassNotFoundException {

        String dbType = "";

        if ("mysql".equals(profile.getDatabaseType())) {
            dbType = "jdbc:mysql://";
        }
        if ("sybase".equals(profile.getDatabaseType())) {
            dbType = "jdbc:sybase:Tds:";
        }

        this.url = dbType + profile.getIpAdd() + ":" + profile.getPortNo() + "/"
                + profile.getDatabaseName();
        this.username = profile.getUsername();
        this.password = profile.getPassword();
        this.databaseName = profile.getDatabaseName();
        System.out.println(" URL ===== " + url);
        open();

        List<String> tableNames = new ArrayList<>();

        statement = connection
                .prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA='" + databaseName + "'");
        resultSet = statement
                .executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA='" + databaseName + "'");

        while (resultSet.next()) {
            String table = resultSet.getString("table_name");
            tableNames.add(table);
        }

        return tableNames;
    }

    public List<ReportColumn> getTableColumns(String table_name) throws SQLException {

        List<ReportColumn> columns = new ArrayList<>();
        columnTypes = new ArrayList<>();

        statement = connection.prepareStatement("SHOW COLUMNS FROM " + table_name);
        resultSet = statement.executeQuery();
        ResultSetMetaData rsmd = resultSet.getMetaData();

        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String col = rsmd.getColumnName(i);
            System.out.println("Column" + i + ": " + col);
        }

        while (resultSet.next()) {
            String col = resultSet.getString(1);
            String type = resultSet.getString(2);
            columns.add(new ReportColumn(col, type));
        }

        return columns;
    }

    public void open() throws SQLException, ClassNotFoundException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public void close() throws SQLException {

        try {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (Exception e) {
            System.out.println(" Error closing connection:  " + e.getMessage());
        }

    }

    public List<ReportColumn> getQueryColumns(String query) throws SQLException {

        List<ReportColumn> columns = new ArrayList<>();
        columnTypes = new ArrayList<>();

        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        ResultSetMetaData rsmd = resultSet.getMetaData();

        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String col = rsmd.getColumnName(i);
            columns.add(new ReportColumn(col, col, i));
        }

        return columns;
    }

    public List<String[]> getQueryData(String query) throws SQLException {

        List<String[]> data = new ArrayList<>();
        String[] dat;

        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        while (resultSet.next()) {
            dat = new String[rsmd.getColumnCount()];
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                dat[i] = resultSet.getString(1 + i);
            }
            data.add(dat);
        }

        return data;
    }

    public JsonArray getReport(String sql) throws SQLException, ClassNotFoundException {

            //this.openRequestConnection(bankCode);
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();

        ResultSetMetaData rsmd = resultSet.getMetaData();
        int colCount = rsmd.getColumnCount();

        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();

        List<String> colNames = new ArrayList<>();
        for (int i = 1; i <= colCount; i++) {
            String col = rsmd.getColumnName(i);
            colNames.add(col);
            object.addProperty("col" + i, col);
        }
        //array.add(object);

        while (resultSet.next()) {
            object = new JsonObject();
            for (int i = 1; i <= colCount; i++) {
                String data = resultSet.getString(i);
                object.addProperty(colNames.get(i - 1), data);
            }
            array.add(object);
        }

            //close();
        return array;

    }

    public String saveConnectionProfile(ConnectionProfile profile) {

        return "";
    }

    public List<TreeNode> getTableNodes(ConnectionProfile profile) throws SQLException, ClassNotFoundException {

        TreeNode root = new DefaultTreeNode(new DataObject("Files", "-", "Folder"), null);
        TreeNode documents = new DefaultTreeNode(new DataObject("Documents", "-", "Folder"), root);
        TreeNode pictures = new DefaultTreeNode(new DataObject("Pictures", "-", "Folder"), root);
        TreeNode movies = new DefaultTreeNode(new DataObject("Movies", "-", "Folder"), root);

        TreeNode work = new DefaultTreeNode(new DataObject("Work", "-", "Folder"), documents);
        TreeNode primefaces = new DefaultTreeNode(new DataObject("PrimeFaces", "-", "Folder"), documents);

        List<ReportColumn> tableColumns;

        List<String> tables = getDBTables(profile);
        for (String tab : tables) {
            tableColumns = getTableColumns(tab);
        }

        return null;
    }

    public String getMessage(String code, String msg) {

        JsonObject o = new JsonObject();
        o.addProperty("code", code);
        o.addProperty("message", msg);

        return o.toString();
    }

}
