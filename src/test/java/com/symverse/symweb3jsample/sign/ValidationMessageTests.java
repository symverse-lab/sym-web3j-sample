package com.symverse.symweb3jsample.sign;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.symverse.library.symweb3j.gsym.GSym;
import com.symverse.library.symweb3j.gsym.GSymCitizen;
import com.symverse.library.symweb3j.gsym.Network;
import com.symverse.library.symweb3j.gsym.SymSign;
import com.symverse.library.symweb3j.gsym.node.GsymNode;
import com.symverse.library.symweb3j.gsym.protocol.SymHttpService;
import com.symverse.library.symweb3j.gsym.protocol.response.citizen.SymGetCitizen;
import com.symverse.library.symweb3j.gsym.rlp.composer.RLPRawTransaction;
import com.symverse.library.symweb3j.gsym.rlp.parser.RLPParser;
import com.symverse.library.symweb3j.gsym.rlp.parser.RawTransactionStruct;
import com.symverse.library.symweb3j.utils.SymUtil;
import com.symverse.library.symweb3j.utils.crypto.Credentials;
import com.symverse.library.symweb3j.utils.crypto.SHA3Hash;
import com.symverse.library.symweb3j.web3j.core.crypto.Wallet;
import com.symverse.library.symweb3j.web3j.core.crypto.WalletFile;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ValidationMessageTests {

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

        testWorkNode = new GsymNode("http://110.10.76.178:8545", "0x0002f4ede5ded7d00002");
        gSymWeb3j = GSymCitizen.build(new SymHttpService(testWorkNode));
    }

    @DisplayName("validate Transaction Message")
    @Test
    public void validationTransactionMessage() throws IOException, CipherException, SignatureException {
        // Own wallet keystore ( 테스트용 keystore )
        Credentials credentials = Credentials.create(Wallet.decrypt("FgCdAb2YzVwur", walletFile));

        // create raw transaction
        RLPRawTransaction rawTransaction = new RLPRawTransaction.Builder()
                .setFrom(walletFile.getSymId())
                .setTo(SymUtil.appendPrefixHex("00024f48413a322b0002"))
                .setNonce(BigInteger.valueOf(1L))
                .setValue(SymUtil.toSym(5))
                .setWarrantNode(testWorkNode.getNodeBaseSymId())
                .build(credentials, Network.TEST_NET.getChainId());
        // sign transaction
        String signedTxHash = rawTransaction.hexSignMessage();

        //decode
        RawTransactionStruct decodeTransaction = RLPParser.rawTxParsing(signedTxHash);

        // get signature data
        SymSign.SignatureData signatureData = decodeTransaction.getSignature();

        BigInteger publicKeyI = SymSign.signedMessageToKey(decodeTransaction.getMessage(Network.TEST_NET.getChainId()), signatureData);
        String publicKeyHash = SymSign.getPublicKeyHashByRecoverKey(publicKeyI);

        SymGetCitizen citizen = gSymWeb3j.citizenBySymId(walletFile.getSymId(), DefaultBlockParameterName.LATEST).send();
        String aKeyPubH = Numeric.cleanHexPrefix(citizen.getResult().getAKeyPubH());
        throwIfPublicKeyNotEqual(publicKeyHash, aKeyPubH);
    }

    @DisplayName("validate Message")
    @Test
    public void validatioMessage() throws CipherException, SignatureException {

        // Own wallet keystore ( 테스트용 keystore )
        Credentials credentials = Credentials.create(Wallet.decrypt("FgCdAb2YzVwur", walletFile));
        System.out.println("credentials >> " + SHA3Hash.sha3(credentials.getEcKeyPair().getPublicKey().toString(16)));

        String message = "Hello Symverse!!!";
        byte[] messageBytes = message.getBytes();
        SymSign.SignatureData signature = SymSign.signMessage(messageBytes, credentials.getEcKeyPair());
        System.out.println(Hex.toHexString(signature.getR()) + " " + Hex.toHexString(signature.getS()));

        // recover
        BigInteger publicKeyI = SymSign.signedMessageToKey(messageBytes, signature);
        String publicKeyHash = SymSign.getPublicKeyHashByRecoverKey(publicKeyI);
        System.out.println("recover publicKeyHash >> " + publicKeyHash);

        Assertions.assertEquals(SymSign.getPublicKeyHashByRecoverKey(credentials.getEcKeyPair().getPublicKey()), publicKeyHash);
    }

    public void throwIfPublicKeyNotEqual(String pub1, String pub2) {
        Assertions.assertEquals(pub1, pub2);
    }

}
