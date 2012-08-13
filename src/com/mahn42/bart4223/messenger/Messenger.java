/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;


import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;
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
    
    public MessageManager MM;
        
    @Override
    public void onEnable() {
        fLog = getLogger();
        MM = new MessageManager(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(MM), this);
        getCommand("messenger_send").setExecutor(new CommandMessengerSend(MM));
        getCommand("messenger_recall").setExecutor(new CommandMessengerRecall(MM));
        getCommand("messenger_listgroups").setExecutor(new CommandMessengerListgroups(MM));
        getCommand("messenger_listgroupusers").setExecutor(new CommandMessengerListgroupusers(MM));
        readMessengerConfig();
        readGroupsConfig();
    }
    
    @Override
    public void onDisable() { 

    }
          
    protected Logger fLog;   
    protected FileConfiguration fGroupsConfig;
    protected File fGroupsConfigFile;

    protected void readMessengerConfig() {
        FileConfiguration lConfig = getConfig();
        MM.MaxMessages = lConfig.getInt("MaxMessages");
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

    protected void readGroupsConfig() {
        FileConfiguration lConfig = getGroupsConfig();
        if (lConfig != null) fLog.info("Groups config loaded...");
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
                    if (MM.VerifyRecipient("@"+lGroupUser)) {
                        GroupUser lGrpUsr = new GroupUser(lGroupUser);
                        lGrp.Users.add(lGrpUsr);
                    }
                }
                MM.Groups.add(lGrp);
                fLog.info("Group " + lGrp.Name + " established with " + new Integer(lGrp.Users.size()).toString() + " users.");
            }            
        }
    }
    
}
