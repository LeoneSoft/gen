/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonesoft.gen.data.processor;

import com.leonesoft.gen.data.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author pete
 */
public class ProcessorObject {
    public static enum ProcessorEvent {
        START,
        NEXT_RECORD,
        END,
        COMPLETE,
        ERROR;
        
        private final static List<String> names;
        
        
        
        static {
            names = new ArrayList<String>();
            for(ProcessorEvent e : ProcessorEvent.values()) {
                names.add(e.name());
            }
        }
        
        public static boolean contains(String name) {
            return names.contains(name);
        }
        
    }
    
    private Map<String, DataSource> dataSources = new HashMap<String, DataSource>();
    
    private boolean fireEvent(ProcessorEvent event, Object args) {
       boolean result = true;
       if(handlerMap.containsKey(event)) {
           List<ProcessorEventHandler> handlers = handlerMap.get(event);
           for(ProcessorEventHandler handler : handlers) {
               handler.call(event, args);
           }
       }
        return result;
    }
       
    Map<ProcessorEvent, List<ProcessorEventHandler>> handlerMap = new HashMap<>();
    
    private List<ProcessorEventHandler> initialiseMap(ProcessorEvent event) {
        if(!(handlerMap.containsKey(event))) 
            handlerMap.put(event, new ArrayList<ProcessorEventHandler>());
        return handlerMap.get(event);
    }
    
    public ProcessorObject addEventListener(ProcessorEvent event, ProcessorEventHandler handler) {
        List<ProcessorEventHandler> handlers = initialiseMap(event);
        handlers.add(handler);
        return this;
    }
    
    public ProcessorObject addEventListener(String name, ProcessorEventHandler handler) {
        if(ProcessorEvent.contains(name)) {
            return addEventListener(ProcessorEvent.valueOf(name), handler);
        }
        throw new IllegalArgumentException(String.format("Event %s not supported", name));
    }
    
    public ProcessorObject addInputDataSource(String name, String connectionString, String query) {
        return addInputDataSource(name, connectionString, query, new Properties());
    }
        
    public ProcessorObject addInputDataSource(String name, String connectionString, String query, Properties properties) {
        DataSource source = new DataSource();
        source.setName(name);
        source.setConnectionString(connectionString);
        source.setProperties(properties);
        source.setDataQuery(query);
        dataSources.put(name, source);
        return this;
    }
      
    public void log(String message) {
        System.out.println(message);
    }
    
    public void run() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(DataSource source : dataSources.values()) {
                    
                    Connection connection = source.getConnection();
                    if(connection == null) {
                        String msg = String.format("Unable to create connection to '%s' DataSource", source.getName());
                        Logger.getLogger(ProcessorObject.class.getName()).log(Level.SEVERE, msg);
                        fireEvent(ProcessorEvent.ERROR, null);
                        
                        continue;
                    }
                    ResultSet results = null;
                    try {
                        Statement statement = connection.createStatement();
                        results = statement.executeQuery(source.getDataQuery());
                    } catch (SQLException ex) {
                        String msg = String.format("Error occurred creating resultset from '%s' for '%s'", source.getDataQuery(), source.getName());
                        Logger.getLogger(ProcessorObject.class.getName()).log(Level.SEVERE, msg, ex);
                    }
                    fireEvent(ProcessorEvent.START, null);
                    if(results == null) {
                        fireEvent(ProcessorEvent.ERROR, null);
                        continue;
                    }
                    try {
                        ResultSetMetaData metaData = results.getMetaData();
                        int count = metaData.getColumnCount();
                        Map<String, Object> list = new HashMap<>(count);
                        while(results.next()) {
                            
                            for(int i = 1; i <= count; i++) {
                                list.put(metaData.getColumnName(i), results.getObject(i));
                            }
                            fireEvent(ProcessorEvent.NEXT_RECORD, list);
                            list.clear();
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(ProcessorObject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    fireEvent(ProcessorEvent.END, null);
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(ProcessorObject.class.getName()).log(Level.SEVERE, null, ex);
                        fireEvent(ProcessorEvent.ERROR, null);
                    }
                    fireEvent(ProcessorEvent.COMPLETE, null);
                    
                }
            }
        });
        thread.start();
    }
}
