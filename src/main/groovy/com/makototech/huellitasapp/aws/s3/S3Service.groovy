package com.makototech.huellitasapp.aws.s3

interface S3Service {

    void uploadFile(File file)

    void downloadFile(File file)

    void deleteFile(String filename)
}
