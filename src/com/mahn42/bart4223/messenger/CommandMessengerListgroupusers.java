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
public class CommandMessengerListgroupusers implements CommandExecutor{

    public Messenger Plugin;
    
    public CommandMessengerListgroupusers(Messenger aPlugin) {
        Plugin = aPlugin;
    }

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        if (aCommandSender instanceof Player) {
            Player lPlayer = (Player) aCommandSender;
            Group lGroup = null;
            for (String lstr : aStrings) {
                if (lGroup == null && Plugin.VerifyGroup(lstr)) {
                    lGroup = Plugin.GetGroup(lstr);
                    lPlayer.sendMessage(ChatColor.GRAY.toString() + "Messenger Users for Group " + lstr);
                    for (GroupUser lGroupuser : lGroup.Users) {
                        lPlayer.sendMessage(ChatColor.DARK_GRAY.toString() + lGroupuser.Name);
                    }
                }
                else {
                    lPlayer.sendMessage(ChatColor.RED.toString() + "Messenger Group " + lstr + " unknown!");                    
                }
            }
        }
        return true;
    }

}
