package com.fooddrive.main;

import com.fooddrive.google.GoogleDriveFile;
import com.fooddrive.onedrive.OneDriveFile;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class main {

    public static void main(String... args) throws IOException, GeneralSecurityException {
        /*OneDriveFile onedrive = new OneDriveFile();
        String resOneDrive = onedrive.uploadFile("/home/jonatan/Downloads/Cedula150.pdf","test/Cedula150.pdf");
        String resOneDrive = onedrive.downloadFile("test/Cedula150.pdf","Cedula150.pdf");
        System.out.println(resOneDrive);*/

        GoogleDriveFile google = new GoogleDriveFile();
        //String resGoogle = google.uploadFile("/home/jonatan/Downloads/Cedula150.pdf","Cedula150.pdf");
        String resGoogle = google.uploadFile2("/home/jonatan/Downloads/Cedula150.pdf","Cedula150.pdf");
        //String resGoogle = google.downloadFile2();
        System.out.println(resGoogle);
    }

}
