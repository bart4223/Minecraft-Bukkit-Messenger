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
 
    public Messenger Plugin;
    
    public CommandMessengerRecall(Messenger aPlugin) {
        Plugin = aPlugin;
    }
    
    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            String lRecipient = "";
            Player lPlayer = (Player) aCommandSender;
            for (String lstr : aStrings) {
                if (lRecipient.length() == 0) {
                    if (Plugin.VerifyRecipient(lstr)) {
                        lRecipient = lstr.substring(1, lstr.length());
                    }
                    else {
                        lRecipient = "?";
                        lPlayer.sendMessage(ChatColor.RED.toString() + "No valid Recipient!");
                    }
                }
                else break;
            }
            if (lRecipient.equals("all")) {
                Plugin.getDB().removePlayerMessagesFrom(lPlayer.getName());
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "All Messages recalled...");
                Plugin.saveDB();
            }
            else if (!lRecipient.equals("?")) {
                Plugin.getDB().removePlayerMessagesFrom(lPlayer.getName(),lRecipient);
                lPlayer.sendMessage(ChatColor.GREEN.toString() + "All Messages to " + lRecipient + " recalled...");
                Plugin.saveDB();
            }
        }
        return true;
    }
    
}
