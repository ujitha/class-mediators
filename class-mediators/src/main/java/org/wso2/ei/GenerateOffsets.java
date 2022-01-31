package org.wso2.ei;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.json.JSONArray;
import org.json.JSONObject;


public class GenerateOffsets extends AbstractMediator {
    private static final Log log = LogFactory.getLog(GenerateOffsets.class);

    @Override
    public boolean mediate(MessageContext messageContext) {
        log.info("Generate Offsets Class Mediator ...Start");
        Integer count  =  Integer.parseInt((String) messageContext.getProperty("count"));
        Integer limit  = Integer.parseInt((String) messageContext.getProperty("limit"));

        int itCount = count/limit + 1;

        JSONObject root = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (int i=0; i < itCount; i++) {
            int offsetValue = i * limit;
            JSONObject obj = new JSONObject();
            obj.put("offset", offsetValue);
            jsonArray.put(obj);
        }
        root.put("offsets", jsonArray);

        messageContext.setProperty("offsets", root.toString());
        log.info("Generate Offsets Class Mediator ...Ends");
        return true;
    }
}
