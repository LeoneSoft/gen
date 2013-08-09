/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonesoft.gen.data.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pete
 */
public class ProcessorObject {
    public enum ProcessorEvent {
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
    
    private Map<String, Object> dataSources = new HashMap<String, Object>();
    
    private boolean fireEvent(ProcessorEvent event, Object[] args) {
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
    
    public ProcessorObject addInputDataSource(String name, String connectionString) {
        return this;
    }
      
    public void log(String message) {
        System.out.println(message);
    }
    
    public void run() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fireEvent(ProcessorEvent.START, null);
                fireEvent(ProcessorEvent.NEXT_RECORD, null);
                fireEvent(ProcessorEvent.END, null);
                fireEvent(ProcessorEvent.COMPLETE, null);
                fireEvent(ProcessorEvent.ERROR, null);
            }
        });
        thread.start();
    }
}
