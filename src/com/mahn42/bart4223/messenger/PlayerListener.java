/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author Nils
 */
public class PlayerListener implements Listener{

   public PlayerListener(MessageManager aMessageManager) {
       fMessageManager = aMessageManager;
   }    
   
   @EventHandler
   public void playerLogin(PlayerJoinEvent event) {
     String lFromPlayer = "";
     Player lPlayer = event.getPlayer();
     SimpleDateFormat lSdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
     ArrayList<Message> lMsgs = fMessageManager.getPlayerMessages(lPlayer.getName());
     for (Message lMsg : lMsgs) {
        if (!lFromPlayer.equals(lMsg.Sender)) {
            lPlayer.sendMessage(ChatColor.GRAY.toString() + "Message(s) from " + lMsg.Sender);
            lFromPlayer = lMsg.Sender;
        }
        lPlayer.sendMessage(ChatColor.DARK_GRAY.toString() + lSdf.format(lMsg.Timestamp));
        if (lMsg.Text.startsWith("&")) {
            lPlayer.sendMessage(fMessageManager.GetChatColor(lMsg.Text) + (lMsg.Text.substring(2)));
        }
        else {
            lPlayer.sendMessage(lMsg.Text);
        }
     }
     fMessageManager.removePlayerMessages(lPlayer.getName());
   }

   protected MessageManager fMessageManager;
       
}
