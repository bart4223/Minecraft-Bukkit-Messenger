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
public class CommandMessengerListgroups implements CommandExecutor {

    public Messenger Plugin;
    
    public CommandMessengerListgroups(Messenger aPlugin) {
        Plugin = aPlugin;
    }

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            Player lPlayer = (Player) aCommandSender;
            lPlayer.sendMessage(ChatColor.GRAY.toString() + "Messenger Groups");
            for (Group lGrp : Plugin.Groups) {
                lPlayer.sendMessage(ChatColor.DARK_GRAY.toString() + lGrp.Name);
            }
        }
        return true;
    }
}
