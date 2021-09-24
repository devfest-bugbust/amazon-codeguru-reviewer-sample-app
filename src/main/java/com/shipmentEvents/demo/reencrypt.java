

import java.nio.ByteBuffer;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest;


/**
A utility class to decrypt and re-encrypt a ciphered text with a new KMS key.
*/
public class Reencrypt {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Reencrypt <ciphertext> <new-key-id>");
            System.exit(1);
        }

        String ciphertext = args[0];
        String newKeyId = args[1];

        // Create an Amazon KMS client
        AWSKMS kms = AWSKMSClientBuilder.defaultClient();

        // Decrypt the ciphertext
        DecryptRequest decryptRequest = new DecryptRequest()
            .withCiphertextBlob(ByteBuffer.wrap(Base64.getDecoder().decode(ciphertext)));
        byte[] plaintext = kms.decrypt(decryptRequest).getPlaintext().array();

        // Encrypt the plaintext with the new key
        EncryptRequest encryptRequest = new EncryptRequest()
            .withPlaintext(ByteBuffer.wrap(plaintext))
            .withKeyId(newKeyId);
        byte[] ciphertext2 = kms.encrypt(encryptRequest).getCiphertextBlob().array();

        // Print the new ciphertext
        System.out.println(Base64.getEncoder().encodeToString(ciphertext2));
    }
}



