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
    public boolean sendPlayerMessage(String aFromPlayer, String aToPlayerName, String aMessage) {
        return SendMessage(aFromPlayer, aToPlayerName, aMessage);
    }

    @Override
    public boolean sendGroupMessage(String aFromPlayer, String aToGroupName, String aMessage) {
        return SendMessageToGroup(aFromPlayer, aToGroupName, aMessage);
    }

    @Override
    public boolean recallPlayerMessages(String aFromPlayer, String aToPlayerName) {
        return removePlayerMessagesFrom(aFromPlayer, aToPlayerName);
    }

    @Override
    public boolean recallGroupMessages(String aFromPlayer, String aToGroupName) {
        return removePlayerMessagesFromGroup(aFromPlayer, aToGroupName);
    }
    
    @Override
    public boolean recallPlayerMessages(String aFromPlayer) {
        return removePlayerMessagesFrom(aFromPlayer);
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

    public boolean removePlayerMessagesFrom(String aSender) {
        boolean lOK = false;
        for (Iterator<MessageDBRecord> it = fMDB.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aSender.equals(lMsgDB.Sender)) {
                it.remove();
                lOK = true;
            }
        }
        return lOK;
    }

    public boolean removePlayerMessagesFrom(String aSender, String aRecipient) {
        boolean lOK = false;
        for (Iterator<MessageDBRecord> it = fMDB.iterator(); it.hasNext();) {
            MessageDBRecord lMsgDB = it.next();
            if (aSender.equals(lMsgDB.Sender) && aRecipient.equals(lMsgDB.Recipient)) {
                it.remove();
                lOK = true;
            }
        }
        return lOK;
    }   
    
    public boolean removePlayerMessagesFromGroup(String aSender, String aGroup) {
        boolean lOK = false;
        Group lGroup = GetGroup(aGroup);
        for (GroupUser lGroupuser : lGroup.Users) {
            for (Iterator<MessageDBRecord> it = fMDB.iterator(); it.hasNext();) {
                MessageDBRecord lMsgDB = it.next();
                if (aSender.equals(lMsgDB.Sender) && lGroupuser.Name.equals(lMsgDB.Recipient)) {
                    it.remove();
                lOK = true;
                }
            }
        }
        return lOK;
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
    
    public boolean SendMessageToAll(String aSender, String aMessage) {
        OfflinePlayer[] offPlayers = fPlugin.getServer().getOfflinePlayers();
        for (OfflinePlayer lOffPlayer : offPlayers) {
            SendMessage(aSender, lOffPlayer.getName(), aMessage);
        }
        return true;
    }

    public boolean SendMessageToGroup(String aSender, String aGroup, String aMessage) {
        Group lGroup = GetGroup(aGroup);
        for (GroupUser lGroupuser : lGroup.Users) {
            SendMessage(aSender, lGroupuser.Name, aMessage);
        }
        return true;
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
