/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import java.util.ArrayList;
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
   public void playerJoin(PlayerJoinEvent event) {
     String lFromPlayer = "";
     Player lPlayer = event.getPlayer();
     MessageDBSet lDB = fMessenger.getDB("world");
     ArrayList<Message> lMsgs = lDB.getPlayerMessages(lPlayer.getName());
     for (Message lMsg : lMsgs) {
        if (!lFromPlayer.equals(lMsg.Sender)) {
            lPlayer.sendMessage("Message(s) from " + lMsg.Sender);
            lFromPlayer = lMsg.Sender;
        }
        lPlayer.sendMessage(lMsg.Text);
     }
     lDB.removePlayerMessages(lPlayer.getName());
   }
   
}
