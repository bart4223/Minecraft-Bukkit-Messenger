/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import com.mahn42.framework.DBSet;
import java.io.File;

/**
 *
 * @author Nils
 */
public class MessageDBSet extends DBSet {

    public MessageDBSet(File aFile) {
        super(MessageDBRecord.class, aFile);
    }
                
}
