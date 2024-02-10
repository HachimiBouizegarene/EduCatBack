package org.hachimi.eduCat.service;

import org.json.JSONArray;

public class GeneralService {


//    public static byte[] BlobToByte (Blob blob) throws SQLException{
//        return blob.getBytes(1, (int) blob.length());
//    }

    public static byte[] JSONarrayToBytes(JSONArray array)
    {
        byte[] bytes = new byte[array.length()];
        for(int i = 0; i < array.length(); i++){
            bytes[i] = (byte) ((int) array.get(i));
        };
        return bytes;
    }
}
