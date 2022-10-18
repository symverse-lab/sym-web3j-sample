package com.symverse.symweb3jsample.apis.citizen;

import com.symverse.library.symweb3j.gsym.protocol.response.citizen.SymGetCitizen;
import com.symverse.symweb3jsample.RPCTestContainer;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.io.IOException;

public class GsymCitizenRpcTests extends RPCTestContainer {

    @Test
    public void testGetCitizen() throws IOException {
        SymGetCitizen citizen = gSymWeb3j.citizenBySymId("0x0002eb055e4a51cd0002", DefaultBlockParameterName.LATEST).send();
        errorCheck(citizen);
        System.out.println(citizen.getResult());
    }

}
