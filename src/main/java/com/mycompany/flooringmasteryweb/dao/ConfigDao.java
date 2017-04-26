/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dao;

import com.mycompany.flooringmasteryweb.exceptions.ConfigurationFileCorruptException;
import com.mycompany.flooringmasteryweb.exceptions.FileCreationException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apprentice
 */
public class ConfigDao {

    java.io.File configFile;
    com.mycompany.flooringmasteryweb.dto.Config config;
    java.io.File defaultTestDirectory = new java.io.File("Test");
    java.io.File defaultDataDirectory = new java.io.File("Data");
    java.io.File defaultOrdersDirectory = new java.io.File("Orders");
    java.io.File defaultTaxesFile = new java.io.File("Taxes.txt");
    java.io.File defaultProductsFile = new java.io.File("Products.txt");

    public ConfigDao() throws ConfigurationFileCorruptException, FileCreationException {
        this(new java.io.File("config.txt"));
    }

    public ConfigDao(java.io.File configFile) throws ConfigurationFileCorruptException, FileCreationException {
        this.configFile = configFile;

        buildConfig(configFile);
        if (!verifyLoading()) {
            throw new com.mycompany.flooringmasteryweb.exceptions.ConfigurationFileCorruptException("The configuration file Failed to verify!\n Without basic configuration parameters, this program will not function correctly.");
        }

        validateFolders();
    }

    public final void validateFolders() throws FileCreationException {

        validateFolder(config.getTestDirectory());
        validateFolder(config.getDataDirectory());
validateFolder(config.getOrdersDirectory());

        if (!config.getTestDirectory().isDirectory()) {
            throw new FileCreationException("Something went wrong, the test file is not a directory.");
        }

        if (!config.getDataDirectory().isDirectory()) {
            throw new FileCreationException("Something went wrong, the data file is not a directory.");
        }

        if (!config.getOrdersDirectory().isDirectory()) {
            throw new FileCreationException("Something went wrong, the orders file is not a directory.");
        }
    }

    public void placeDataFilesInDataFolder() {
        placeDataFilesInDataFolder(config);
    }

    public void placeDataFilesInDataFolder(com.mycompany.flooringmasteryweb.dto.Config config) {

        // The config file should always be non-null, but just in case.
        if (config != null) {

            // Get the Tax File
            java.io.File taxFile = config.getTaxesFile();
            java.io.File taxParentFile = null;

            // If the Tax file has a parent define it, otherwise it stays null.
            if (taxFile.getParent() != null) {
                taxParentFile = new java.io.File(taxFile.getParent());
            }

            // Get the data directory.
            java.io.File dataDir = config.getDataDirectory();

            // If the tax file is an orphan give it a parent.
            if (taxParentFile == null) {
                config.setTaxesFile(new java.io.File((dataDir.getPath() + "/" + config.getTaxesFile().getName()).replaceAll("//", "/")));
            }

            java.io.File productFile = config.getProductFile();
            java.io.File productParentFile = null;

            if (productFile.getParent() != null) {
                productParentFile = new java.io.File(productFile.getParent());
            }

            if (productParentFile == null) {
                config.setProductFile(new java.io.File((dataDir.getPath() + "/" + config.getProductFile().getName()).replaceAll("//", "/")));
            }
     
            
        }
    }

    public void validateFolder(java.io.File folder) throws FileCreationException {

        if (!folder.exists()) {
            if (!folder.mkdir()) {
                throw new FileCreationException("Something went wrong during file creation.");
            }
        }
    }

    private boolean verifyLoading() { 
        boolean goodLoad = true;

        // This loads the defaults and will transfer the default values to any null fields.
        com.mycompany.flooringmasteryweb.dto.Config tempConfig = makeConfig();
        if (config.getProductFile() == null) {
            config.setProductFile(tempConfig.getProductFile());
            goodLoad = false;
        }

        if (config.getTaxesFile() == null) {
            config.setTaxesFile(tempConfig.getTaxesFile());
            goodLoad = false;
        }

        if (config.getTestDirectory() == null) {
            config.setTestDirectory(tempConfig.getTestDirectory());
            goodLoad = false;
        }

        if (config.getDataDirectory() == null) {
            config.setDataDirectory(tempConfig.getDataDirectory());
            goodLoad = false;
        }

        if (config.getOrdersDirectory() == null) {
            config.setOrdersDirectory(tempConfig.getOrdersDirectory());
            goodLoad = false;
        }

        return goodLoad;
    }

    private void buildConfig(java.io.File configFile) {
        if (configFile == null) {
            config = makeConfig();

        } else if (configFile.exists()) {
            config = decode(configFile);
        } else {
            config = makeConfig();
            encode();
        }

    }

    public com.mycompany.flooringmasteryweb.dto.Config get() {
        return config;
    }

    public void update() {
        encode();
    }

    private com.mycompany.flooringmasteryweb.dto.Config decode(java.io.File dataFile) {

        com.mycompany.flooringmasteryweb.dto.Config tempConfig = new com.mycompany.flooringmasteryweb.dto.Config();

        java.util.List<String> configList = new java.util.ArrayList();

        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(dataFile)))) {
            while (sc.hasNextLine()) {
                String currentLine = sc.nextLine();

                if (!currentLine.trim().isEmpty()) {
                    configList.add(currentLine);
                }
            }

            for (String configLine : configList) {
                if (configLine.contains("ProductFile:")) {
                    String productFilePath = configLine.replace("ProductFile:", "");
                    java.io.File productFile = new java.io.File(productFilePath);
                    tempConfig.setProductFile(productFile);
                } else if (configLine.contains("TaxesFile:")) {
                    String taxesFilePath = configLine.replace("TaxesFile:", "");
                    java.io.File taxesFile = new java.io.File(taxesFilePath);
                    tempConfig.setTaxesFile(taxesFile);
                } else if (configLine.contains("TestDirectory:")) {
                    String testDirectoryPath = configLine.replace("TestDirectory:", "");
                    java.io.File testDirectory = new java.io.File(testDirectoryPath);
                    tempConfig.setTestDirectory(testDirectory);
                } else if (configLine.contains("DataDirectory:")) {
                    String dataDirectoryPath = configLine.replace("DataDirectory:", "");
                    java.io.File dataDirectory = new java.io.File(dataDirectoryPath);
                    tempConfig.setDataDirectory(dataDirectory);
                } else if (configLine.contains("OrdersDirectory:")) {
                    String ordersDirectoryPath = configLine.replace("OrdersDirectory:", "");
                    java.io.File ordersDirectory = new java.io.File(ordersDirectoryPath);
                    tempConfig.setOrdersDirectory(ordersDirectory);
                } else if (configLine.contains("InTestMode:")) {
                    String inTestModeString = configLine.replace("InTestMode:", "");
                    boolean inTestMode = true;
                    if (inTestModeString.equalsIgnoreCase("false")) {
                        inTestMode = false;
                    }

                    tempConfig.setInTestMode(inTestMode);
                } else {
                    System.out.println("Throw unknown line in config file EXCEPTION!!");
                }

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tempConfig;
    }

    private com.mycompany.flooringmasteryweb.dto.Config makeConfig() {

        com.mycompany.flooringmasteryweb.dto.Config tempConfig = new com.mycompany.flooringmasteryweb.dto.Config();

        tempConfig.setInTestMode(true);
        tempConfig.setProductFile(defaultProductsFile);
        tempConfig.setTaxesFile(defaultTaxesFile);
        tempConfig.setTestDirectory(defaultTestDirectory);
        tempConfig.setDataDirectory(defaultDataDirectory);
        tempConfig.setOrdersDirectory(defaultOrdersDirectory);
        

        placeDataFilesInDataFolder(tempConfig);

        return tempConfig;
    }

    private void encode() {

        try (PrintWriter out = new PrintWriter(new FileWriter(configFile))) {

            String configString = "ProductFile:" + config.getProductFile().getPath() + "\n"
                    + "TaxesFile:" + config.getTaxesFile().getPath() + "\n"
                    + "TestDirectory:" + config.getTestDirectory().getPath() + "\n"
                    + "DataDirectory:" + config.getDataDirectory().getPath() + "\n"
                    + "OrdersDirectory:" + config.getOrdersDirectory().getPath() + "\n"
                    + "InTestMode:" + config.isInTestMode();

            out.print(configString);
            out.println("");

            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ConfigDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
