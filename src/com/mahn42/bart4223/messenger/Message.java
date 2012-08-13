/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

/**
 *
 * @author Nils
 */
public class Message implements Comparable<Message>{

    public String Sender;
    public String Text;
    public long Timestamp;

    public Message(String aSender, String aText, long aTimestamp){
        Sender = aSender;
        Text = aText;
        Timestamp = aTimestamp;
    }    
    
    @Override
    public int compareTo( Message argument ) {
        return Sender.compareTo(argument.Sender);
    }    
}
