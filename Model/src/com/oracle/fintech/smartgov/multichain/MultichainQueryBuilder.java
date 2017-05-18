package com.oracle.fintech.smartgov.multichain;

import com.oracle.fintech.smartgov.multichain.rpc.JsonRPC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MultichainQueryBuilder {

    private static String CHAIN = "orahyperledger";
    private static boolean header = false;
    private static String MULTICHAIN_BINARY_PATH = "D:\\OfficeWork\\blockchain\\";//"C:\\Users\\devel\\Apps\\Multichain\\";
    private static String MULTICHAIN_CLI = MULTICHAIN_BINARY_PATH + "multichain-cli  ";
    private static String MULTICHAIN_UTIL = MULTICHAIN_BINARY_PATH + "multichain-util  ";
    private static String MULTICHAIN_DEAMON = MULTICHAIN_BINARY_PATH + "multichaind  ";

    public enum CALLMODE {
        LOCAL, REMOTE
    };
    private static CALLMODE CALL_MODE = CALLMODE.REMOTE;

    private static String formatedArray(String[] array, boolean formatWithArray) {
        String stringFormated = "";

        if (formatWithArray) {
            stringFormated = "[";
        }

        for (int i = 0; i < array.length; i++) {
            if (formatWithArray) {
                stringFormated = stringFormated.concat("\"" + array[i] + "\"");
            } else {
                stringFormated = stringFormated.concat(array[i]);
            }

            if (i < array.length - 1) {
                stringFormated = stringFormated.concat(",");
            }
        }

        if (formatWithArray) {
            stringFormated = stringFormated.concat("]");
        }
        return stringFormated;
    }

    private static String formatedAmount(String[] assetsNames, float[] quantities) {
        String amountFormated = "{";

        for (int i = 0; i < assetsNames.length; i++) {

            amountFormated = amountFormated.concat("\"" + assetsNames[i] + "\":" + quantities[i]);

            if (i < assetsNames.length - 1) {
                amountFormated = amountFormated.concat(",");
            }
        }
        amountFormated = amountFormated.concat("}");
        return amountFormated;
    }

    public static void initializeChain(String nameChain) {
        CHAIN = nameChain;
    }

    public static void setCallMode(CALLMODE c) {
        CALL_MODE = c;
    }

    public static String executeProcess(MultichainCommand command) {
        if (CALL_MODE == null) {
            CALL_MODE = CALLMODE.LOCAL;
        }
        return execute(command);
    }

    public static String createMultichain(String name) {
        Runtime rt = Runtime.getRuntime();
        Process pr;
        String result = "";
        try {

            pr = rt.exec(MULTICHAIN_UTIL + " create " + name);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

            // read the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                result = result.concat(s + "\n");
            }

            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                result = result.concat(s + "\n");
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "IOException error";
    }

    public static String launchMultichainNode(String name) {
        Runtime rt = Runtime.getRuntime();
        Process pr;
        String result = "";
        try {

            pr = rt.exec(MULTICHAIN_DEAMON + name + " -daemon");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

            // read the output from the command
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                result = result.concat(s + "\n");
                Thread.sleep(1);
            }
            //
            stdInput.close();
            //
            // read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                result = result.concat(s + "\n");
                Thread.sleep(1);
            }

            stdError.close();

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "IOException error";

    }

    /**
     *
     * @param command
     * @param parameter
     *
     * @return
     *
     * example :
     * MultichainQueryBuidlder.executeProcess(MultichainCommand.SENDTOADDRESS,"1EyXuq2JVrj4E3CpM9iNGNSqBpZ2iTPdwGKgvf
     * {\"rdcoin\":0.01}"
     */
    public static String execute(MultichainCommand command, String... parameters) {

        if (!CHAIN.equals("")) {
            MethodJSONBuilder m = new MethodJSONBuilder();
            m.setMethodName(command.toString().toLowerCase());
            m.setChainName(CHAIN);
            Runtime rt = Runtime.getRuntime();
            Process pr = null;
            String result = "";
            String postResponse = "";
            boolean error = false;
            try {
                if (parameters.length > 0) {
                    String params = "";
                    for (String parameter : parameters) {
                        params = params.concat(parameter + " ");
                        m.addParam(parameter);
                    }
                    System.out.println(MULTICHAIN_CLI + CHAIN + " " + command.toString().toLowerCase() + " " + params);
                    if (CALL_MODE == CALLMODE.LOCAL) {
                        pr = rt.exec(MULTICHAIN_CLI + CHAIN + " " + command.toString().toLowerCase() + " " + params);
                    } else {
                        System.out.println(m.getJSON());
                        postResponse = JsonRPC.doPost(m.getJSON().toString());
                    }
                } else {
                    System.out.println(MULTICHAIN_CLI + CHAIN + " " + command.toString().toLowerCase());
                    if (CALL_MODE == CALLMODE.LOCAL) {
                        pr = rt.exec(MULTICHAIN_CLI + CHAIN + " " + command.toString().toLowerCase());
                    } else {
                        System.out.println(m.getJSON());
                        postResponse = JsonRPC.doPost(m.getJSON().toString());
                    }
                }

                BufferedReader stdInput = null;
                BufferedReader stdError = null;

                if (CALL_MODE == CALLMODE.LOCAL) {
                    stdInput = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                    stdError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
                }
                // read the output from the command
                String s = null;
                if (CALL_MODE == CALLMODE.LOCAL) {
                    while ((s = stdInput.readLine()) != null) {
                        result = result.concat(s + "\n");
                    }

                    // read any errors from the attempted command
                    while ((s = stdError.readLine()) != null) {
                        error = true;
                        result = result.concat(s + "\n");
                    }
                } else{
                   result = postResponse;
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!header && !error) {
                return removeHeader(result);
            } else {
                return result;
            }
        } else {
            return "ERROR, CHAIN NAME ARE EMPTY !";
        }

    }

    private static String removeHeader(String result) {
        String resultWithoutHeader = "";
        int size = 16 + CHAIN.length();
        int index = 0;
        System.out.println("removeHeader result arg:::"+result);
        if (CALL_MODE == CALLMODE.LOCAL) {
            index = result.indexOf("\"chain_name\":\"" + CHAIN + "\"");
        }
        resultWithoutHeader = resultWithoutHeader.concat(result.substring(index + size));
        return resultWithoutHeader;
    }

    public static void includeHeaderIntoTransactionResult(boolean b) {
        header = b;
    }

    /**
     * Creates a pay-to-scripthash (P2SH) multisig address. Funds sent to this
     * address can only be spent by transactions signed by nrequired of the
     * specified keys. Each key can be a full public key, or an address if the
     * corresponding key is in the node’s wallet.
     * <p>
     * (Public keys for a wallet’s addresses can be obtained using the
     * getaddresses call with verbose=true.)
     * </p>
     *
     *
     * @param numberOfSigRequired
     * @param publicKeys
     * @return The P2SH address and corresponding redeem script.
     * @throws MultichainException
     */
    public static String executeCreateMultiSig(int numberOfSigRequired, String[] publicKeys)
            throws MultichainException {

        MultichainTestParameter.intValueIsPositive("number of signature required", numberOfSigRequired);
        MultichainTestParameter.arrayIsNotNullOrEmpty("public Keys", publicKeys);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("public Keys", publicKeys);
        if (publicKeys.length >= numberOfSigRequired) {
            String publicKeysFormated = formatedArray(publicKeys, true);

            return execute(MultichainCommand.CREATEMULTISIG, String.valueOf(numberOfSigRequired), publicKeysFormated);
        } else {
            throw new MultichainException("number of signature", "is greater than the size of public keys");
        }
    }

    /**
     *
     * @return list of all the parameters of this blockchain, reflecting the
     * content of its params.dat file.
     */
    public static String executeGetBlockChainParams() {
        return execute(MultichainCommand.GETBLOCKCHAINPARAMS);
    }

    /**
     *
     * @return Returns general information about this node and blockchain.
     */
    public static String executeGetInfo() {
        return execute(MultichainCommand.GETINFO);
    }

    /**
     *
     * @return A list of available API commands, including MultiChain-specific
     * commands.
     */
    public static String executeHelp() {
        return execute(MultichainCommand.HELP);
    }

    /**
     * Shuts down the this blockchain node, i.e. stops the multichaind process.
     *
     * @return
     */
    public static String executeStop() {
        return execute(MultichainCommand.STOP);
    }

    /**
     * Returns information about address including a check for its validity.
     *
     * @param address
     * @return
     * @throws MultichainException
     */
    public static String executeValidateAddress(String address) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        return execute(MultichainCommand.VALIDATEADDRESS, address);
    }

    /**
     * Creates a pay-to-scripthash (P2SH) multisig address and adds it to the
     * wallet. Funds sent to this address can only be spent by transactions
     * signed by nrequired of the specified keys. Each key can be a full public
     * key, or an address if the corresponding key is in the node’s wallet.
     * (Public keys for a wallet’s addresses can be obtained using the
     * getaddresses call with verbose=true.)
     *
     * @param numberOfSigRequired
     * @param publicKeys
     * @return the P2SH address
     * @throws MultichainException
     */
    public static String executeAddMultiSigAddress(int numberOfSigRequired, String[] publicKeys)
            throws MultichainException {
        MultichainTestParameter.intValueIsPositive("number of signature required", numberOfSigRequired);
        MultichainTestParameter.arrayIsNotNullOrEmpty("publicKeys", publicKeys);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("publicKeys", publicKeys);
        if (publicKeys.length >= numberOfSigRequired) {

            String publicKeysFormated = formatedArray(publicKeys, true);
            return execute(MultichainCommand.ADDMULTISIGADDRESS, String.valueOf(numberOfSigRequired),
                    publicKeysFormated);
        } else {
            throw new MultichainException("number of signature", "is greater than the size of public keys");
        }
    }

    /**
     *
     * @param address
     * @return the private key associated with address in this node’s wallet
     * @throws MultichainException
     */
    public static String executeDumpPrivKey(String address) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        return execute(MultichainCommand.VALIDATEADDRESS, address);
    }

    /**
     *
     * @param verbose t verbose to true to get more information about each
     * address, formatted like the output of the validateaddress command.
     * @return a list of addresses in this node’s wallet
     */
    public static String executeGetAddresses(boolean verbose) {
        return execute(MultichainCommand.GETADDRESSES, String.valueOf(verbose));
    }

    /**
     *
     * @return a new address whose private key is added to the wallet.
     */
    public static String executeGetNewAddress() {
        return execute(MultichainCommand.GETNEWADDRESS);
    }

    /**
     * Adds address to the wallet, without an associated private key, to create
     * a watch-only address. This is an address whose activity and balance can
     * be retrieved , but whose funds cannot be spent by this node
     *
     * @param address
     * @param rescan If rescan is true, the entire blockchain is checked for
     * transactions relating to all addresses in the wallet, including the added
     * one
     * @return null if successful.
     * @throws MultichainException
     */
    public static String executeImportAddress(String address, boolean rescan) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        return execute(MultichainCommand.IMPORTADDRESS, address, String.valueOf(rescan));
    }

    /**
     * Adds address to the wallet, without an associated private key, to create
     * a watch-only address. This is an address whose activity and balance can
     * be retrieved , but whose funds cannot be spent by this node
     *
     * @param address
     * @param label
     * @param rescan If rescan is true, the entire blockchain is checked for
     * transactions relating to all addresses in the wallet, including the added
     * one
     * @return null if successful.
     * @throws MultichainException
     */
    public static String executeImportAddress(String address, String label, boolean rescan) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("label", label);
        return execute(MultichainCommand.IMPORTADDRESS, address, label, String.valueOf(rescan));

    }

    /**
     * Adds the privkey private key to the wallet, together with its associated
     * public address
     *
     * @param privKey
     * @param rescan If rescan is true, the entire blockchain is checked for
     * transactions relating to all addresses in the wallet, including the added
     * one
     * @return
     * @throws MultichainException
     */
    public static String executeImportPrivKey(String privKey, boolean rescan) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("privKey", privKey);
        return execute(MultichainCommand.IMPORTPRIVKEY, privKey, String.valueOf(rescan));
    }

    /**
     * Adds the privkey private key to the wallet, together with its associated
     * public address
     *
     * @param privKey
     * @param label An optional parameter, if you want to set label just send
     * only one string
     * @param rescan If rescan is true, the entire blockchain is checked for
     * transactions relating to all addresses in the wallet, including the added
     * one
     * @return
     * @throws MultichainException
     */
    public static String executeImportPrivKey(String privKey, String label, boolean rescan) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("privKey", privKey);
        MultichainTestParameter.stringIsNotNullOrEmpty("label", label);
        return execute(MultichainCommand.IMPORTPRIVKEY, privKey, label, String.valueOf(rescan));
    }

    /**
     * Grants permissions to addresses
     *
     * @param addresses
     * @param permissions This permissions will be grant to all addresses who
     * are send in parameter
     * @return the txid of the transaction granting the permissions
     * @throws MultichainException
     */
    public static String executeGrant(String[] addresses, MultichainPermissions[] permissions)
            throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);

        String addressesFormated = formatedArray(addresses, false);

        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }
        return execute(MultichainCommand.GRANT, addressesFormated, permissionsFormated);
    }

    //
    /**
     * This works like grant, but with control over the from-address used to
     * grant the permissions. If there are multiple addresses with administrator
     * permissions on one node, this allows control over which address is used.
     *
     * @param fromAddress
     * @param toAddresses
     * @param permissions This permissions will be grant to all addresses who
     * are send in parameter
     * @return the txid of the transaction granting the permissions
     * @throws MultichainException
     */
    public static String executeGrantFrom(String fromAddress, String[] toAddresses, MultichainPermissions[] permissions)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.arrayIsNotNullOrEmpty("toAddresses", toAddresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("toAddresses", toAddresses);
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);

        String addressesFormated = formatedArray(toAddresses, false);

        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }
        return execute(MultichainCommand.GRANTFROM, fromAddress, addressesFormated, permissionsFormated);
    }

    /**
     * This works like grant, but includes the data-hex hexadecimal metadata in
     * an additional OP_RETURN transaction output.
     *
     * @param addresses
     * @param permissions This permissions will be grant to all addresses who
     * are send in parameter
     * @param dataHex
     * @return the txid of the transaction granting the permissions
     * @throws MultichainException
     */
    public static String executeGrantWithMetaData(String[] addresses, MultichainPermissions[] permissions,
            String dataHex) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);
        MultichainTestParameter.stringIsNotNullOrEmpty("dataHex", dataHex);
        String addressesFormated = formatedArray(addresses, false);

        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }
        return execute(MultichainCommand.GRANTWITHMETADATA, addressesFormated, permissionsFormated, dataHex);
    }

    /**
     * This works like grantfrom, but includes the data-hex hexadecimal metadata
     * in an additional OP_RETURN transaction output.
     *
     * @param fromAddress
     * @param toAddresses
     * @param permissions This permissions will be grant to all addresses who
     * are send in parameter
     * @param dataHex
     * @return the txid of the transaction granting the permissions
     * @throws MultichainException
     */
    public static String executeGrantWithMetaDataFrom(String fromAddress, String[] toAddresses,
            MultichainPermissions[] permissions, String dataHex) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("toAddresses", toAddresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("toAddress", toAddresses);
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("dataHex", dataHex);
        String addressesFormated = formatedArray(toAddresses, false);

        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }
        return execute(MultichainCommand.GRANTWITHMETADATAFROM, addressesFormated, permissionsFormated, dataHex);
    }

    /**
     *
     * @return a list of all permissions currently granted to addresses.
     */
    public static String executeListPermissions() {
        return execute(MultichainCommand.LISTPERMISSIONS);
    }

    /**
     *
     * @param permissions
     * @return a List information about specific permissions only.
     * @throws MultichainException
     */
    public static String executeListPermissions(MultichainPermissions[] permissions) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);

        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }
        return execute(MultichainCommand.LISTPERMISSIONS, permissionsFormated);
    }

    /**
     *
     * @param permissions
     * @param addresses
     * @return a List information about specific permissions only list the
     * permissions for particular addresses
     * @throws MultichainException
     */
    public static String executeListPermissions(MultichainPermissions[] permissions, String[] addresses)
            throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);
        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }
        String addressesFormated = formatedArray(addresses, false);
        return execute(MultichainCommand.LISTPERMISSIONS, permissionsFormated, addressesFormated);
    }

    /**
     * If verbose is true, the admins output field lists the administrator/s who
     * assigned the corresponding permission, and the pending field lists
     * permission changes which are waiting to reach consensus.
     *
     * @param permissions
     * @param addresses
     * @param verbose
     * @return a List information about specific permissions only list the
     * permissions for particular addresses
     * @throws MultichainException
     */
    public static String executeListPermissions(MultichainPermissions[] permissions, String[] addresses,
            boolean verbose) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);
        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }

        String addressesFormated = formatedArray(addresses, false);

        return execute(MultichainCommand.LISTPERMISSIONS, permissionsFormated, addressesFormated,
                String.valueOf(verbose));
    }

    /**
     * Revoke permissions to addresses
     *
     * @param addresses
     * @param permissions This permissions will be revoke to all addresses who
     * are send in parameter
     * @return the txid of the transaction revoking the permissions
     * @throws MultichainException
     */
    public static String executeRevoke(String[] addresses, MultichainPermissions[] permissions)
            throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);

        String addressesFormated = formatedArray(addresses, false);

        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }
        return execute(MultichainCommand.REVOKE, addressesFormated, permissionsFormated);
    }

    /**
     * This works like revoke, but with control over the from-address used to
     * grant the permissions. If there are multiple addresses with administrator
     * permissions on one node, this allows control over which address is used.
     *
     * @param fromAddress
     * @param toAddresses
     * @param permissions This permissions will be revoke to all addresses who
     * are send in parameter
     * @return the txid of the transaction granting the permissions
     * @throws MultichainException
     */
    public static String executeRevokeFrom(String fromAddress, String[] toAddresses,
            MultichainPermissions[] permissions) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("toAddress", toAddresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("toAddress", toAddresses);
        MultichainTestParameter.arrayIsNotNullOrEmpty("permissions", permissions);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("permissions", permissions);
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        String addressesFormated = formatedArray(toAddresses, false);

        String permissionsFormated = "";
        for (int i = 0; i < permissions.length; i++) {
            permissionsFormated = permissionsFormated.concat(permissions[i].toString().toLowerCase());
            if (i < permissions.length - 1) {
                permissionsFormated = permissionsFormated.concat(",");
            }
        }

        return execute(MultichainCommand.REVOKEFROM, fromAddress, addressesFormated, permissionsFormated);
    }

    /**
     * Creates a new asset on the blockchain, sending the initial qty units to
     * address. If open is true then additional units can be issued in future by
     * the same key which signed the original issuance, via the issuemore or
     * issuemorefrom command.
     *
     * @param address
     * @param assetName
     * @param open
     * @param quantity
     * @return
     * @throws MultichainException
     */
    public static String executeIssue(String address, String assetName, boolean open, int quantity)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
        MultichainTestParameter.intValueIsPositive("quantity", quantity);

        String assetParam = "{\"name\":\"" + assetName + "\",\"open\":" + open + "}";

        return execute(MultichainCommand.ISSUE, address, assetParam, String.valueOf(quantity));
    }



    /**
     * Creates a new asset on the blockchain, sending the initial qty units to
     * address. If open is true then additional units can be issued in future by
     * the same key which signed the original issuance, via the issuemore or
     * issuemorefrom command.
     *
     * @param address
     * @param assetName
     * @param open
     * @param quantity
     * @param unit the smallest transactable unit is given by units, e.g. 0.01.
     * @return
     * @throws MultichainException
     */
    public static String executeIssue(String address, String assetName, boolean open, int quantity, float unit)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
        MultichainTestParameter.intValueIsPositive("quantity", quantity);
        MultichainTestParameter.floatValueIsPositive("unit", unit);
        String assetParam = "'{" + "name:" + ":" + assetName + "," + "open" + ":" + open + "}'";

        return execute(MultichainCommand.ISSUE, address, assetParam, String.valueOf(quantity), String.valueOf(unit));
    }


  public static String executeIssue(String address, String assetName, boolean open, int quantity, float unit, String details)
          throws MultichainException {
      MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
      MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
      MultichainTestParameter.intValueIsPositive("quantity", quantity);
      MultichainTestParameter.floatValueIsPositive("unit", unit);
      String assetParam = "'{" + "name:" + ":" + assetName + "," + "open" + ":" + open + "}'";


      return execute(MultichainCommand.ISSUE, address, assetParam, String.valueOf(quantity), String.valueOf(unit), details);
  }

    /**
     * This works like issue, but with control over the from-address used to
     * issue the asset. If there are multiple addresses with asset issuing
     * permissions on one node, this allows control over which address is used.
     *
     * @param toAddress
     * @param assetName
     * @param open
     * @param quantity
     * @return
     * @throws MultichainException
     */
    public static String executeIssueFrom(String fromAddress, String toAddress, String assetName, boolean open,
            int quantity) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("toAddress", toAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
        MultichainTestParameter.intValueIsPositive("quantity", quantity);

        String assetParam = "{\"name:\":" + assetName + ",\"open\":" + open + "}";
        return execute(MultichainCommand.ISSUEFROM, fromAddress, toAddress, assetParam, String.valueOf(quantity));
    }

    /**
     * This works like issue, but with control over the from-address used to
     * issue the asset. If there are multiple addresses with asset issuing
     * permissions on one node, this allows control over which address is used.
     *
     * @param toAddress
     * @param assetName
     * @param open
     * @param quantity
     * @param unit the smallest transactable unit is given by units, e.g. 0.01.
     * @return
     * @throws MultichainException
     */
    public static String executeIssueFrom(String fromAddress, String toAddress, String assetName, boolean open,
            int quantity, float unit) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("toAddress", toAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
        MultichainTestParameter.intValueIsPositive("quantity", quantity);
        MultichainTestParameter.floatValueIsPositive("unit", unit);
        String assetParam = "{\"name:\":" + assetName + ",\"open\":" + open + "}";
        return execute(MultichainCommand.ISSUE, toAddress, assetParam, String.valueOf(quantity), String.valueOf(unit));
    }

    /**
     * Issues qty additional units of asset, sending them to address. The asset
     * can be specified using its name, ref or issuance txid
     *
     * @param address
     * @param assetName
     * @param quantity
     * @return
     * @throws MultichainException
     */
    public static String executeIssueMore(String fromAddress, String toAddress, String assetName, int quantity) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("toAddress", toAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
        MultichainTestParameter.intValueIsPositive("quantity", quantity);
        return execute(MultichainCommand.ISSUEMOREFROM, fromAddress, toAddress, String.valueOf(quantity));
    }

    /**
     * Issues qty additional units of asset, sending them to address. The asset
     * can be specified using its name, ref or issuance txid
     *
     * @param address
     * @param assetName
     * @param quantity
     * @return
     * @throws MultichainException
     */
    public static String executeIssueMore(String address, String assetName, int quantity) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
        MultichainTestParameter.intValueIsPositive("quantity", quantity);
        return execute(MultichainCommand.ISSUEMORE, address, address, String.valueOf(quantity));
    }

    /**
     *
     * @return information about all assets issued on the blockchain. Provide an
     * asset name, ref or issuance txid
     */
    public static String executeListAssets() {
        return execute(MultichainCommand.LISTASSETS);
    }

    /**
     *
     * @param asset
     * @return information about assets issued on the blockchain. Provide an
     * asset name, ref or issuance txid
     * @throws MultichainException
     */
    public static String executeListAssets(String asset) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("asset", asset);
        return execute(MultichainCommand.LISTASSETS, asset);
    }

    /**
     *
     * @param asset
     * @param verbose
     * @return information about assets issued on the blockchain. Provide an
     * asset name, ref or issuance txid
     * @throws MultichainException
     */
    public static String executeListAssets(String asset, boolean verbose) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("asset", asset);
        return execute(MultichainCommand.LISTASSETS, asset, String.valueOf(verbose));
    }

    /**
     *
     * @param verbose
     * @return information about assets issued on the blockchain. Provide an
     * asset name, ref or issuance txid
     */
    public static String executeListAssets(boolean verbose) {
        return execute(MultichainCommand.LISTASSETS, "*", String.valueOf(verbose));
    }

    /**
     *
     * @param address
     * @return a list of all the asset balances for address in this node’s
     * wallet
     * @throws MultichainException
     */
    public static String executeGetAddressBalances(String address) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        return execute(MultichainCommand.GETADDRESSBALANCES, address);
    }

    /**
     * Provides information about transaction txid related to address in this
     * node’s wallet, including how it affected that address’s balance.
     *
     * @param address
     * @param txid
     * @return
     * @throws MultichainException
     */
    public static String executeGetAddressTransaction(String address, String txid) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        return execute(MultichainCommand.GETADDRESSTRANSACTION, address, txid);
    }

    /**
     * Provides information about transaction txid related to address in this
     * node’s wallet, including how it affected that address’s balance.
     *
     * @param address
     * @param txid
     * @return
     * @throws MultichainException
     */
    public static String executeGetAddressTransaction(String address, String txid, boolean verbose)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        return execute(MultichainCommand.GETADDRESSTRANSACTION, address, txid, String.valueOf(verbose));
    }

    /**
     * Returns a list of balances of all addresses in this node’s wallet
     *
     * @return
     */
    public static String executeGetMultiBalances() {
        return execute(MultichainCommand.GETMULTIBALANCES);
    }

    /**
     * Returns a list of balances of the addresses in this node’s wallet
     *
     * @return
     * @throws MultichainException
     */
    public static String executeGetMultiBalances(String[] addresses) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);

        String addressesFormated = formatedArray(addresses, false);
        return execute(MultichainCommand.GETMULTIBALANCES, addressesFormated);
    }

    /**
     * Returns a list of balances of the addresses in this node’s wallet with
     * the specified assets
     *
     * @return
     * @throws MultichainException
     */
    public static String executeGetMultiBalances(String[] addresses, String[] assets) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets", assets);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets", assets);
        String addressesFormated = formatedArray(addresses, false);
        String assetsFormated = formatedArray(assets, true);
        return execute(MultichainCommand.GETMULTIBALANCES, addressesFormated, assetsFormated);
    }

    /**
     * Returns a list of balances of the addresses in this node’s wallet with
     * the specified assets
     *
     * @return
     * @throws MultichainException
     */
    public static String executeGetMultiBalances(String[] addresses, String[] assets, boolean includeWatchOnly)
            throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets", assets);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets", assets);
        String addressesFormated = formatedArray(addresses, false);
        String assetsFormated = formatedArray(assets, true);
        return execute(MultichainCommand.GETMULTIBALANCES, addressesFormated, assetsFormated, "1",
                String.valueOf(includeWatchOnly));
    }

    /**
     * Returns a list of balances of all the addresses in this node’s wallet
     * with the specified assets
     *
     * @return
     * @throws MultichainException
     */
    public static String executeGetMultiBalancesForAsset(String[] assets) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets", assets);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets", assets);
        String assetsFormated = formatedArray(assets, true);
        return execute(MultichainCommand.GETMULTIBALANCES, "*", assetsFormated);
    }

    /**
     * Returns a list of balances of all the addresses in this node’s wallet
     * with the specified assets
     *
     * @return
     * @throws MultichainException
     */
    public static String executeGetMultiBalancesForAsset(String[] assets, boolean includeWatchOnly)
            throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets", assets);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets", assets);
        String assetsFormated = formatedArray(assets, true);
        return execute(MultichainCommand.GETMULTIBALANCES, "*", assetsFormated, "1",
                String.valueOf(includeWatchOnly));
    }

    /**
     *
     * @return a list of all the asset balances in this node’s wallet
     */
    public static String executeGetTotalBalances() {
        return execute(MultichainCommand.GETTOTALBALANCES);
    }

    /**
     *
     * @return a list of all the asset balances in this node’s wallet
     */
    public static String executeGetTotalBalances(boolean includeWatchOnly) {
        return execute(MultichainCommand.GETTOTALBALANCES, String.valueOf(includeWatchOnly));
    }

    /**
     * Provides information about transaction txid in this node’s wallet,
     * including how it affected the node’s total balance.
     *
     * @param txid
     * @return
     * @throws MultichainException
     */
    public static String executeGetWalletTransaction(String txid) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        return execute(MultichainCommand.GETWALLETTRANSACTION, txid);
    }

    /**
     * Provides information about transaction txid in this node’s wallet,
     * including how it affected the node’s total balance.
     *
     * @param txid
     * @param includeWatchOnly
     * @return
     * @throws MultichainException
     */
    public static String executeGetWalletTransaction(String txid, boolean includeWatchOnly) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        return execute(MultichainCommand.GETWALLETTRANSACTION, txid, String.valueOf(includeWatchOnly));
    }

    /**
     * Provides information about transaction txid in this node’s wallet,
     * including how it affected the node’s total balance.
     *
     * @param txid
     * @param includeWatchOnly
     * @param verbose
     * @return
     * @throws MultichainException
     */
    public static String executeGetWallletTransaction(String txid, boolean includeWatchOnly, boolean verbose)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        return execute(MultichainCommand.GETWALLETTRANSACTION, txid, String.valueOf(includeWatchOnly),
                String.valueOf(verbose));
    }

    /**
     * Lists information about the 10 most recent transactions related to
     * address in this node’s wallet, including how they affected that address’s
     * balance.
     *
     * @param address
     * @return
     * @throws MultichainException
     */
    public static String executeListAddressTransactions(String address) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        return execute(MultichainCommand.LISTADDRESSTRANSACTIONS, address);
    }

    /**
     * Lists information about the count most recent transactions related to
     * address in this node’s wallet, including how they affected that address’s
     * balance.
     *
     * @param address
     * @param count
     * @return
     * @throws MultichainException
     */
    public static String executeListAddressTransactions(String address, int count) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.intValueIsPositive("count", count);
        return execute(MultichainCommand.LISTADDRESSTRANSACTIONS, address, String.valueOf(count));
    }

    /**
     * Lists information about the count most recent transactions,Use skip to go
     * back further in history, related to address in this node’s wallet,
     * including how they affected that address’s balance.
     *
     * @param address
     * @param count
     * @param skip
     * @return
     * @throws MultichainException
     */
    public static String executeListAddressTransactions(String address, int count, int skip)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.intValueIsPositive("count", count);
        MultichainTestParameter.intValueIsPositive("skip", skip);
        return execute(MultichainCommand.LISTADDRESSTRANSACTIONS, address, String.valueOf(count), String.valueOf(skip));
    }

    /**
     * Lists information about the count most recent transactions,Use skip to go
     * back further in history, related to address in this node’s wallet,
     * including how they affected that address’s balance.
     *
     * @param address
     * @param count
     * @param skip
     * @param verbose
     * @return
     * @throws MultichainException
     */
    public static String executeListAddressTransactions(String address, int count, int skip, boolean verbose)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.intValueIsPositive("count", count);
        MultichainTestParameter.intValueIsPositive("skip", skip);
        return execute(MultichainCommand.LISTADDRESSTRANSACTIONS, address, String.valueOf(count), String.valueOf(skip),
                String.valueOf(verbose));
    }

    /**
     * Lists information about the 10 most recent transactions in this node’s
     * wallet, including how they affected the node’s total balance.
     *
     * @return
     */
    public static String executeListWalletTransaction() {
        return execute(MultichainCommand.LISTWALLETTRANSACTIONS);
    }

    /**
     * Lists information about the count most recent transactions in this node’s
     * wallet, including how they affected the node’s total balance.
     *
     * @param count
     * @return
     * @throws MultichainException
     */
    public static String executeListWalletTransaction(int count) throws MultichainException {
        MultichainTestParameter.intValueIsPositive("count", count);
        return execute(MultichainCommand.LISTWALLETTRANSACTIONS, String.valueOf(count));
    }

    /**
     * Lists information about the count most recent transactions;Use skip to go
     * back further in history; in this node’s wallet, including how they
     * affected the node’s total balance.
     *
     * @param count
     * @param skip
     * @return
     * @throws MultichainException
     */
    public static String executeListWalletTransaction(int count, int skip) throws MultichainException {
        MultichainTestParameter.intValueIsPositive("count", count);
        MultichainTestParameter.intValueIsPositive("skip", skip);
        return execute(MultichainCommand.LISTWALLETTRANSACTIONS, String.valueOf(count), String.valueOf(skip));
    }

    /**
     * Lists information about the count most recent transactions;Use skip to go
     * back further in history; in this node’s wallet, including how they
     * affected the node’s total balance.
     *
     * @param count
     * @param skip
     * @param includeWatchOnly
     * @return
     * @throws MultichainException
     */
    public static String executeListWalletTransaction(int count, int skip, boolean includeWatchOnly)
            throws MultichainException {
        MultichainTestParameter.intValueIsPositive("count", count);
        MultichainTestParameter.intValueIsPositive("skip", skip);

        return execute(MultichainCommand.LISTWALLETTRANSACTIONS, String.valueOf(count), String.valueOf(skip),
                String.valueOf(includeWatchOnly));
    }

    /**
     * Lists information about the count most recent transactions;Use skip to go
     * back further in history; in this node’s wallet, including how they
     * affected the node’s total balance.
     *
     * @param count
     * @param skip
     * @param includeWatchOnly
     * @return
     * @throws MultichainException
     */
    public static String executeListWalletTransaction(int count, int skip, boolean includeWatchOnly, boolean verbose)
            throws MultichainException {
        MultichainTestParameter.intValueIsPositive("count", count);
        MultichainTestParameter.intValueIsPositive("skip", skip);

        return execute(MultichainCommand.LISTWALLETTRANSACTIONS, String.valueOf(count), String.valueOf(skip),
                String.valueOf(includeWatchOnly), String.valueOf(verbose));
    }

    /**
     * Sends assets to address, returning the txid.
     *
     * @param fromAddress
     * @param toAddress
     * @param assetName
     * @param quantity
     * @return
     * @throws MultichainException
     */
    public static String executeSendAssetFrom(String fromAddress, String toAddress, String assetName, float quantity)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("toAddress", toAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
        MultichainTestParameter.floatValueIsPositive("quantity", quantity);
        return execute(MultichainCommand.SENDASSETFROM, fromAddress, toAddress, assetName, String.valueOf(quantity));
    }

    /**
     * Sends assets to address, returning the txid.
     *
     * @param address
     * @param assetName
     * @param quantity
     * @return
     * @throws MultichainException
     */
    public static String executeSendAssetToAddress(String address, String assetName, float quantity)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("assetName", assetName);
        MultichainTestParameter.floatValueIsPositive("quantity", quantity);
        return execute(MultichainCommand.SENDASSETTOADDRESS, address, assetName, String.valueOf(quantity));
    }

    /**
     * Sends assets to address, returning the txid.
     *
     * @param address
     * @param assetName
     * @param quantity
     * @return
     * @throws MultichainException
     */
    public static String executeSendFromAddress(String toAddress, String fromAddress, String[] assetsNames,
            float[] quantities) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("toAddress", toAddress);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assetsNames", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assetsNames", assetsNames);
        if (assetsNames.length == quantities.length) {
            return execute(MultichainCommand.SENDFROMADDRESS, fromAddress, toAddress,
                    formatedAmount(assetsNames, quantities));
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     *
     * Sends one or more assets to address, returning the txid.
     *
     * @param address
     * @param assetsNames
     * @param quantities
     * @return
     * @throws MultichainException
     */
    public static String executeSendToAddress(String address, String[] assetsNames, float[] quantities)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assetsNames", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assetsNames", assetsNames);
        MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);

        if (quantities.length == assetsNames.length) {
            return execute(MultichainCommand.SENDTOADDRESS, address, formatedAmount(assetsNames, quantities));
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     * This works like sendtoaddress, but includes the data-hex hexadecimal
     * metadata in an additional OP_RETURN transaction output.
     *
     * @param address
     * @param assetsNames
     * @param quantities
     * @param hexMetaData
     * @return
     * @throws MultichainException
     */
    public static String executeSendWithMetaData(String address, String[] assetsNames, float[] quantities,
            String hexMetaData) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("hexMetaData", hexMetaData);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assetsNames", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assetsNames", assetsNames);
        if (quantities.length == assetsNames.length) {
            MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);

            return execute(MultichainCommand.SENDWITHMETADATA, address, formatedAmount(assetsNames, quantities),
                    hexMetaData);
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     * This works like sendtoaddress, but with control over the from-address
     * whose funds are used, and with the data-hex hexadecimal metadata added in
     * an additional OP_RETURN transaction output. Any change from the
     * transaction is sent back to from-address.
     *
     * @param address
     * @param assetsNames
     * @param quantities
     * @param hexMetaData
     * @return
     * @throws MultichainException
     */
    public static String executeSendWithMetaDataFrom(String fromAddress, String toAddress, String[] assetsNames,
            float[] quantities, String hexMetaData) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("toAddress", toAddress);
        MultichainTestParameter.stringIsNotNullOrEmpty("hexMetaData", hexMetaData);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assetsNames", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assetsNames", assetsNames);
        if (quantities.length == assetsNames.length) {
            MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);

            return execute(MultichainCommand.SENDWITHMETADATAFROM, fromAddress, toAddress,
                    formatedAmount(assetsNames, quantities), hexMetaData);
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     * Adds to the raw atomic exchange transaction in hexstring given by a
     * previous call to createrawexchange or appendrawexchange. This adds an
     * offer to exchange the asset/s in output vout of transaction txid for qty
     * units of asset, where asset is an asset name, ref or issuance txid.
     *
     * @param hexString
     * @param txid
     * @param vout
     * @param assetsNames
     * @param quantities
     * @return
     * @throws MultichainException
     */
    public static String executeAppendRawExchange(String hexString, String txid, int vout, String[] assetsNames,
            float[] quantities) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hexString", hexString);
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        MultichainTestParameter.floatValueIsPositive("vout", vout);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets names", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets Names", assetsNames);
        if (quantities.length == assetsNames.length) {
            MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);
            return execute(MultichainCommand.APPENDRAWEXCHANGE, hexString, txid, String.valueOf(vout),
                    formatedAmount(assetsNames, quantities));
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     *
     * Creates a new atomic exchange transaction which offers to exchange the
     * asset/s in output vout of transaction txid for qty units of asset, where
     * asset is an asset name, ref or issuance txid.
     *
     * @param txid
     * @param vout
     * @param assetsNames
     * @param quantities
     * @return
     * @throws MultichainException
     */
    public static String executeCreateRawExchange(String txid, int vout, String[] assetsNames, float[] quantities)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        MultichainTestParameter.floatValueIsPositive("vout", vout);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets names", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets Names", assetsNames);
        if (quantities.length == assetsNames.length) {
            MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);
            return execute(MultichainCommand.CREATERAWEXCHANGE, txid, String.valueOf(vout),
                    formatedAmount(assetsNames, quantities));
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     * Decodes the raw exchange transaction in hexstring, given by a previous
     * call to createrawexchange or appendrawexchange. Returns details on the
     * offer represented by the exchange and its present state
     *
     * @param hexString
     * @return
     * @throws MultichainException
     */
    public static String executeDecodeRawExchange(String hexString) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hexString", hexString);
        return execute(MultichainCommand.DECODERAWEXCHANGE, hexString);
    }

    /**
     * Decodes the raw exchange transaction in hexstring, given by a previous
     * call to createrawexchange or appendrawexchange. Returns details on the
     * offer represented by the exchange and its present state
     *
     * @param hexString
     * @return
     * @throws MultichainException
     */
    public static String executeDecodeRawExchange(String hexString, boolean verbose) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hexString", hexString);
        return execute(MultichainCommand.DECODERAWEXCHANGE, hexString, String.valueOf(verbose));
    }

    /**
     * Sends a transaction to disable the offer of exchange in hexstring,
     * returning the txid
     *
     * @param hexString
     * @return
     * @throws MultichainException
     */
    public static String executeDisableRawTransaction(String hexString) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hexString", hexString);
        return execute(MultichainCommand.DISABLERAWTRANSACTION, hexString);
    }

    /**
     * Prepares an unspent transaction output containing qty units of asset,
     * where asset is an asset name, ref or issuance txid
     *
     * @param assetsNames
     * @param quantities
     * @throws MultichainException
     */
    public static String executePrepareLockUnspent(String[] assetsNames, float[] quantities)
            throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets names", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets Names", assetsNames);
        if (quantities.length == assetsNames.length) {
            MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);
            return execute(MultichainCommand.PREPARELOCKUNSPENT, formatedAmount(assetsNames, quantities));
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     * Prepares an unspent transaction output containing qty units of asset,
     * where asset is an asset name, ref or issuance txid
     *
     * @param assetsNames
     * @param quantities
     * @param lock
     * @throws MultichainException
     */
    public static String executePrepareLockUnspent(String[] assetsNames, float[] quantities, boolean lock)
            throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets names", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets Names", assetsNames);
        if (quantities.length == assetsNames.length) {
            MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);
            return execute(MultichainCommand.PREPARELOCKUNSPENT, formatedAmount(assetsNames, quantities),
                    String.valueOf(lock));
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     * This works like preparelockunspent, but with control over the
     * from-address whose funds are used to prepare the unspent transaction
     * output.
     *
     * @param fromAddress
     * @param assetsNames
     * @param quantities
     * @return
     * @throws MultichainException
     */
    public static String executePrepareLockUnspentFrom(String fromAddress, String[] assetsNames, float[] quantities)
            throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets names", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets Names", assetsNames);
        if (quantities.length == assetsNames.length) {
            MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);
            return execute(MultichainCommand.PREPARELOCKUNSPENTFROM, fromAddress,
                    formatedAmount(assetsNames, quantities));
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     * This works like preparelockunspent, but with control over the
     * from-address whose funds are used to prepare the unspent transaction
     * output.
     *
     * @param fromAddress
     * @param assetsNames
     * @param quantities
     * @param lock
     * @return
     * @throws MultichainException
     */
    public static String executePrepareLockUnspentFrom(String fromAddress, String[] assetsNames, float[] quantities,
            boolean lock) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("fromAddress", fromAddress);
        MultichainTestParameter.arrayIsNotNullOrEmpty("assets names", assetsNames);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("assets Names", assetsNames);
        if (quantities.length == assetsNames.length) {
            MultichainTestParameter.floatArrayContainNullOrNegativeValue("quantities", quantities);
            return execute(MultichainCommand.PREPARELOCKUNSPENTFROM, fromAddress,
                    formatedAmount(assetsNames, quantities), String.valueOf(lock));
        } else {
            throw new MultichainException("assets names array", "doesn't have the same size as quantities array");
        }
    }

    /**
     * ends transactions to combine large groups of unspent outputs (UTXOs)
     * belonging to the same address into a single unspent output, returning a
     * list of txids
     *
     * @return
     */
    public static String executeCombineUnspent() {
        return execute(MultichainCommand.COMBINEUNPSENT);
    }

    /**
     * Returns a list of locked unspent transaction outputs in the wallet
     *
     * @return
     */
    public static String executeListLockUnspent() {
        return execute(MultichainCommand.LISTLOCKUNPSENT);
    }

    /**
     * Returns a list of locked unspent transaction outputs in the wallet
     *
     * @param minconf
     * @return
     * @throws MultichainException
     */
    public static String executeListUnspent(int minconf) throws MultichainException {
        MultichainTestParameter.intValueIsPositive("minconf", minconf);
        return execute(MultichainCommand.LISTUNSPENT, String.valueOf(minconf));
    }

    /**
     * Returns a list of locked unspent transaction outputs in the wallet
     *
     * @param minconf
     * @param maxconf
     * @return
     * @throws MultichainException
     */
    public static String executeListUnspent(int minconf, int maxconf) throws MultichainException {
        MultichainTestParameter.intValueIsPositive("minconf", minconf);
        MultichainTestParameter.intValueIsPositive("maxconf", maxconf);
        return execute(MultichainCommand.LISTUNSPENT, String.valueOf(minconf), String.valueOf(maxconf));
    }

    /**
     * Returns a list of locked unspent transaction outputs in the wallet
     *
     * @param minconf
     * @param maxconf
     * @param addresses
     * @return
     * @throws MultichainException
     */
    public static String executeListUnspent(int minconf, int maxconf, String[] addresses) throws MultichainException {
        MultichainTestParameter.intValueIsPositive("minconf", minconf);
        MultichainTestParameter.intValueIsPositive("maxconf", maxconf);
        MultichainTestParameter.arrayIsNotNullOrEmpty("addresses", addresses);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("addresses", addresses);
        return execute(MultichainCommand.LISTUNSPENT, String.valueOf(minconf), String.valueOf(maxconf),
                formatedArray(addresses, true));
    }

    /**
     * If unlock is false, locks the specified transaction outputs in the
     * wallet, so they will not be used for automatic coin selection
     *
     * @param lock
     * @return
     */
    public static String executeLockUnspent(boolean unlock) {
        return execute(MultichainCommand.LOCKUNSPENT, String.valueOf(unlock));
    }

    /**
     * If unlock is false, locks the specified transaction outputs in the
     * wallet, so they will not be used for automatic coin selection
     *
     * @param lock
     * @return
     * @throws MultichainException
     */
    public static String executeLockUnspent(boolean unlock, String[] txids, int[] vouts) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("txids", txids);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("txids", txids);
        if (txids.length == vouts.length) {
            MultichainTestParameter.intArrayContainNullOrNegativeValue("vouts", vouts);
            String formatedArray = "[";

            for (int i = 0; i < txids.length; i++) {
                formatedArray = formatedArray.concat("{\"txid\":" + txids[i] + ",\"vout\":" + vouts[i] + "}");
                if (i < txids.length - 1) {
                    formatedArray = formatedArray.concat(",");
                }
            }
            formatedArray = formatedArray.concat("]");
            return execute(MultichainCommand.LOCKUNSPENT, String.valueOf(unlock), formatedArray);
        } else {
            throw new MultichainException("txids array", "doesn't have the same size as vouts array");
        }
    }

    /**
     * Adds a change output to the raw transaction in hexstring given by a
     * previous call to createrawtransaction
     *
     * @param hexString
     * @param address
     * @return
     * @throws MultichainException
     */
    public static String executeAppendRawChange(String hexString, String address) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hexString", hexString);
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        return execute(MultichainCommand.APPENDRAWCHANGE, hexString, address);
    }

    /**
     * Adds a metadata output (using an OP_RETURN) to the raw transaction in
     * tx-hex given by a previous call to createrawtransaction
     *
     * @param txHex
     * @param dataHex
     * @return
     * @throws MultichainException
     */
    public static String executeAppendRawMetaData(String txHex, String dataHex) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txHex", txHex);
        MultichainTestParameter.stringIsNotNullOrEmpty("dataHex", dataHex);
        return execute(MultichainCommand.APPENDROWMETADA, txHex, dataHex);
    }

    /**
     * Creates a transaction spending the specified inputs, sending to the given
     * addresses
     *
     * @param txids
     * @param vouts
     * @param addresses
     * @param amounts
     * @return
     * @throws MultichainException
     */
    public static String executeCreateRawTransaction(String[] txids, int[] vouts, String[] addresses, float[] amounts) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("txids", txids);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("txids", txids);
        if (txids.length == vouts.length) {
            MultichainTestParameter.intArrayContainNullOrNegativeValue("vouts", vouts);
            String formatedArray = "[";

            for (int i = 0; i < txids.length; i++) {
                formatedArray = formatedArray.concat("{\"txid\":" + txids[i] + ",\"vout\":" + vouts[i] + "}");
                if (i < txids.length - 1) {
                    formatedArray = formatedArray.concat(",");
                }
            }
            formatedArray = formatedArray.concat("]");

            return execute(MultichainCommand.CREATERAWTRANSACTION, formatedArray, formatedAmount(addresses, amounts));
        } else {
            throw new MultichainException("txids array", "doesn't have the same size as vouts array");
        }
    }

    /**
     * Returns a JSON object describing the serialized transaction in hexstring
     *
     * @param hexString
     * @return
     * @throws MultichainException
     */
    public static String executeDecodeRawTransaction(String hexString) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hexString", hexString);
        return execute(MultichainCommand.DECODERAWTRANSACTION, hexString);
    }

    /**
     * Validates the raw transaction in hexstring and transmits it to the
     * network, returning the txid.
     *
     * @param hexString
     * @return
     * @throws MultichainException
     */
    public static String executeSendRawTransaction(String hexString) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hexString", hexString);
        return execute(MultichainCommand.SENDRAWTRANSACTION, hexString);
    }

    /**
     * Signs the raw transaction in hexstring, often provided by a previous call
     * to createrawtransaction and (optionally) appendrawmetadata. * @param
     * hexString
     *
     * @return
     * @throws MultichainException
     */
    public static String executeSignRawTransaction(String hexString) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hexString", hexString);
        return execute(MultichainCommand.SIGNTAWTRANSACTION, hexString);
    }

    /**
     * Manually adds or removes a peer-to-peer connection (peers are also
     * discovered and added automatically).
     *
     * @param ip
     * @param port
     * @param command
     * @return
     * @throws MultichainException
     */
    public static String executeAddNode(String ip, int port, MultichainAddNodeCommand command) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("ip", ip);
        MultichainTestParameter.intValueIsPositive("port", port);
        String formatedIpPort = ip + ":" + String.valueOf(port);
        return execute(MultichainCommand.ADDNODE, formatedIpPort, command.toString());
    }

    /**
     * Returns information about the other nodes to which this node is
     * connected.
     *
     * @return
     */
    public static String executeGetPeerInfo() {
        return execute(MultichainCommand.GETPEERINFO);
    }

    /**
     * Sends a ping message to all connected nodes to measure network latency
     * and backlog. The results are received asynchronously and retrieved from
     * the pingtime field of the response to getpeerinfo
     */
    public static void executePing() {
        execute(MultichainCommand.PING);
    }

    /**
     * Returns a base64-encoded digital signature which proves that message was
     * approved by the owner of address
     *
     * @param address
     * @param message
     * @return
     * @throws MultichainException
     */
    public static String executeSignMessage(String address, String message) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("message", message);
        return execute(MultichainCommand.SIGNMESSAGE, address, message);
    }

    /**
     * Verifies that message was approved by the owner of address by checking
     * the base64-encoded digital signature provided by a previous call to
     * signmessage.
     *
     * @param address
     * @param signature
     * @param message
     * @return
     * @throws MultichainException
     */
    public static boolean executeVerifyMessage(String address, String signature, String message) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("address", address);
        MultichainTestParameter.stringIsNotNullOrEmpty("signature", signature);
        MultichainTestParameter.stringIsNotNullOrEmpty("message", message);
        return Boolean.valueOf(execute(MultichainCommand.VERIFYMESSAGE, address, signature, message));
    }

    /**
     * eturns information about the block with hash (retrievable from
     * getblockhash) or at the given height in the active chain
     *
     * @param hashOrHeight
     * @return
     * @throws MultichainException
     */
    public static String executeGetBlock(String hashOrHeight) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hash", hashOrHeight);
        return execute(MultichainCommand.GETBLOCK, hashOrHeight);
    }

    /**
     * eturns information about the block with hash (retrievable from
     * getblockhash) or at the given height in the active chain
     *
     * @param hashOrHeight
     * @param verbose
     * @return
     * @throws MultichainException
     */
    public static String executeGetBlock(String hashOrHeight, boolean verbose) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hash", hashOrHeight);
        return execute(MultichainCommand.GETBLOCK, hashOrHeight, String.valueOf(verbose));
    }

    /**
     * Returns the hash of the block at the given height
     *
     * @param height
     * @return
     * @throws MultichainException
     */
    public static String executeGetBlockHash(String height) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("height", height);
        return execute(MultichainCommand.GETBLOCKHASH, height);
    }

    /**
     *
     * @param txid
     * @return
     * @throws MultichainException
     */
    public static String executeGetRawTransaction(String txid) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        return execute(MultichainCommand.GETRAWTRANSACTION, txid);
    }

    /**
     *
     * @param txid
     * @param verbose
     * @return
     * @throws MultichainException
     */
    public static String executeGetRawTransaction(String txid, boolean verbose) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        return execute(MultichainCommand.GETRAWTRANSACTION, txid, String.valueOf(verbose));
    }

    /**
     * Returns details about an unspent transaction output vout of txid.
     *
     * @param txid
     * @param vout
     * @return
     * @throws MultichainException
     */
    public static String executeGetTxOut(String txid, int vout) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        MultichainTestParameter.intValueIsPositive("vout", vout);
        return execute(MultichainCommand.GETTXOUT, txid, String.valueOf(vout));
    }

    /**
     * Returns details about an unspent transaction output vout of txid.
     *
     * @param txid
     * @param vout
     * @param includeInconfirmed
     * @return
     * @throws MultichainException
     */
    public static String executeGetTxOut(String txid, int vout, boolean includeInconfirmed) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("txid", txid);
        MultichainTestParameter.intValueIsPositive("vout", vout);
        return execute(MultichainCommand.GETTXOUT, txid, String.valueOf(vout), String.valueOf(includeInconfirmed));
    }

    /**
     * Removes all unconfirmed transactions from this node’s memory pool. This
     * can only be called after pause incoming,mining. Successful if no error is
     * returned
     */
    public static String executeClearMemPool() {
        return execute(MultichainCommand.CLEARMEMPOOL);
    }

    /**
     * Pauses the specified tasks
     *
     * @param tasks
     * @return
     * @throws MultichainException
     */
    public static String executePause(String[] tasks) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("tasks", tasks);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("tasks", tasks);
        String tasksFormated = "";
        for (int i = 0; i < tasks.length; i++) {
            tasksFormated = tasksFormated.concat(tasks[i]);
            if (i < tasks.length - 1) {
                tasksFormated = tasksFormated.concat(";");
            }
        }
        return execute(MultichainCommand.PAUSE, tasksFormated);
    }

    /**
     * Resumes the specified tasks, specified as in the pause command.
     *
     * @param tasks
     * @return
     * @throws MultichainException
     */
    public static String executeResume(String[] tasks) throws MultichainException {
        MultichainTestParameter.arrayIsNotNullOrEmpty("tasks", tasks);
        MultichainTestParameter.arrayNotContainNullOrEmptyValues("tasks", tasks);
        String tasksFormated = "";
        for (int i = 0; i < tasks.length; i++) {
            tasksFormated = tasksFormated.concat(tasks[i]);
            if (i < tasks.length - 1) {
                tasksFormated = tasksFormated.concat(";");
            }
        }
        return execute(MultichainCommand.RESUME, tasksFormated);
    }

    /**
     * Rewinds this node’s active chain to height or rewinds/switches to another
     * block with hash.
     *
     * @param heightOrHash
     * @return
     * @throws MultichainException
     */
    public static String executeSetLastBlock(String heightOrHash) throws MultichainException {
        MultichainTestParameter.stringIsNotNullOrEmpty("hashOrHeight", heightOrHash);
        return execute(MultichainCommand.SETLASTBLOCK, heightOrHash);
    }
}
