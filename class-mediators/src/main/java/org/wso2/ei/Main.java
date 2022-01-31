package org.wso2.ei;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
//        generateJSONPayload(250, 100);
    }

    public static void generateJSONPayload(int count, int limit) {
        int itCount = count/limit + 1;

        JSONObject root = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (int i=0; i<itCount; i++) {
            int offsetValue = i * 100 + 1;
            JSONObject obj = new JSONObject();
            obj.put("offset", offsetValue);
            jsonArray.put(obj);
        }

        root.put("offsets", jsonArray);

        System.out.println(root.toString());
    }
}
