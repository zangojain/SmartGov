/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.fintech.smartgov.multichain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author developer
 */
public class MethodJSONBuilder {

    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    private List<String> params = new ArrayList<String>();

    public void addParam(String param) {
        this.params.add(param);
    }

    private static int id = 0;

    private String chainName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void incrementId(int id) {
        this.id++;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getJSON() {
        JsonObject methodJSON;
        JsonObjectBuilder j = Json.createObjectBuilder().add("method", this.methodName);
        JsonArrayBuilder pa = Json.createArrayBuilder();
        for (int i = 0; i < this.params.size(); i++) {
            pa.add(this.params.get(i));
        }
        j.add("params", pa);
        j.add("id", this.id);
        j.add("chain_name", this.chainName==null?"":this.chainName);
        methodJSON = j.build();
        return methodJSON.toString();
    }
}
