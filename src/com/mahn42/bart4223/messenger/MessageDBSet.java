/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import com.mahn42.framework.DBSet;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.bukkit.World;

/**
 *
 * @author Nils
 */
public class MessageDBSet extends DBSet {

    protected World fWorld;
    
    public MessageDBSet(World aWorld, File aFile) {
        super(MessageDBRecord.class, aFile);
        fWorld = aWorld;
    }
    
    public ArrayList<Message> getPlayerMessages(String aPlayerName){
        ArrayList<Message> lResult = new ArrayList<Message>();
        Iterator iterator = iterator();
        while(iterator.hasNext()) {
            MessageDBRecord lMsgDB = (MessageDBRecord)iterator.next();
                if (aPlayerName.equals(lMsgDB.Recipient)) {
                    Message lMsg = new Message(lMsgDB.Sender, lMsgDB.Text);
                    lResult.add(lMsg);
                }
            }
        Collections.sort( lResult );
        return lResult;
    }
    
    public void removePlayerMessages(String aPlayerName) {
        for (Iterator<MessageDBRecord> it = this.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aPlayerName.equals(lMsgDB.Recipient)) {
                it.remove();
            }
        }
    }
    
}
