/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import com.mahn42.framework.Framework;
import com.mahn42.framework.Messenger;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author Nils
 */
public class MessageManager implements Messenger{

    public int MaxMessages = 10;
    public ArrayList<Group> Groups;

    public MessageManager(com.mahn42.bart4223.messenger.Messenger aPlugin){
        fPlugin = aPlugin;
        fLog = fPlugin.getLogger();
        Groups = new ArrayList<Group>();
        InitMDB();
        Framework.plugin.registerMessenger(this);
        Framework.plugin.registerSaver(fMDB);
    }
        
    @Override
    public void sendPlayerMessage(String aFromPlayer, String aToPlayerName, String aMessage) {
        SendMessage(aFromPlayer, aToPlayerName, aMessage);
    }

    @Override
    public void sendGroupMessage(String aFromPlayer, String aToGroupName, String aMessage) {
        SendMessageToGroup(aFromPlayer, aToGroupName, aMessage);
    }

    @Override
    public void recallPlayerMessages(String aFromPlayer, String aToPlayerName) {
        removePlayerMessagesFrom(aFromPlayer, aToPlayerName);
    }

    @Override
    public void recallGroupMessages(String aFromPlayer, String aToGroupName) {
        removePlayerMessagesFromGroup(aFromPlayer, aToGroupName);
    }
    
    public boolean VerifyRecipient(String aRecipient) {
        boolean lOK = aRecipient.equals("@all");
        if (!lOK && aRecipient.startsWith("@")) {
            String lRecipient = aRecipient.substring(1, aRecipient.length());
            OfflinePlayer[] offPlayers = fPlugin.getServer().getOfflinePlayers();
            for (OfflinePlayer lOffPlayer : offPlayers) {
                lOK = (lOffPlayer.getName().equals(lRecipient));
                if (lOK) {
                    break;
                }  
            }
        }
      return lOK;
    }
    
    public boolean VerifyGroup(String aGroup) {
        boolean lOK = false;
        for (Group lGroup : Groups) {
            lOK = (lGroup.Name.equals(aGroup));
            if (lOK) {
                break;
            }  
        }
        return lOK;
    }

    public Group GetGroup(String aGroup) {
        Group lRes = null;
        for (Group lGroup : Groups) {
            if (lGroup.Name.equals(aGroup)) {
                lRes = lGroup;
                break;
            }
        }
        return lRes;
    }
    
    public ChatColor GetChatColor(String aMsg) {
        if (aMsg.startsWith("&1")) {
            return ChatColor.BLUE;       
        }       
        else if (aMsg.startsWith("&2")) {
            return ChatColor.RED;       
        }       
        else if (aMsg.startsWith("&3")) {
            return ChatColor.GREEN;       
        }       
        else if (aMsg.startsWith("&4")) {
            return ChatColor.YELLOW;       
        }       
        else if (aMsg.startsWith("&5")) {
            return ChatColor.GOLD;       
        }       
        else if (aMsg.startsWith("&6")) {
            return ChatColor.WHITE;       
        }       
        else if (aMsg.startsWith("&7")) {
            return ChatColor.GRAY;       
        }       
        else if (aMsg.startsWith("&8")) {
            return ChatColor.DARK_AQUA;       
        }       
        else if (aMsg.startsWith("&9")) {
            return ChatColor.DARK_PURPLE;
        }       
        else {
            return ChatColor.WHITE;
        }  
    }    
    
    public ArrayList<Message> getPlayerMessages(String aPlayerName){
        ArrayList<Message> lResult = new ArrayList<Message>();
        Iterator iterator = fMDB.iterator();
        while(iterator.hasNext()) {
            MessageDBRecord lMsgDB = (MessageDBRecord)iterator.next();
                if (aPlayerName.equals(lMsgDB.Recipient)) {
                    Message lMsg = new Message(lMsgDB.Sender, lMsgDB.Text, lMsgDB.Timestamp);
                    lResult.add(lMsg);
                }
            }
        Collections.sort( lResult );
        return lResult;
    }

    public void removePlayerMessages(String aRecipient) {
        for (Iterator<MessageDBRecord> it = fMDB.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aRecipient.equals(lMsgDB.Recipient)) {
                it.remove();
            }
        }
    }

    public void removePlayerMessagesFrom(String aSender) {
        for (Iterator<MessageDBRecord> it = fMDB.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aSender.equals(lMsgDB.Sender)) {
                it.remove();
            }
        }
    }

    public void removePlayerMessagesFrom(String aSender, String aRecipient) {
        for (Iterator<MessageDBRecord> it = fMDB.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aSender.equals(lMsgDB.Sender) && aRecipient.equals(lMsgDB.Recipient)) {
                it.remove();
            }
        }
    }   
    
    public void removePlayerMessagesFromGroup(String aSender, String aGroup) {
        Group lGroup = GetGroup(aGroup);
        for (GroupUser lGroupuser : lGroup.Users) {
            for (Iterator<MessageDBRecord> it = fMDB.iterator(); it.hasNext();) {
                MessageDBRecord lMsgDB = it.next();
                if (aSender.equals(lMsgDB.Sender) && lGroupuser.Name.equals(lMsgDB.Recipient)) {
                    it.remove();
                }
            }
        }
    }   

    public boolean SendMessage(String aSender, String aRecipient, String aMessage) {
        if (!MaxPlayerMessagesAchieved(aRecipient)) {
            MessageDBRecord lMes = new MessageDBRecord();
            lMes.Sender = aSender;
            lMes.Recipient = aRecipient;
            lMes.Text = aMessage;
            lMes.Timestamp = new Date().getTime();
            fMDB.addRecord(lMes);
            return true;
        }
        else {
            return false;
        }
    }
    
    public void SendMessageToAll(String aSender, String aMessage) {
        OfflinePlayer[] offPlayers = fPlugin.getServer().getOfflinePlayers();
        for (OfflinePlayer lOffPlayer : offPlayers) {
            SendMessage(aSender, lOffPlayer.getName(), aMessage);
        }
    }

    public void SendMessageToGroup(String aSender, String aGroup, String aMessage) {
        Group lGroup = GetGroup(aGroup);
        for (GroupUser lGroupuser : lGroup.Users) {
            SendMessage(aSender, lGroupuser.Name, aMessage);
        }
    }    
    
    protected com.mahn42.bart4223.messenger.Messenger fPlugin; 
    protected Logger fLog; 
    protected MessageDBSet fMDB;    
        
    protected void InitMDB() {
        if (fMDB == null) {
            File lFolder = fPlugin.getDataFolder();
            if (!lFolder.exists()) {
                lFolder.mkdirs();
            }
            String lPath = lFolder.getPath();
            lPath = lPath + File.separatorChar + "messenger.csv";
            File lFile = new File(lPath);
            fMDB = new MessageDBSet(lFile);
            fMDB.load();
            fLog.info("Datafile " + lFile.toString() + " loaded. (Records:" + new Integer(fMDB.size()).toString() + ")");
        }
    }    

    protected boolean MaxPlayerMessagesAchieved(String aPlayerName) {
        ArrayList<Message> lMsgs = getPlayerMessages(aPlayerName);
        return lMsgs.size() >= MaxMessages; 
    }
    
}
