package com.makototech.huellitasapp.aws.s3.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.makototech.huellitasapp.aws.s3.S3Service
import com.makototech.huellitasapp.config.S3ConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.aws.outbound.S3MessageHandler
import org.springframework.integration.aws.outbound.S3MessageHandler.Command
import org.springframework.integration.support.MessageBuilder
import org.springframework.messaging.Message
import org.springframework.stereotype.Service

@Service
class S3ServiceImpl implements S3Service {

    @Autowired
    private AmazonS3 s3Client

    @Autowired
    private S3ConfigurationProperties s3ConfigurationProperties

    @Autowired
    private S3MessageHandler s3MessageHandler

    @Override
    void uploadFile(File file) {
        handleMessage(file, Command.UPLOAD)
    }

    @Override
    void downloadFile(File file) {
        handleMessage(file, Command.DOWNLOAD)
    }

    @Override
    void deleteFile(String filename) {
        s3Client.deleteObject(new DeleteObjectRequest(s3ConfigurationProperties.bucket, filename))
    }

    private void handleMessage(File file, Command command) {
        Message<?> message = MessageBuilder.withPayload(file)
                .setHeader('s3Command', command)
                .build()

        def messageHandler = new S3MessageHandler(s3Client, s3ConfigurationProperties.bucket)
        messageHandler.command = command

        messageHandler.handleMessage(message)
    }
}
