package com.headhunt.utils.commonutils.mixed;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

/**
 * Created by sagraw001c on 10/25/14.
 */
public class UtilitiesBase64 {

    public static String base64Encode(File file) throws FileNotFoundException, IOException {
        FileInputStream imageInFile = new FileInputStream(file);
        byte imageData[] = new byte[(int) file.length()];
        imageInFile.read(imageData);
        return Base64.encodeBase64String(imageData);
    }

    public static void base64Decode(String stringToDecode, String file) throws FileNotFoundException, IOException {

        byte [] imageByteArray = Base64.decodeBase64(stringToDecode);

        FileOutputStream imageOutFile = new FileOutputStream(file);
        imageOutFile.write(imageByteArray);
        imageOutFile.close();

    }

}
