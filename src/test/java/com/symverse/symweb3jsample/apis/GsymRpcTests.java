package com.symverse.symweb3jsample.apis;

import com.symverse.library.symweb3j.gsym.protocol.response.*;
import com.symverse.library.symweb3j.utils.SymUtil;
import com.symverse.symweb3jsample.RPCTestContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.admin.AdminNodeInfo;

import java.io.IOException;

public class GsymRpcTests extends RPCTestContainer {

    @Test
    public void testGetBalance() throws IOException {
        SymGetBalance balance = gSymWeb3j.symGetBalance("0x00000000000000000001", DefaultBlockParameterName.LATEST).send();

        errorCheck(balance);
        Assertions.assertTrue(balance.getBalanceDecimal().longValue() >= 0);
        System.out.println(balance.getBalanceDecimal());
    }

    @Test
    public void testAdminNodeInfo() throws IOException {
        AdminNodeInfo nodeInfo = gSymWeb3j.adminNodeInfo().send();

        errorCheck(nodeInfo);
        System.out.println(nodeInfo.getResult().getIp() + " " + nodeInfo.getResult().getName());
    }

    @Test
    public void testGetBlock() throws IOException {
        System.out.println(SymUtil.big(SymUtil.hexToNumber("0x1f0cac")));
        SymBlock block = gSymWeb3j.symGetBlockByNumber(DefaultBlockParameter.valueOf(SymUtil.big(SymUtil.hexToNumber("0x1f0cac"))), true).send();

        errorCheck(block);
        System.out.println(block.getBlock().toString());
    }

    @Test
    public void testGetTransaction() throws IOException {
        SymTransaction transaction = gSymWeb3j.symGetTransactionByHash("0x10aac13794eae0488dd034dc8a3774714fb9eccafe931a40429c92604ecace0f").send();

        errorCheck(transaction);
        System.out.println(transaction.getTransaction().get());
    }

    @Test
    public void testGetTransactionCount() throws IOException {
        SymGetTransactionCount count = gSymWeb3j.symGetTransactionCount(SymUtil.appendPrefixHex("0002aea7a09991500002"), DefaultBlockParameterName.LATEST).send();
        errorCheck(count);
        System.out.println(count.getTransactionCount().longValue());
    }

    @Test
    public void testGetTransactionReceipt() throws IOException {
        SymTransactionReceipt transaction = gSymWeb3j.symGetTransactionReceipt("0x10aac13794eae0488dd034dc8a3774714fb9eccafe931a40429c92604ecace0f").send();

        errorCheck(transaction);
        System.out.println(transaction.getTransactionReceipt().get());
    }
}
