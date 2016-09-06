package com.makototech.huellitasapp.aws.s3

interface S3Service {

    void uploadFile(File file)

    void uploadFile(String objectKey, File file)

    void downloadFile(File file)

    void downloadFile(String objectKey, File file)

    void deleteFile(String objectKey)

    boolean doesFileExists(String objectKey)
}
