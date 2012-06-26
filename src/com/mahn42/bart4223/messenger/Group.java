/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import java.util.ArrayList;

/**
 *
 * @author Nils
 */
public class Group {
    
    public Group(String aName) {
        Name = aName;
        Users = new ArrayList<GroupUser>();
    }
    
    public String Name;
    public ArrayList<GroupUser> Users;
    
}
