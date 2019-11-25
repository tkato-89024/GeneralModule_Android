/*
 GeneralModule_Android Cipherer

 Copyright (c) 2019 tkato

 This software is released under the MIT License.
 http://opensource.org/licenses/mit-license.php
 */

package jp.co.model.tkato.general_module.cipher;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// reference:
// https://gist.github.com/m1entus/f70d4d1465b90d9ee024

@SuppressWarnings({"unused", "WeakerAccess", "SpellCheckingInspection"})
public class Cipherer {

    private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";
    private static final String ALGORITHM      = "AES";

    @Nullable
    public static String encode(@NonNull String password, @NonNull String text) throws NullPointerException {

        validation(password, text);

        try {
            final SecretKeySpec skeySpec  = getKey(password);
            final byte[]        clearText = text.getBytes(StandardCharsets.UTF_8);

            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            final Cipher cipher  = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

            final String result = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
            return result.trim(); // 行末に改行が含まれているため削除

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String decode(@NonNull String password, @NonNull String text) throws NullPointerException {

        validation(password, text);

        try {
            final SecretKey key = getKey(password);

            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            final byte[] encrypedPwdBytes = Base64.decode(text, Base64.DEFAULT);
            final Cipher cipher           = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

            final byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));
            return new String(decrypedValueBytes);

        } catch (Exception e) {
            // InvalidAlgorithmParameterException, InvalidKeyException
            e.printStackTrace();
            return null;
        }
    }

    private static void validation(@NonNull String password, @NonNull String text) throws NullPointerException {

        if (password.isEmpty()) {
            throw new NullPointerException("\"password\" is null");
        }
        if (text.isEmpty()) {
            throw new NullPointerException("\"text\" is null");
        }
    }

    private static SecretKeySpec getKey(String password) {
        final int    keyLength = 256;
        final byte[] keyBytes  = new byte[keyLength / 8];
        Arrays.fill(keyBytes, (byte) 0x0);

        final byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        final int    length        = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    private Cipherer() {}
}
