<p align="center">
  <img src="https://raw.githubusercontent.com/MasterFlomaster1/SimpleJavaCrypter/master/.github/ic.png" width="250" height="250">
</p>
<h1 align="center">SimpleJavaCrypter</h1>

![GitHub License](https://img.shields.io/github/license/MasterFlomaster1/SimpleJavaCrypter)
![GitHub Release](https://img.shields.io/github/v/release/MasterFlomaster1/SimpleJavaCrypter)

### Description

SJC is a flexible cryptographic tool designed to handle a wide range of encryption tasks. Featuring modern GUI, it supports over 150 encryption algorithms, spanning from ancient Greek ciphers to modern algorithms adopted by the US military and NSA.

### Features

- Utilize hundreds of available encryption algorithms through the [Bouncy Castle](https://www.bouncycastle.org/download/bouncy-castle-java/) library. See full list below.
- Rich toolkit for encrypting text or files with support for variable key lengths, modes (ECB, CBC, CFB, OFB), various paddings (PKCS5Padding, PKCS7Padding, ISO10126Padding), and integration of salts and IVs for enhanced security.
- Generate hashes for text and files, and utilize HMAC (Hash-based Message Authentication Code) for data integrity verification.
- Includes a collection of classic ciphers and ciphers from World Wars (See full list below)
- Modern graphical interface powered by [atlantafx](https://github.com/mkpaz/atlantafx) library with support for multiple themes, animations, and icons.
- Asynchronous operations, leveraging NIO 2 for efficient file handling and processing.
- Specialized password generation tool based on PBKDF2 for modern and secure password hashing.
- Password complexity and strength analysis using the [zxcvbn4j](https://github.com/nulab/zxcvbn4j) library.

### Supported algorithms:
| Type | Algorithms |
| --- | --- |
| Classical Cryptography | ADFGVX, Atbash, Affine, Caesar, Enigma, Playfair, Vigenere |
| Block Ciphers | AES, BLOWFISH, CAMELLIA, CAST5, CAST6, DES, DESEDE, DSTU7624, GOST28147, GOST3412-2015, IDEA, NOEKEON, RC2, RC5, RC6, RIJNDAEL, SEED, SHACAL-2, SKIPJACK, SM4, Serpent, TEA, Threefish-1024, Threefish-256, Threefish-512, Tnepres, Twofish, XTEA |
| Digests | BLAKE2B-160, BLAKE2B-256, BLAKE2B-384, BLAKE2B-512, BLAKE2S-128, BLAKE2S-160, BLAKE2S-224, BLAKE2S-256, BLAKE3-256, DSTU7564-256, DSTU7564-384, DSTU7564-512, GOST3411, GOST3411-2012-256, GOST3411-2012-512, HARAKA-256, HARAKA-512, KECCAK-224, KECCAK-256, KECCAK-288, KECCAK-384, KECCAK-512, MD2, MD4, MD5, PARALLELHASH128-256, PARALLELHASH256-512, RIPEMD128, RIPEMD160, RIPEMD256, RIPEMD320, SHA-1, SHA-224, SHA-256, SHA-384, SHA-512, SHA-512/224, SHA-512/256, SHA3-224, SHA3-256, SHA3-384, SHA3-512, SHAKE128-256, SHAKE256-512, SM3, Skein-1024-1024, Skein-1024-384, Skein-1024-512, Skein-256-128, Skein-256-160, Skein-256-224, Skein-256-256, Skein-512-128, Skein-512-160, Skein-512-224, Skein-512-256, Skein-512-384, Skein-512-512, TIGER, TUPLEHASH128-256, TUPLEHASH256-512, WHIRLPOOL |
| HMAC | HMACDSTU7564-256, HMACDSTU7564-384, HMACDSTU7564-512, HMACGOST3411, HMACGOST3411-2012-256, HMACGOST3411-2012-512, HMACKECCAK224, HMACKECCAK256, HMACKECCAK288, HMACKECCAK384, HMACKECCAK512, HMACMD2, HMACMD4, HMACMD5, HMACRIPEMD128, HMACRIPEMD160, HMACRIPEMD256, HMACRIPEMD320, HMACSHA1, HMACSHA224, HMACSHA256, HMACSHA3-224, HMACSHA3-256, HMACSHA3-384, HMACSHA3-512, HMACSHA384, HMACSHA512, HMACSHA512/224, HMACSHA512/256, HMACSM3, HMACSkein-1024-1024, HMACSkein-1024-384, HMACSkein-1024-512, HMACSkein-256-128, HMACSkein-256-160, HMACSkein-256-224, HMACSkein-256-256, HMACSkein-512-128, HMACSkein-512-160, HMACSkein-512-224, HMACSkein-512-256, HMACSkein-512-384, HMACSkein-512-512, HMACTIGER, HMACWHIRLPOOL |
| PBKDF2 | PBKDF2, PBKDF2WITHASCII, PBKDF2WITHHMACGOST3411, PBKDF2WITHHMACSHA224, PBKDF2WITHHMACSHA256, PBKDF2WITHHMACSHA3-224, PBKDF2WITHHMACSHA3-256, PBKDF2WITHHMACSHA3-384, PBKDF2WITHHMACSHA3-512, PBKDF2WITHHMACSHA384, PBKDF2WITHHMACSHA512, PBKDF2WITHHMACSM3 |

### Usage

1. Download the latest release JAR file
2. ```bash
   java --module-path path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar simple-java-crypter-<version>.jar

The build requires JDK 17 or later.

### Screenshots

![SimpleJavaCrypter](https://raw.githubusercontent.com/MasterFlomaster1/SimpleJavaCrypter/master/.github/im1.png)

![SimpleJavaCrypter](https://raw.githubusercontent.com/MasterFlomaster1/SimpleJavaCrypter/master/.github/im2.png)

![SimpleJavaCrypter](https://raw.githubusercontent.com/MasterFlomaster1/SimpleJavaCrypter/master/.github/im3.png)

![SimpleJavaCrypter](https://raw.githubusercontent.com/MasterFlomaster1/SimpleJavaCrypter/master/content/im4.png)

## Contributing and support

Feel free to contribute by opening issues or creating a pull request. Any help is appreciated :)

