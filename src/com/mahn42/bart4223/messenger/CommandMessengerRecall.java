/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Nils
 */
public class CommandMessengerRecall implements CommandExecutor {
 
    public CommandMessengerRecall(MessageManager aMessageManager) {
        fMessageManager = aMessageManager;
    }
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
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
                    break;
                }
            }
            if (lRecipientGrp.length() != 0) {
                fMessageManager.removePlayerMessagesFrom(lPlayer.getName(), lRecipientGrp);
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "Messages recalled from group " + lRecipientGrp + "...");
            }
            else if (lRecipient.equals("all")) {
                fMessageManager.removePlayerMessagesFrom(lPlayer.getName());
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "All Messages recalled...");
            }
            else if (!lRecipient.equals("?")) {
                fMessageManager.removePlayerMessagesFrom(lPlayer.getName(),lRecipient);
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "All Messages to " + lRecipient + " recalled...");
            }
        }
        return true;
    }
    
    protected MessageManager fMessageManager;
    
}
