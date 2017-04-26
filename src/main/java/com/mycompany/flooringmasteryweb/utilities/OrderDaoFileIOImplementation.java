/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.Order;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author apprentice
 */
public class OrderDaoFileIOImplementation implements OrderDaoFileIO {
 
    private OrderDao orderDao;
    private StateDao stateDao;
    private ProductDao productDao;

    public OrderDaoFileIOImplementation(OrderDao orderDao, StateDao stateDao, ProductDao productDao){
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.stateDao = stateDao;
    }
    
    @Override
    public void encode(PrintWriter printWriter, List<Order> groupOfOrders) {

        final String TOKEN = ",";
        final String DATAHEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCost"
                + "PerSquareFoot,MaterialCost,LaborCost,Tax,Total";

        try (PrintWriter out = printWriter) {
            out.println(DATAHEADER);

            groupOfOrders.stream()
                        .map((order) -> orderDao.toString(order, TOKEN))
                        .forEach((orderString) -> {
                                                out.println(orderString);
                        });

            out.flush();
        }

    }
    
    @Override
    public List<Order> decode(java.io.File orderFile) throws FileNotFoundException, IOException {

        if (!orderFile.exists()) {
            orderFile.createNewFile();
        }

        String dateString = orderFile.getName();

        return decode(new BufferedReader(new FileReader(orderFile)), dateString);

    }

    @Override
    public List<Order> decode(BufferedReader bufferedReader, String dateString) {

        List<Order> orderList = new ArrayList<>();

        final String TOKEN = ",";
        final String CSV_ESCAPE = Pattern.quote("\\") + TOKEN;
        final String CSV_ESCAPE_TEMP = "::==::";

        final String DATAHEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCost"
                + "PerSquareFoot,MaterialCost,LaborCost,Tax,Total";

        java.util.Date orderDate = orderDao.extractDate(dateString);

        try (Scanner sc = new Scanner(bufferedReader)) {
            while (sc.hasNextLine()) {
                String currentLine = sc.nextLine();
                if (currentLine.equalsIgnoreCase(DATAHEADER)) {

                } else if (!currentLine.trim().isEmpty()) {

                    String[] stringParts = currentLine.replaceAll(CSV_ESCAPE, CSV_ESCAPE_TEMP).split(TOKEN);

                    for (int x = 0; x < stringParts.length; x++) {
                        stringParts[x] = stringParts[x].replaceAll(CSV_ESCAPE_TEMP, TOKEN);
                    }

                    Order order = new Order();

                    String orderIdString = stringParts[0];

                    try {
                        int orderId = Integer.parseInt(orderIdString);
                        order.setId(orderId);
                    } catch (NumberFormatException numFmtEx) {

                    }

                    String name = stringParts[1];
                    order.setName(name);

                    String state = stringParts[2];
                    order.setState(stateDao.get(state));

                    try {

                        String taxRateString = stringParts[3];
                        double taxRate = Double.parseDouble(taxRateString);
                        order.setTaxRate(taxRate);

                    } catch (NumberFormatException numFmtEx) {

                    }
                    String productType = stringParts[4];

                    order.setProduct(productDao.get(productType));

                    try {
                        String areaString = stringParts[5];
                        double area = Double.parseDouble(areaString);
                        order.setArea(area);
                    } catch (NumberFormatException numFmtEx) {

                    }

                    try {
                        String costPerSquareFootString = stringParts[6];
                        double costPerSquareFoot = Double.parseDouble(costPerSquareFootString);
                        order.setCostPerSquareFoot(costPerSquareFoot);
                    } catch (NumberFormatException numFmtEx) {

                    }

                    try {
                        String laborCostPerSquareFootString = stringParts[7];
                        double laborCostPerSquareFoot = Double.parseDouble(laborCostPerSquareFootString);
                        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
                    } catch (NumberFormatException numFmtEx) {

                    }

                    try {

                        String materialCostString = stringParts[8];
                        double materialCost = Double.parseDouble(materialCostString);
                        order.setMaterialCost(materialCost);
                    } catch (NumberFormatException numFmtEx) {

                    }

                    try {

                        String laborCostString = stringParts[9];
                        double laborCost = Double.parseDouble(laborCostString);
                        order.setLaborCost(laborCost);

                    } catch (NumberFormatException numFmtEx) {

                    }
                    try {

                        String taxString = stringParts[10];
                        double tax = Double.parseDouble(taxString);
                        order.setTax(tax);

                    } catch (NumberFormatException numFmtEx) {

                    }
                    try {

                        String totalString = stringParts[11];
                        double total = Double.parseDouble(totalString);
                        order.setTotal(total);
                    } catch (NumberFormatException numFmtEx) {

                    }

                    order.setDate(orderDate);

                    orderList.add(order);
                }
            }
        }

        return orderList;
    }

}
