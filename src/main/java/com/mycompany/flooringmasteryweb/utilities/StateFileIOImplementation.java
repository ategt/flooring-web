/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.State;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apprentice
 */
public class StateFileIOImplementation implements StateFileIO, GenericMapFileIO<State> {

    private StateDao stateDao;

    public StateFileIOImplementation(StateDao stateDao) {
        this.stateDao = stateDao;
    }

    @Override
    public void encode(java.io.File stateDataFile, List<String> listToEncode) {

        final String TOKEN = ",";
        final String DATAHEADER = "State,TaxRate";
        try {

            try (PrintWriter out = new PrintWriter(new FileWriter(stateDataFile))) {
                out.println(DATAHEADER);

                for (String stateName : listToEncode) {

                    State state = stateDao.get(stateName);

                    out.print(state.getState());
                    out.print(TOKEN);
                    out.print(state.getStateTax());
                    out.println("");
                }

                out.flush();
            }

        } catch (IOException ex) {
            Logger.getLogger(StateDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Map<String, State> decode(java.io.File stateDataFile) {

        Map<String, State> stateList = new java.util.HashMap<>();

        final String TOKEN = ",";
        final String DATAHEADER = "State,TaxRate";

        try {

            if (!stateDataFile.exists()) {
                stateDataFile.createNewFile();
            }

            try (Scanner sc = new Scanner(new BufferedReader(new FileReader(stateDataFile)))) {
                while (sc.hasNextLine()) {
                    String currentLine = sc.nextLine();
                    if (currentLine.equalsIgnoreCase(DATAHEADER)) {

                    } else if (!currentLine.trim().isEmpty()) {

                        String[] stringParts = currentLine.split(TOKEN);

                        State state = new State();

                        String content = stringParts[0];

                        state.setState(content);

                        String stateSalesTaxString = stringParts[1];

                        try {
                            double stateSalesTax = Double.parseDouble(stateSalesTaxString);
                            state.setStateTax(stateSalesTax);
                        } catch (NumberFormatException numFmtEx) {

                        }

                        if (state.getState() != null && !state.getState().equalsIgnoreCase("")) {
                            stateList.put(state.getState(), state);
                        }
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(StateDao.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StateDao.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return stateList;
    }

}
