/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.bart4223.messenger;

import com.mahn42.framework.DBRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nils
 */
public class MessageDBRecord extends DBRecord{
 
    public String Sender;
    public String Recipient;
    public String Text;
    public long Timestamp;

    @Override
    protected void toCSVInternal(ArrayList aCols) {
        super.toCSVInternal(aCols);
        aCols.add(Sender);
        aCols.add(Recipient);
        aCols.add(Text);
        SimpleDateFormat lSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        aCols.add(lSdf.format(new Date(Timestamp)));
    }

    @Override
    protected void fromCSVInternal(DBRecord.DBRecordCSVArray aCols) {
        super.fromCSVInternal(aCols);
        Sender = aCols.pop();
        Recipient = aCols.pop();
        Text = aCols.pop();   
        SimpleDateFormat lSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        Date lDate;
        try {
            lDate = lSdf.parse(aCols.pop());
            Timestamp = lDate.getTime();
        } catch (ParseException ex) {
            Logger.getLogger(MessageDBRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
