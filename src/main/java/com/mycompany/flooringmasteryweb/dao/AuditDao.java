/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.dto.Audit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apprentice
 */
public class AuditDao {

    private List<Audit> auditsList;
    private File auditLogFile;

    public AuditDao(ConfigDao configDao) {
        this.auditLogFile = configDao.get().getAuditLogFile();
        auditsList = decode();
    }

    public AuditDao(java.io.File auditLogFile) {
        this.auditLogFile = auditLogFile;
        auditsList = decode();
    }

    public AuditDao(String auditString) {
        this.auditLogFile = new File("auditLog.txt");
    }

    public Audit create(Audit audit) {
        auditsList.add(audit);
        encode(audit);
        return audit;
    }

    public void update(Audit audit) {
        encode(audit, auditLogFile);
    }

    private void encode(Audit audit) {
        encode(audit, auditLogFile);
    }

    private void encode(Audit audit, File auditLogFile) {

        final String TOKEN = "\t";
        final String DATAHEADER = "Date\t Order Id\t Action Performed";

        boolean newLog = false;

        if (!auditLogFile.exists()) {
            newLog = true;
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(auditLogFile,true))) {

            if (newLog) {
                out.println(DATAHEADER);
            }

            out.println(convertAuditToString(audit, TOKEN));

            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(AuditDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String convertAuditToString(Audit audit, String TOKEN) {
        String auditString = audit.getLogDate()+ TOKEN + audit.getOrderid() + TOKEN + audit.getActionPerformed() + TOKEN + audit.getDate();
        return auditString;
    }

    private Audit buildAuditFromString(String auditString, String TOKEN) {

        String[] auditStringArray = auditString.split(TOKEN);
        Audit audit = new Audit();

        try {
            
            
            
            audit.setLogDate(getDateFromString(auditStringArray[0]));
            if ( auditStringArray.length > 3 ) {
            audit.setDate(getDateFromString(auditStringArray[3]));
            }
        } catch (ParseException ex) {
            System.out.println("Audit Builder was unable to parse the date.");
        }

        try {
            audit.setOrderid(Integer.parseInt(auditStringArray[1]));
        } catch (NumberFormatException ex) {
            System.out.println("Audit Builder was unable to parse the Order Id.");
        }

        audit.setActionPerformed(auditStringArray[2]);

        return audit;
    }

    private Date getDateFromString(String stringToParse) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        cal.setTime(sdf.parse(stringToParse));
        Date date = new Date();
        date.setTime(cal.getTimeInMillis());
        return date;
    }

    private List<Audit> decode() {
        return decode(auditLogFile);
    }

    private List<Audit> decode(File auditLogFile) {

        final String TOKEN = "\t";
        final String DATAHEADER = "Date\t Order Id\t Action Performed";

        List<Audit> tempAuditList = new ArrayList();

        if (auditLogFile.exists()) {

            try (Scanner sc = new Scanner(new BufferedReader(new FileReader(auditLogFile)))) {
                while (sc.hasNextLine()) {
                    String currentLine = sc.nextLine();
                    if (currentLine.equalsIgnoreCase(DATAHEADER)) {

                    } else if (!currentLine.trim().isEmpty()) {

                            Audit audit = buildAuditFromString(currentLine, TOKEN);
                            tempAuditList.add(audit);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AuditDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tempAuditList;
    }
}
