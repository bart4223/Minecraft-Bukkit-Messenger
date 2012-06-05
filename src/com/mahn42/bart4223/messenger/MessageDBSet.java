/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import com.mahn42.framework.DBRecord;
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

   protected Messenger fPlugin;
    
    public MessageDBSet(File aFile, Messenger aPlugin) {
        super(MessageDBRecord.class, aFile);
        fPlugin = aPlugin;
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
    
    public void removePlayerMessages(String aRecipient) {
        for (Iterator<MessageDBRecord> it = this.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aRecipient.equals(lMsgDB.Recipient)) {
                it.remove();
            }
        }
    }
    
    public void removePlayerMessagesFrom(String aSender) {
        for (Iterator<MessageDBRecord> it = this.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aSender.equals(lMsgDB.Sender)) {
                it.remove();
            }
        }
    }

    public void removePlayerMessagesFrom(String aSender, String aRecipient) {
        for (Iterator<MessageDBRecord> it = this.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aSender.equals(lMsgDB.Sender) && aRecipient.equals(lMsgDB.Recipient)) {
                it.remove();
            }
        }
    }

    public boolean MaxPlayerMessagesAchieved(String aPlayerName) {
        ArrayList<Message> lMsgs = getPlayerMessages(aPlayerName);
        return lMsgs.size() >= fPlugin.configMaxMessages; 
    }
    
}
