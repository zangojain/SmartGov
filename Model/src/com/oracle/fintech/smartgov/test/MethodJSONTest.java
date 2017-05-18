/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.fintech.smartgov.test;

import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;
import com.oracle.fintech.smartgov.multichain.MethodJSONBuilder;

/**
 *
 * @author developer
 */
public class MethodJSONTest {
        public static void main(String[] args) {
//        JsonObject personObject = Json.createObjectBuilder()
//                .add("method", "getinfo")
//                .add("params", 
//                     Json.createArrayBuilder().add("1")
//                                              .add("2")
//                                              .add("3")
//                                              .build()
//                    )
//                .add("id", BigDecimal.ONE)
//                .build();
        
        MethodJSONBuilder m = new MethodJSONBuilder();
        
        m.setMethodName("getinfo");
        m.addParam("a");
        m.addParam("b");
        
         
        System.out.println("Object: " + m.getJSON());
    }
}
