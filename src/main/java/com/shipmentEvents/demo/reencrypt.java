/**
A function to re-encrypt a file using a new key.
*/


import java.nio.ByteBuffer;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.EncryptRequest; 

AWSKMS client = AWSKMSClientBuilder.standard().build();
ByteBuffer sourceCipherTextBlob = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});

/**
A function to decrypt and re-encrypt a ciphered text with a new key.
*/
public static void reencrypt(String keyId, ByteBuffer sourceCipherTextBlob) {
    DecryptRequest decryptRequest = new DecryptRequest().withCiphertextBlob(sourceCipherTextBlob);
    EncryptRequest encryptRequest = new EncryptRequest().withKeyId(keyId).withPlaintext(client.decrypt(decryptRequest).getPlaintext());
    client.encrypt(encryptRequest);
}


