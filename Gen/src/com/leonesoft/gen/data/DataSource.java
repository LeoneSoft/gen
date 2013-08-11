/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonesoft.gen.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pete
 */
public class DataSource {
    
    private String name;
    private String description;
    private String connectionString;
    private String dataQuery;

    public String getDataQuery() {
        return dataQuery;
    }

    public void setDataQuery(String dataQuery) {
        this.dataQuery = dataQuery;
    }
    private Properties properties;
    
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(connectionString, properties);
        } catch (SQLException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public Properties getProperties() {
        return new Properties(properties);
    }

    public void setProperties(Properties properties) {
        this.properties = new Properties(properties);
    }
}
