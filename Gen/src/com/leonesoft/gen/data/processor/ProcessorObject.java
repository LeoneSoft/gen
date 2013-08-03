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
    }
    
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
    };
    
    public ProcessorObject addEventListener(String name, ProcessorEventHandler handler) {
        return addEventListener(ProcessorEvent.valueOf(name), handler);
    }
}
