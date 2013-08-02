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
    
    @Override
    public void run() {
        FileInputStream stream = null;
        try {
            Parser parser = new Parser();
            stream = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(stream);
            AstRoot root = parser.parse(reader, path, 0);
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(Engine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        
    }
}
