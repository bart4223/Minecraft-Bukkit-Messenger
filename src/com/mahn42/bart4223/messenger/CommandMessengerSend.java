/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import org.bukkit.ChatColor;
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
            String lRecipientGrp = "";
            Player lPlayer = (Player) aCommandSender;
            for (String lstr : aStrings) {
                if (lRecipient.length() == 0 && lRecipientGrp.length() == 0 ) {
                    if (Plugin.VerifyRecipient(lstr)) {
                        lRecipient = lstr.substring(1, lstr.length());
                    }
                    else if (Plugin.VerifyGroup(lstr.substring(1, lstr.length()))) {
                        lRecipientGrp = lstr.substring(1, lstr.length());
                    }
                    else {
                        lRecipient = "?";
                        lPlayer.sendMessage(ChatColor.RED.toString() + "No valid Recipient!");
                    }
                }
                else {
                    if (lMsg.length() == 0)
                        lMsg = lstr;
                    else
                        lMsg = lMsg + " " + lstr;
                }
            }
            if (lRecipientGrp.length() != 0) {
                SendMessageToGroup(lPlayer.getName(), lRecipientGrp, lMsg);
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "Message sent to group " + lRecipientGrp + "...");
                Plugin.saveDB();
            }
            else if (lRecipient.equals("all")) {
                SendMessageToAll(lPlayer.getName(), lMsg);
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "Message sent to all...");
                Plugin.saveDB();
            }
            else if (!lRecipient.equals("?")) {
                boolean lOK = SendMessage(lPlayer.getName(), lRecipient, lMsg);
                if (lOK) {
                    lPlayer.sendMessage(ChatColor.GREEN.toString() + "Message sent to " + lRecipient + "...");
                    Plugin.saveDB();
                }
                else
                    lPlayer.sendMessage(ChatColor.RED.toString() + "Message not sent to " + lRecipient + "!");
            }
        }
        return true;
    }
       
    protected boolean SendMessage(String aSender, String aRecipient, String aMessage) {
        if (!Plugin.getDB().MaxPlayerMessagesAchieved(aRecipient)) {
            MessageDBRecord lMes = new MessageDBRecord();
            lMes.Sender = aSender;
            lMes.Recipient = aRecipient;
            lMes.Text = aMessage;
            MessageDBSet lDB = Plugin.getDB();
            lDB.addRecord(lMes);
            return true;
        }
        else return false;
    }
    
    protected void SendMessageToAll(String aSender, String aMessage) {
        OfflinePlayer[] offPlayers = Plugin.getServer().getOfflinePlayers();
        for (OfflinePlayer lOffPlayer : offPlayers) {
            SendMessage(aSender, lOffPlayer.getName(), aMessage);
        }
    }

    protected void SendMessageToGroup(String aSender, String aGroup, String aMessage) {
        Group lGroup = Plugin.GetGroup(aGroup);
        for (GroupUser lGroupuser : lGroup.Users) {
            SendMessage(aSender, lGroupuser.Name, aMessage);
        }
    }
    
}
