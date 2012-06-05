/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;


import java.io.File;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Nils
 */

public class Messenger extends JavaPlugin{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    }
    
    public Logger log;   
    
    public int configMaxMessages = 10;
    
    @Override
    public void onEnable() {
      getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
      getCommand("messenger_send").setExecutor(new CommandMessengerSend(this));
      getCommand("messenger_recall").setExecutor(new CommandMessengerRecall(this));
      log = this.getLogger();
      readMessengerConfig();
      getDB();
    }
    
    @Override
    public void onDisable() { 
      saveDB();
    }
     
    public void saveDB() { 
        if (fMessengerDB != null) {
            log.info("Saving DB...");
            fMessengerDB.save();          
        }
    }
     
    public MessageDBSet getDB() {
        if (fMessengerDB == null) {
            File lFolder = getDataFolder();
            if (!lFolder.exists()) {
                lFolder.mkdirs();
            }
            String lPath = lFolder.getPath();
            lPath = lPath + File.separatorChar + "messenger.csv";
            File lFile = new File(lPath);
            fMessengerDB = new MessageDBSet(lFile,this);
            fMessengerDB.load();
            log.info("Datafile " + lFile.toString() + " loaded. (Records:" + new Integer(fMessengerDB.size()).toString() + ")");
        }
        return fMessengerDB;
    }    

    protected MessageDBSet fMessengerDB;
       
    protected void readMessengerConfig() {
        FileConfiguration lConfig = getConfig();
        configMaxMessages = lConfig.getInt("MaxMessages");
    }
 
    public boolean VerifyRecipient(String aRecipient) {
      boolean lOK = aRecipient.equals("@all");
      if (!lOK && aRecipient.startsWith("@")) {
        String lRecipient = aRecipient.substring(1, aRecipient.length());
        OfflinePlayer[] offPlayers = getServer().getOfflinePlayers();
        for (OfflinePlayer lOffPlayer : offPlayers) {
          lOK = (lOffPlayer.getName().equals(lRecipient));
          if (lOK) break;  
          }
      }
      return lOK;
    }
    
}
