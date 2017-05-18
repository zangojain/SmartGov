/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.fintech.smartgov.test;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.oracle.fintech.smartgov.multichain.MultichainQueryBuilder;
import com.oracle.fintech.smartgov.multichain.rpc.JsonRPC;


/**
 *
 * @author developer
 */
public class MultiChainTest {
    private static String CHAINNAME = "orahyperledger"; //"testchain";
    
    public static void main(String [] args ) {

            //MultichainQueryBuilder.createMultichain(CHAINNAME);
            MultichainQueryBuilder.initializeChain(CHAINNAME);
            //MultichainQueryBuilder.setCallMode(MultichainQueryBuilder.CALLMODE.LOCAL);
            //System.out.println(MultichainQueryBuilder.executeGetInfo());
            MultichainQueryBuilder.setCallMode(MultichainQueryBuilder.CALLMODE.REMOTE);
            System.out.println(MultichainQueryBuilder.executeGetInfo());
            System.out.println("Test Complete");

    }
    
}
