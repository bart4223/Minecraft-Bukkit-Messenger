/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

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

   protected Messenger fMessenger;
    
   public PlayerListener(Messenger aThis) {
       fMessenger = aThis;
   }    
   
   @EventHandler
   public void playerLogin(PlayerJoinEvent event) {
     String lFromPlayer = "";
     Player lPlayer = event.getPlayer();
     MessageDBSet lDB = fMessenger.getDB();
     ArrayList<Message> lMsgs = lDB.getPlayerMessages(lPlayer.getName());
     for (Message lMsg : lMsgs) {
        if (!lFromPlayer.equals(lMsg.Sender)) {
            lPlayer.sendMessage(ChatColor.GRAY.toString() + "Message(s) from " + lMsg.Sender);
            lFromPlayer = lMsg.Sender;
        }
        if (lMsg.Text.startsWith("&")) {
            lPlayer.sendMessage(GetChatColor(lMsg.Text) + (lMsg.Text.substring(2)));
        }
        else lPlayer.sendMessage(lMsg.Text);
     }
     lDB.removePlayerMessages(lPlayer.getName());
     lDB.save();
   }

   protected ChatColor GetChatColor(String aMsg) {
     if (aMsg.startsWith("&1"))
     return ChatColor.BLUE;       
     else if (aMsg.startsWith("&2"))
     return ChatColor.RED;       
     else if (aMsg.startsWith("&3"))
     return ChatColor.GREEN;       
     else if (aMsg.startsWith("&4"))
     return ChatColor.YELLOW;       
     else if (aMsg.startsWith("&5"))
     return ChatColor.GOLD;       
     else if (aMsg.startsWith("&6"))
     return ChatColor.WHITE;       
     else if (aMsg.startsWith("&7"))
     return ChatColor.GRAY;       
     else if (aMsg.startsWith("&8"))
     return ChatColor.DARK_AQUA;       
     else if (aMsg.startsWith("&9"))
     return ChatColor.DARK_PURPLE;       
     else return ChatColor.WHITE;  
   }
   
}
