package com.symverse.symweb3jsample.account;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.symverse.library.symweb3j.gsym.GSym;
import com.symverse.library.symweb3j.utils.crypto.Credentials;
import com.symverse.library.symweb3j.web3j.core.crypto.Wallet;
import com.symverse.library.symweb3j.web3j.core.crypto.WalletFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Map;


class KeystoreTests {
    public ObjectMapper objectMapper;

    private WalletFile walletFile;

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

        System.out.println(walletFile.getAddress() + walletFile.getCrypto().toString());
    }

    @DisplayName("Keystore Unlock - Success")
    @Test
    public void keystoreUnlock() {
        Assertions.assertDoesNotThrow(() -> {
            Credentials credentials = Credentials.create(Wallet.decrypt("FgCdAb2YzVwur", walletFile));

            System.out.println("===========pri key:" + credentials.getEcKeyPair().getPrivateKey().toString());
            System.out.println("===========pub key: " + credentials.getEcKeyPair().getPublicKey().toString());
        });
    }

    @DisplayName("Keystore Unlock - Failed (password)")
    @Test
    public void keystoreUnlockFail() {
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            Credentials credentials = Credentials.create(Wallet.decrypt("1234", walletFile));
        });

        System.out.println(exception.getMessage());
        System.out.println(exception.getClass());
    }
}
