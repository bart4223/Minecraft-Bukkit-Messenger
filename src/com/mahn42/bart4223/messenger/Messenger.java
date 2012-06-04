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
      getDB("world");
    }
    
    
    @Override
    public void onDisable() { 
      saveDB();
    }
 
    public void saveDB() { 
        if (fMessengerDB != null) {
            getLogger().info("Saving DB...");
            for(MessageDBSet lDB : fMessengerDB.values()) {
                lDB.save();
            }
        }
    }
    
    protected HashMap<String, MessageDBSet> fMessengerDB;
 
    
    public MessageDBSet getDB(String aWorldName) {
        if (fMessengerDB == null) {
            fMessengerDB = new HashMap<String, MessageDBSet>();
        }
        if (!fMessengerDB.containsKey(aWorldName)) {
            //World lWorld = getServer().getWorld(aWorldName);
            File lFolder = getDataFolder();
            if (!lFolder.exists()) {
                lFolder.mkdirs();
            }
            String lPath = lFolder.getPath();
            lPath = lPath + File.separatorChar + "messenger.csv";
            File lFile = new File(lPath);
            MessageDBSet lDB = new MessageDBSet(lFile);
            lDB.load();
            getLogger().info("Datafile " + lFile.toString() + " loaded. (Records:" + new Integer(lDB.size()).toString() + ")");
            fMessengerDB.put(aWorldName, lDB);
        }
        return fMessengerDB.get(aWorldName);
    }    
}
