/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonesoft.gen.data.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.*;

/**
 *
 * @author pete
 */
public class Engine implements Runnable {
    
    File script;
    String extension = "js";
    
    Engine(String scriptPath) {
        script = new File(scriptPath);        
        if(!script.exists())
            throw new IllegalArgumentException(String.format("file %s not found", script.toPath()));
        int index = script.getName().lastIndexOf(".");
        if(index != -1)
            extension = script.getName().substring(index + 1);
    }
    
    
    
    @Override
    public void run() {
        
        FileReader reader = null;
        try {
            reader = new FileReader(script);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension(extension);
        Compilable compilable = (Compilable) engine;
        ScriptContext context = engine.getContext();
        
        ProcessorObject gen = new ProcessorObject();
        
        context.setAttribute("gen", gen, ScriptContext.ENGINE_SCOPE);
        try {
            CompiledScript compiledScript = compilable.compile(reader);
            compiledScript.eval();
        } catch (ScriptException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        
        Thread thread = new Thread(new Engine("/home/pete/NetBeansProjects/gen/gen/Gen/test/com/leonesoft/gen/test/script/interface.js"));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
