/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import com.mahn42.framework.Framework;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nils
 */
public class CommandMessengerSend implements CommandExecutor {
 
    public CommandMessengerSend(MessageManager aMessageManager) {
        fMessageManager = aMessageManager;
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
                    if (fMessageManager.VerifyRecipient(lstr)) {
                        lRecipient = lstr.substring(1, lstr.length());
                    }
                    else if (fMessageManager.VerifyGroup(lstr.substring(1, lstr.length()))) {
                        lRecipientGrp = lstr.substring(1, lstr.length());
                    }
                    else {
                        lRecipient = "?";
                        lPlayer.sendMessage(ChatColor.RED.toString() + "No valid Recipient!");
                    }
                }
                else {
                    if (lMsg.length() == 0) {
                        lMsg = lstr;
                    }
                    else {
                        lMsg = lMsg + " " + lstr;
                    }
                }
            }
            com.mahn42.framework.Messenger lMessenger = Framework.plugin.getMessenger();
            if (lRecipientGrp.length() != 0) {
                lMessenger.sendGroupMessage(lPlayer.getName(), lRecipientGrp, lMsg);
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "Message sent to group " + lRecipientGrp + "...");
            }
            else if (lRecipient.equals("all")) {
                fMessageManager.SendMessageToAll(lPlayer.getName(), lMsg);
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "Message sent to all...");
            }
            else if (!lRecipient.equals("?")) {
                boolean lOK = lMessenger.sendPlayerMessage(lPlayer.getName(), lRecipient, lMsg);
                if (lOK) {
                    lPlayer.sendMessage(ChatColor.GREEN.toString() + "Message sent to " + lRecipient + "...");
                }
                else {
                    lPlayer.sendMessage(ChatColor.RED.toString() + "Message not sent to " + lRecipient + "!");
                }
            }
        }
        return true;
    }
       
    protected MessageManager fMessageManager;
        
}
