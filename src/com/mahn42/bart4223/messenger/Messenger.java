/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
    public ArrayList<Group> Groups;
        
    @Override
    public void onEnable() {
      getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
      getCommand("messenger_send").setExecutor(new CommandMessengerSend(this));
      getCommand("messenger_recall").setExecutor(new CommandMessengerRecall(this));
      getCommand("messenger_listgroups").setExecutor(new CommandMessengerListgroups(this));
      getCommand("messenger_listgroupusers").setExecutor(new CommandMessengerListgroupusers(this));
      log = this.getLogger();
      readMessengerConfig();
      Groups = new ArrayList<Group>();
      readGroupsConfig();
      MessageDBSet lDB = getDB();
      lDB.MaxMessages = configMaxMessages;
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
            fMessengerDB = new MessageDBSet(lFile);
            fMessengerDB.load();
            log.info("Datafile " + lFile.toString() + " loaded. (Records:" + new Integer(fMessengerDB.size()).toString() + ")");
        }
        return fMessengerDB;
    }    

    protected MessageDBSet fMessengerDB;
    protected FileConfiguration fGroupsConfig;
    protected File fGroupsConfigFile;

    protected void readMessengerConfig() {
        FileConfiguration lConfig = getConfig();
        configMaxMessages = lConfig.getInt("MaxMessages");
    }

    protected void readGroupsConfig() {
        FileConfiguration lConfig = getGroupsConfig();
        if (lConfig != null) log.info("Groups config loaded...");
        List<Object> lGroups = (List<Object>)lConfig.getList("Groups");
        if (lGroups != null) {
            Iterator<Object> iter = lGroups.iterator();
            while (iter.hasNext()) {
                LinkedHashMap lGroup = (LinkedHashMap)iter.next();
                Group lGrp = new Group(lGroup.get("Name").toString());
                List<Object> lGroupUsers = (List<Object>)lGroup.get("Users");
                Iterator<Object> literGU = lGroupUsers.iterator();
                while (literGU.hasNext()) {
                    String lGroupUser = (String)literGU.next();
                    if (VerifyRecipient("@"+lGroupUser)) {
                        GroupUser lGrpUsr = new GroupUser(lGroupUser);
                        lGrp.Users.add(lGrpUsr);
                    }
                }
                Groups.add(lGrp);
                log.info("Group " + lGrp.Name + " established with " + new Integer(lGrp.Users.size()).toString() + " users.");
            }            
        }
    }
    
    protected void LoadGroupsConfig() {
        if (fGroupsConfigFile == null) {
            fGroupsConfigFile = new File(getDataFolder(), "groups.yml");
        }
        fGroupsConfig = YamlConfiguration.loadConfiguration(fGroupsConfigFile);
    }
    
    protected FileConfiguration getGroupsConfig() {
        if (fGroupsConfig == null) {
            LoadGroupsConfig();
        }
        return fGroupsConfig;
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
    
    public boolean VerifyGroup(String aGroup) {
        boolean lOK = false;
        for (Group lGroup : Groups) {
            lOK = (lGroup.Name.equals(aGroup));
            if (lOK) break;  
        }
        return lOK;
    }
    
    public Group GetGroup(String aGroup) {
        Group lRes = null;
        for (Group lGroup : Groups) {
            if (lGroup.Name.equals(aGroup)) {
                lRes = lGroup;
                break;
            }
        }
        return lRes;
    }

}
