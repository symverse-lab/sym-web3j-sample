package com.symverse.symweb3jsample.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.symverse.library.symweb3j.gsym.GSym;
import com.symverse.library.symweb3j.gsym.Network;
import com.symverse.library.symweb3j.gsym.protocol.response.SymGetTransactionCount;
import com.symverse.library.symweb3j.gsym.protocol.response.SymSendRawTransaction;
import com.symverse.library.symweb3j.gsym.rlp.composer.RLPRawTransaction;
import com.symverse.library.symweb3j.gsym.rlp.composer.sct.Sct;
import com.symverse.library.symweb3j.utils.SymUtil;
import com.symverse.library.symweb3j.utils.crypto.Credentials;
import com.symverse.library.symweb3j.web3j.core.crypto.Wallet;
import com.symverse.library.symweb3j.web3j.core.crypto.WalletFile;
import com.symverse.symweb3jsample.RPCTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Map;

public class GsymSendTransactionTests extends RPCTestContainer {
    public ObjectMapper objectMapper = new ObjectMapper();
    private WalletFile walletFile;

    @Test
    @DisplayName("일반 트랜잭션 전송")
    public void testSendTransaction() throws IOException, CipherException {

        // keystore load
        InputStream inputStream = (GSym.class).getResourceAsStream("/keystore-local.json");
        Map keyStoreMap = objectMapper.readValue(inputStream, Map.class);
        walletFile = objectMapper.convertValue(keyStoreMap, WalletFile.class);
        Credentials credentials = Credentials.create(Wallet.decrypt("FgCdAb2YzVwur", walletFile));

        // symid nonce 값 구하기
        SymGetTransactionCount nonce = gSymWeb3j.symGetTransactionCount(walletFile.getSymId(), DefaultBlockParameterName.PENDING).send();


        // transaction 발생
        RLPRawTransaction rawTransaction = new RLPRawTransaction.Builder()
                .setFrom(walletFile.getSymId())
                .setTo(SymUtil.appendPrefixHex("00024f48413a322b0002"))
                .setNonce(nonce.getTransactionCount())
                .setValue(SymUtil.toSym(1))
                .setWarrantNode(testWorkNode.getNodeBaseSymId())
                .build(credentials, Network.TEST_NET.getChainId());

        SymSendRawTransaction symSendRawTransaction = gSymWeb3j.symSendRawTransaction(rawTransaction.hexSignMessage()).send();

        if ( symSendRawTransaction.hasError()){
            //에러처리
        }

        System.out.println(symSendRawTransaction.getTransactionHash());
    }

    @Test
    @DisplayName("SCT20 생성")
    public void testSendSct() throws IOException, CipherException {

        // keystore load
        InputStream inputStream = (GSym.class).getResourceAsStream("/keystore-local.json");
        Map keyStoreMap = objectMapper.readValue(inputStream, Map.class);
        walletFile = objectMapper.convertValue(keyStoreMap, WalletFile.class);
        Credentials credentials = Credentials.create(Wallet.decrypt("FgCdAb2YzVwur", walletFile));

        // symid nonce 값 구하기
        SymGetTransactionCount nonce = gSymWeb3j.symGetTransactionCount(walletFile.getSymId(), DefaultBlockParameterName.PENDING).send();

        new Sct.Sct21Maker().create("SYMVERSE", "SYM", SymUtil.toSym(1000), BigInteger.ZERO, walletFile.getSymId());


        // sct transaction
        RLPRawTransaction rawTransaction = new RLPRawTransaction.SctBuilder()
                .setSct(new Sct.Sct21Maker().create("SYMVERSE", "SYM", SymUtil.toSym(1000), BigInteger.ZERO, walletFile.getSymId()))
                .setContractAddress(null)
                .setFrom(walletFile.getSymId())
                .setNonce(nonce.getTransactionCount())
                .setWarrantNode(testWorkNode.getNodeBaseSymId())
                .build(credentials, Network.TEST_NET.getChainId());
        SymSendRawTransaction symSendRawTransaction = gSymWeb3j.symSendRawTransaction(rawTransaction.hexSignMessage()).send();
        errorCheck(symSendRawTransaction);
        System.out.println(symSendRawTransaction.getTransactionHash());
    }

    @Test
    @DisplayName("SCT30 생성")
    public void testSendSct30() throws IOException, CipherException {

        // keystore load
        InputStream inputStream = (GSym.class).getResourceAsStream("/keystore-local.json");
        Map keyStoreMap = objectMapper.readValue(inputStream, Map.class);
        walletFile = objectMapper.convertValue(keyStoreMap, WalletFile.class);
        Credentials credentials = Credentials.create(Wallet.decrypt("FgCdAb2YzVwur", walletFile));

        // symid nonce 값 구하기
        SymGetTransactionCount nonce = gSymWeb3j.symGetTransactionCount(walletFile.getSymId(), DefaultBlockParameterName.PENDING).send();

        // sct transaction
        RLPRawTransaction rawTransaction = new RLPRawTransaction.SctBuilder()
                .setSct(new Sct.Sct30Maker().create("SYMVERSE", "SYM", walletFile.getSymId()))
                .setContractAddress(null)
                .setFrom(walletFile.getSymId())
                .setNonce(nonce.getTransactionCount())
                .setWarrantNode(testWorkNode.getNodeBaseSymId())
                .build(credentials, Network.TEST_NET.getChainId());
        SymSendRawTransaction symSendRawTransaction = gSymWeb3j.symSendRawTransaction(rawTransaction.hexSignMessage()).send();
        errorCheck(symSendRawTransaction);
        System.out.println(symSendRawTransaction.getTransactionHash());
    }
}
