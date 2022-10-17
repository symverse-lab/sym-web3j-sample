package com.symverse.symweb3jsample.account;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.symverse.library.symweb3j.gsym.GSym;
import com.symverse.library.symweb3j.gsym.GSymCitizen;
import com.symverse.library.symweb3j.gsym.Network;
import com.symverse.library.symweb3j.gsym.SymSign;
import com.symverse.library.symweb3j.gsym.node.GsymNode;
import com.symverse.library.symweb3j.gsym.node.PublicWorkNode;
import com.symverse.library.symweb3j.gsym.protocol.SymHttpService;
import com.symverse.library.symweb3j.gsym.protocol.response.SymGetTransactionCount;
import com.symverse.library.symweb3j.gsym.protocol.response.SymSendRawTransaction;
import com.symverse.library.symweb3j.gsym.protocol.response.SymTransaction;
import com.symverse.library.symweb3j.gsym.protocol.response.citizen.Citizen;
import com.symverse.library.symweb3j.gsym.protocol.response.citizen.SymGetCitizen;
import com.symverse.library.symweb3j.gsym.rlp.composer.RLPComposer;
import com.symverse.library.symweb3j.gsym.rlp.composer.RLPRawTransaction;
import com.symverse.library.symweb3j.gsym.rlp.composer.sct.Sct;
import com.symverse.library.symweb3j.gsym.rlp.parser.RLPParser;
import com.symverse.library.symweb3j.gsym.rlp.parser.RawTransactionStruct;
import com.symverse.library.symweb3j.utils.SymUtil;
import com.symverse.library.symweb3j.utils.crypto.Credentials;
import com.symverse.library.symweb3j.web3j.core.crypto.Wallet;
import com.symverse.library.symweb3j.web3j.core.crypto.WalletFile;
import com.symverse.symweb3jsample.RPCTestContainer;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionDecoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.rlp.RlpDecoder;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ValidationSymIdTests {

    public ObjectMapper objectMapper;

    private WalletFile walletFile;

    protected GSymCitizen gSymWeb3j;

    protected GsymNode testWorkNode;

    @BeforeEach
    public void init() throws IOException {
        Class clazz = GSym.class;
        InputStream inputStream = clazz.getResourceAsStream("/keystore-local.json");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        objectMapper = new ObjectMapper()
                .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setDateFormat(dateFormat);

        Map keyStoreMap = objectMapper.readValue(inputStream, Map.class);
        walletFile = objectMapper.convertValue(keyStoreMap, WalletFile.class);

        testWorkNode = PublicWorkNode.fromNetwork(Network.TEST_NET);
        gSymWeb3j = GSymCitizen.build(new SymHttpService(testWorkNode));
    }
}
