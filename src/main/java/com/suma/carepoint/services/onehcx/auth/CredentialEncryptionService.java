package com.suma.carepoint.services.onehcx.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class CredentialEncryptionService {

    private static final int IV_LENGTH = 12;
    private static final int TAG_LENGTH_BITS = 128;

    private final SecretKeySpec key;
    private final SecureRandom secureRandom = new SecureRandom();

    public CredentialEncryptionService(
            @Value("${onehcx.encryption-key}") String encodedKey
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);

        if (keyBytes.length != 32) {
            throw new IllegalArgumentException(
                    "OneHcx encryption key must decode to 32 bytes"
            );
        }

        this.key = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    key,
                    new GCMParameterSpec(TAG_LENGTH_BITS, iv)
            );

            byte[] encrypted = cipher.doFinal(
                    plaintext.getBytes(StandardCharsets.UTF_8)
            );

            ByteBuffer buffer = ByteBuffer.allocate(
                    iv.length + encrypted.length
            );
            buffer.put(iv);
            buffer.put(encrypted);

            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to encrypt OneHcx credential", e
            );
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            byte[] input = Base64.getDecoder().decode(encryptedValue);

            if (input.length <= IV_LENGTH) {
                throw new IllegalArgumentException(
                        "Invalid encrypted credential"
                );
            }

            ByteBuffer buffer = ByteBuffer.wrap(input);
            byte[] iv = new byte[IV_LENGTH];
            buffer.get(iv);

            byte[] ciphertext = new byte[buffer.remaining()];
            buffer.get(ciphertext);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    key,
                    new GCMParameterSpec(TAG_LENGTH_BITS, iv)
            );

            return new String(
                    cipher.doFinal(ciphertext),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to decrypt OneHcx credential", e
            );
        }
    }
}
