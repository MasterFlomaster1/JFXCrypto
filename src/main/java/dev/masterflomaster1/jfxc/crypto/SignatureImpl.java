package dev.masterflomaster1.jfxc.crypto;

import org.bouncycastle.asn1.ua.DSTU4145NamedCurves;
import org.bouncycastle.jcajce.provider.asymmetric.DSTU4145;
import org.bouncycastle.jce.spec.ECNamedCurveGenParameterSpec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SignatureImpl {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static byte[] sign(String algorithm, PrivateKey key, byte[] value) {
        try {
            Signature signature = Signature.getInstance(algorithm, "BC");

            signature.initSign(key, SECURE_RANDOM);
            signature.update(value);

            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String algorithm, PublicKey key, byte[] sign, byte[] value) {
        try {
            Signature signature = Signature.getInstance(algorithm, "BC");
            signature.initVerify(key);
            signature.update(value);
            return signature.verify(sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generateKey(String algorithm) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(getProperKeyGenAlgorithm(algorithm), "BC");
            var keyGenParamsOptional = getAvailableKeyGenParameters(algorithm);

            if (keyGenParamsOptional.isPresent()) {
                keyPairGenerator.initialize(new ECGenParameterSpec(keyGenParamsOptional.get().get(0)));
            } else if ("EDDSA".equals(algorithm)) {
                keyPairGenerator.initialize(255);
            } else if ("DSTU4145".equals(algorithm) || "GOST3411WITHDSTU4145".equals(algorithm) || "GOST3411WITHDSTU4145LE".equals(algorithm)) {
                /*
                [1.2.804.2.1.1.1.1.3.1.1.2.0,
                 1.2.804.2.1.1.1.1.3.1.1.2.1,
                 1.2.804.2.1.1.1.1.3.1.1.2.2,
                 1.2.804.2.1.1.1.1.3.1.1.2.3,
                 1.2.804.2.1.1.1.1.3.1.1.2.4,
                 1.2.804.2.1.1.1.1.3.1.1.2.5,
                 1.2.804.2.1.1.1.1.3.1.1.2.6,
                 1.2.804.2.1.1.1.1.3.1.1.2.7,
                 1.2.804.2.1.1.1.1.3.1.1.2.8,
                 1.2.804.2.1.1.1.1.3.1.1.2.9]
                 */
                keyPairGenerator.initialize(new ECNamedCurveGenParameterSpec("1.2.804.2.1.1.1.1.3.1.1.2.9"));
            }

            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Optional<List<String>> getAvailableKeyGenParameters(String algorithm) {
        return switch (algorithm) {
            case "ECGOST3410", "ECGOST3410-2012-256", "ECGOST3410-2012-512", "GOST3411-2012-256WITHECGOST3410-2012-256",
                 "GOST3411-2012-512WITHECGOST3410-2012-512", "GOST3411WITHECGOST3410" ->
                    Optional.of(List.of("GostR3410-2001-CryptoPro-A", "GostR3410-2001-CryptoPro-B",
                            "GostR3410-2001-CryptoPro-C"));
            default -> Optional.empty();
        };
    }

    private static String getProperKeyGenAlgorithm(String algorithm) {
        return switch (algorithm) {
            case "DDSA", "DETDSA", "NONEWITHDSA", "SHA224WITHDSA", "SHA256WITHDSA", "SHA384WITHDSA", "SHA512WITHDSA",
                 "SHA3-224WITHDSA", "SHA3-256WITHDSA", "SHA3-384WITHDSA", "SHA3-512WITHDSA", "SHA1WITHDSA" -> "DSA";
            case "DSTU4145", "GOST3411WITHDSTU4145", "GOST3411WITHDSTU4145LE" -> "DSTU4145";
            case "ECDDSA", "NONEwithECDSA", "SHA1WITHECDSA", "SHA224WITHECDSA", "SHA256WITHECDSA", "SHA384WITHECDSA",
                 "SHA512WITHECDSA", "SHA3-224WITHECDSA", "SHA3-256WITHECDSA", "SHA3-384WITHECDSA", "SHA3-512WITHECDSA",
                 "SHA1WITHPLAIN-ECDSA", "SHA224WITHPLAIN-ECDSA", "SHA256WITHPLAIN-ECDSA", "SHA384WITHPLAIN-ECDSA",
                 "SHA512WITHPLAIN-ECDSA", "SHA3-224WITHPLAIN-ECDSA", "SHA3-256WITHPLAIN-ECDSA", "SHA3-384WITHPLAIN-ECDSA",
                 "SHA3-512WITHPLAIN-ECDSA", "SHA1WITHECNR", "SHA224WITHECNR", "SHA256WITHECNR", "SHA384WITHECNR",
                 "SHA512WITHECNR", "RIPEMD160WITHECDSA", "RIPEMD160WITHPLAIN-ECDSA", "SHAKE128WITHECDSA",
                 "SHAKE256WITHECDSA", "SHA1WITHCVC-ECDSA", "SHA256WITHCVC-ECDSA", "SHA384WITHCVC-ECDSA",
                 "SHA512WITHCVC-ECDSA", "SHA224WITHCVC-ECDSA" -> "ECDSA";
            case "ECGOST3410", "GOST3411WITHECGOST3410" -> "ECGOST3410";
            case "ECGOST3410-2012-256", "GOST3411-2012-256WITHECGOST3410-2012-256", "ECGOST3410-2012-512",
                 "GOST3411-2012-512WITHECGOST3410-2012-512" -> "ECGOST3410-2012";
            case "EDDSA" -> "EDDSA";
            case "SHA1WITHDDSA", "SHA224WITHDDSA", "SHA256WITHDDSA", "SHA3-224WITHDDSA", "SHA3-256WITHDDSA",
                 "SHA3-384WITHDDSA", "SHA384WITHDDSA", "SHA512WITHDDSA", "RIPEMD160WITHDSA", "SHA1WITHDETDSA",
                 "SHA1WITHECDDSA", "SHA224WITHDETDSA", "SHA224WITHECDDSA", "SHA256WITHDETDSA", "SHA256WITHECDDSA",
                 "SHA3-224WITHECDDSA", "SHA3-256WITHECDDSA", "SHA3-384WITHECDDSA", "SHA3-512WITHDDSA",
                 "SHA3-512WITHECDDSA", "SHA384WITHDETDSA", "SHA384WITHECDDSA", "SHA512WITHDETDSA", "SHA512WITHECDDSA" -> "DSA";
            case "MD2WITHRSA", "MD4WITHRSA", "MD5WITHRSA", "MD5WITHRSA/ISO9796-2", "RAWRSASSA-PSS", "RIPEMD128WITHRSA",
                 "RIPEMD128WITHRSA/X9.31", "RIPEMD160WITHRSA", "RIPEMD160WITHRSA/X9.31", "RIPEMD160withRSA/ISO9796-2",
                 "RIPEMD256WITHRSA", "RMD128WITHRSA", "RMD128WITHRSA/X9.31", "RMD160WITHRSA", "RMD160WITHRSA/X9.31",
                 "RMD256WITHRSA", "SHA1WITHRSA", "SHA1WITHRSA/ISO9796-2", "SHA1WITHRSA/X9.31", "SHA1WITHRSAANDMGF1",
                 "SHA1WITHRSAANDSHAKE128", "SHA1WITHRSAANDSHAKE256", "SHA224WITHRSA", "SHA224WITHRSA/ISO9796-2",
                 "SHA224WITHRSA/X9.31", "SHA224WITHRSAANDMGF1", "SHA224WITHRSAANDSHAKE128", "SHA224WITHRSAANDSHAKE256",
                 "SHA256WITHRSA", "SHA256WITHRSA/ISO9796-2", "SHA256WITHRSA/X9.31", "SHA256WITHRSAANDMGF1",
                 "SHA256WITHRSAANDSHAKE128", "SHA256WITHRSAANDSHAKE256", "SHA3-224WITHRSA", "SHA3-224WITHRSAANDMGF1",
                 "SHA3-224WITHRSAANDSHAKE128", "SHA3-224WITHRSAANDSHAKE256", "SHA3-256WITHRSA", "SHA3-256WITHRSAANDMGF1",
                 "SHA3-256WITHRSAANDSHAKE128", "SHA3-256WITHRSAANDSHAKE256", "SHA3-384WITHRSA", "SHA3-384WITHRSAANDMGF1",
                 "SHA3-384WITHRSAANDSHAKE128", "SHA3-384WITHRSAANDSHAKE256", "SHA3-512WITHRSA", "SHA3-512WITHRSAANDMGF1",
                 "SHA3-512WITHRSAANDSHAKE128", "SHA3-512WITHRSAANDSHAKE256", "SHA384WITHRSA", "SHA384WITHRSA/ISO9796-2",
                 "SHA384WITHRSA/X9.31", "SHA384WITHRSAANDMGF1", "SHA384WITHRSAANDSHAKE128", "SHA384WITHRSAANDSHAKE256",
                 "SHA512(224)WITHRSA", "SHA512(224)WITHRSA/ISO9796-2", "SHA512(224)WITHRSA/X9.31", "SHA512(224)WITHRSAANDMGF1",
                 "SHA512(224)WITHRSAANDSHAKE128", "SHA512(224)WITHRSAANDSHAKE256", "SHA512(256)WITHRSA",
                 "SHA512(256)WITHRSA/ISO9796-2", "SHA512(256)WITHRSA/X9.31", "SHA512(256)WITHRSAANDMGF1",
                 "SHA512(256)WITHRSAANDSHAKE128", "SHA512(256)WITHRSAANDSHAKE256", "SHA512WITHRSA",
                 "SHA512WITHRSA/ISO9796-2", "SHA512WITHRSA/X9.31", "SHA512WITHRSAANDMGF1", "SHA512WITHRSAANDSHAKE128",
                 "SHA512WITHRSAANDSHAKE256", "SHAKE128WITHRSAPSS", "SHAKE256WITHRSAPSS", "WhirlpoolWITHRSA/ISO9796-2",
                 "WhirlpoolWITHRSA/X9.31" -> "RSA";
            case "SHA256WITHSM2", "SM3WITHSM2" -> "SM2";
            default -> algorithm;
        };
    }

}
