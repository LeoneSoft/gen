/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonesoft.gen.data.processor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.AstRoot;

/**
 *
 * @author pete
 */
public class Engine implements Runnable {
    
    String path;
    
    Engine(String scriptPath) {
        path = scriptPath;
    }
    
    ProcessorObject gen = new ProcessorObject();
    
    @Override
    public void run() {
        FileInputStream stream = null;
        try {
            Context context = Context.enter();
            stream = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(stream);
            ScriptableObject scope = context.initStandardObjects();
            Object jsOut = Context.javaToJS(gen, scope);
            ScriptableObject.putProperty(scope, "gen", jsOut);
            Script script = context.compileReader(reader, path, 0, null);
            script.exec(context, scope);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(stream != null)
                    stream.close();
            } catch (IOException ex) {
                Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
            }
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
