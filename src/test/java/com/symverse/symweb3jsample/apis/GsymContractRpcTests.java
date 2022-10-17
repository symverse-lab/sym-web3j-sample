package com.symverse.symweb3jsample.apis;

import com.symverse.library.symweb3j.gsym.protocol.response.sct.SymSCTGetContract;
import com.symverse.library.symweb3j.gsym.protocol.response.sct.SymSCTGetContractAccount;
import com.symverse.symweb3jsample.RPCTestContainer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GsymContractRpcTests extends RPCTestContainer {


    @Test
    public void testGetSCT() throws IOException {
        SymSCTGetContract contract = gSymWeb3j.sctGetContract(testSctAddress).send();

        errorCheck(contract);
        System.out.println(contract.getResult().toString());
    }

    @Test
    public void testGetContractAccount() throws IOException {
        SymSCTGetContractAccount contractAccount = gSymWeb3j.sctGetContractAccount(testSctAddress, "0x0002eb055e4a51cd0002").send();

        errorCheck(contractAccount);
        System.out.println(contractAccount.getBalance());
    }
}
