/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import com.mahn42.framework.DBRecord;
import java.util.ArrayList;

/**
 *
 * @author Nils
 */
public class MessageDBRecord extends DBRecord{
 
    public String Sender;
    public String Recipient;
    public String Text;

    @Override
    protected void toCSVInternal(ArrayList aCols) {
        super.toCSVInternal(aCols);
      aCols.add(Sender);
      aCols.add(Recipient);
      aCols.add(Text);
    }

    @Override
    protected void fromCSVInternal(DBRecord.DBRecordCSVArray aCols) {
        super.fromCSVInternal(aCols);
     Sender = aCols.pop();
     Recipient = aCols.pop();
     Text = aCols.pop();   
    }
}
