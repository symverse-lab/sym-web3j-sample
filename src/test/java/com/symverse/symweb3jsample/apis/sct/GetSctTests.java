package com.symverse.symweb3jsample.apis.sct;

import com.symverse.library.symweb3j.gsym.protocol.response.sct.SymSCTGetContract;
import com.symverse.library.symweb3j.utils.SymUtil;
import com.symverse.symweb3jsample.RPCTestContainer;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GetSctTests extends RPCTestContainer {


    @Test
    public void getSctToken() throws IOException {
        SymSCTGetContract sctToken = gSymWeb3j.sctGetContract("0xff76e9323d2ce688c962").send();

        errorCheck(sctToken);
        System.out.println(SymUtil.fromHug(sctToken.getTotal()));
    }
}
