package org.qsp;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class GenerateLookupTables extends AbstractMediator {
    private static final Log log = LogFactory.getLog(GenerateLookupTables.class);

    public static HashMap<String, String> upcSKUMap = new HashMap<>();
    public static HashMap<String, String> shopIdSiteMap = new HashMap<>();

    @Override
    public boolean mediate(MessageContext messageContext) {

        log.info("Lookup table Class Mediator ...Start");

        String upcMapFilePath = (String) messageContext.getProperty("upc-sku-map-location");
        String shopIdMapFilePath = (String) messageContext.getProperty("shopid-site-map-location");

        try {
            this.readUPCSKUAndMap(upcMapFilePath);
            this.readShopIDSiteAndMap(shopIdMapFilePath);
        } catch (FileNotFoundException e) {
            log.error(e);
        }
        SOAPBody soapBody= messageContext.getEnvelope().getBody();

        OMElement rootElement = soapBody.getFirstElement();
        Iterator itemIterator = rootElement.getChildElements();

        while (itemIterator.hasNext()) {
            OMElement item = (OMElement) itemIterator.next();

            Iterator itemElementsIterator = item.getChildElements();
            OMElement upcElement = (OMElement) itemElementsIterator.next();
            if (upcSKUMap.get(upcElement.getText()) != null) {
                upcElement.setText(upcSKUMap.get(upcElement.getText()));
            } else {
                item.detach();
                continue;
            }

            OMElement qohElement = (OMElement) itemElementsIterator.next();

            OMElement shopIdElement = (OMElement) itemElementsIterator.next();
            if (shopIdSiteMap.get(shopIdElement.getText()) != null && !shopIdSiteMap.get(shopIdElement.getText()).equals("0")) {
                shopIdElement.setText(shopIdSiteMap.get(shopIdElement.getText()));
            } else {
                item.detach();
            }
        }
        return true;
    }

    public static void readUPCSKUAndMap(String filePath) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filePath));
        sc.useDelimiter(",");
        String line = sc.nextLine();
        while (sc.hasNextLine())  //returns a boolean value
        {
            line = sc.nextLine();
            String[] numberValues = line.split(",");

            if (!numberValues[0].isEmpty() && !numberValues[1].isEmpty()) {
                upcSKUMap.put(numberValues[1].replaceAll("\"", ""), numberValues[0].replaceAll("\"", ""));
            }
        }
        sc.close();
    }

    public static void readShopIDSiteAndMap(String filePath) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filePath));
        sc.useDelimiter(",");
        String line = sc.nextLine();
        while (sc.hasNextLine())
        {
            line = sc.nextLine();
            String[] numberValues = line.split(",");

            if (!numberValues[0].isEmpty() && !numberValues[1].isEmpty()) {
                shopIdSiteMap.put(numberValues[0].replaceAll("\"", ""), numberValues[1].replaceAll("\"", ""));
            }
        }
        sc.close();
    }
}
