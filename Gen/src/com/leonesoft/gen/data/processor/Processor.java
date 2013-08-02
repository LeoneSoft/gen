/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leonesoft.gen.data.processor;

/**
 *
 * @author pete
 */
public interface Processor {
    boolean onInitialisation();
    boolean onStart();
    boolean onNextRecord();
    boolean onEnd();
    boolean onCompletion();
}
