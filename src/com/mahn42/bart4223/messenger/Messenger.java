/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;


import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.World;
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
    
    @Override
    public void onEnable() {
      this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
      getCommand("messenger_send").setExecutor(new CommandMessengerSend(this));
      log = this.getLogger();
      getDB();
    }
    
    
    @Override
    public void onDisable() { 
      saveDB();
    }
 
    public void saveDB() { 
        if (fMessengerDB != null) {
            getLogger().info("Saving DB...");
            fMessengerDB.save();          
        }
    }
    
    protected MessageDBSet fMessengerDB;
 
    
    public MessageDBSet getDB() {
        if (fMessengerDB == null) {
            File lFolder = getDataFolder();
            if (!lFolder.exists()) {
                lFolder.mkdirs();
            }
            String lPath = lFolder.getPath();
            lPath = lPath + File.separatorChar + "messenger.csv";
            File lFile = new File(lPath);
            fMessengerDB = new MessageDBSet(lFile);
            fMessengerDB.load();
            getLogger().info("Datafile " + lFile.toString() + " loaded. (Records:" + new Integer(fMessengerDB.size()).toString() + ")");
        }
        return fMessengerDB;
    }    
}
