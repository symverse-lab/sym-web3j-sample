package com.symverse.symweb3jsample;

import com.symverse.library.symweb3j.gsym.GSymCitizen;
import com.symverse.library.symweb3j.gsym.Network;
import com.symverse.library.symweb3j.gsym.node.GsymNode;
import com.symverse.library.symweb3j.gsym.node.PublicWorkNode;
import com.symverse.library.symweb3j.gsym.protocol.SymHttpService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.web3j.protocol.core.Response;

import java.io.IOException;

public class RPCTestContainer {

    protected GSymCitizen gSymWeb3j;

    protected GsymNode testWorkNode;

    protected final String testSctAddress = "0xff89fc8f8050e05fff64";

    @BeforeEach
    public void init() throws IOException {
        testWorkNode = PublicWorkNode.fromNetwork(Network.TEST_NET);
        gSymWeb3j = GSymCitizen.build(new SymHttpService(testWorkNode));

    }

    protected void errorCheck(Response<?> request) {
        Assertions.assertTrue(!request.hasError());
        if (request.getError() != null) {
            System.out.println("========== error: " + request.getError().getMessage());
            System.out.println("========== error: " + request.getError().getCode());
        }
    }
}
