/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nils
 */
public class CommandMessengerSend implements CommandExecutor {
 
    public Messenger Plugin;
    
    public CommandMessengerSend(Messenger aPlugin) {
        Plugin = aPlugin;
    }

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            String lMsg = "";
            String lRecipient = "";
            Player lPlayer = (Player) aCommandSender;
            for (String lstr : aStrings) {
                if (lRecipient.length() == 0) {
                    if (VerifyRecipient(lstr)) {
                        lRecipient = lstr.substring(1, lstr.length());
                    }
                    else {
                        lRecipient = "?";
                        lPlayer.sendMessage("No valid Recipient!");
                
                    }
                }
                else {
                    if (lMsg.length() == 0)
                        lMsg = lstr;
                    else
                        lMsg = lMsg + " " + lstr;
                }
            }
            if (lRecipient.equals("all")) {
                SendMessageToAll(lPlayer.getName(), lMsg);
                lPlayer.sendMessage("Message sent to all...");
            }
            else if (!lRecipient.equals("?")) {
                SendMessage(lPlayer.getName(), lRecipient, lMsg);
                lPlayer.sendMessage("Message sent to " + lRecipient + "...");
            }
        }
        return true;
    }
    
    protected boolean VerifyRecipient(String aRecipient) {
        boolean lOK = aRecipient.equals("@all");
        if (!lOK && aRecipient.startsWith("@")) {
            String lRecipient = aRecipient.substring(1, aRecipient.length());
            OfflinePlayer[] offPlayers = Plugin.getServer().getOfflinePlayers();
            for (OfflinePlayer lOffPlayer : offPlayers) {
                lOK = (lOffPlayer.getName().equals(lRecipient));
                if (lOK) break;  
            }
        }
        return lOK;
    }
    
    protected void SendMessage(String aSender, String aRecipient, String aMessage) {
        MessageDBRecord lMes = new MessageDBRecord();
        lMes.Sender = aSender;
        lMes.Recipient = aRecipient;
        lMes.Text = aMessage;
        MessageDBSet lDB = Plugin.getDB();
        lDB.addRecord(lMes);
    }
    
    protected void SendMessageToAll(String aSender, String aMessage) {
        OfflinePlayer[] offPlayers = Plugin.getServer().getOfflinePlayers();
        for (OfflinePlayer lOffPlayer : offPlayers) {
            SendMessage(aSender, lOffPlayer.getName(), aMessage);
        }
    }

}
