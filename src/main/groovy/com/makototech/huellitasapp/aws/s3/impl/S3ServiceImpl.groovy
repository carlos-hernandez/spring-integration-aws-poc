package com.makototech.huellitasapp.aws.s3.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.makototech.huellitasapp.aws.s3.S3Service
import com.makototech.huellitasapp.config.S3ConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.integration.aws.outbound.S3MessageHandler
import org.springframework.integration.aws.outbound.S3MessageHandler.Command
import org.springframework.integration.expression.ValueExpression
import org.springframework.integration.support.MessageBuilder
import org.springframework.messaging.Message
import org.springframework.stereotype.Service

@Service
class S3ServiceImpl implements S3Service {

    @Autowired
    private AmazonS3 s3Client

    @Autowired
    private S3ConfigurationProperties s3ConfigurationProperties

    @Override
    void uploadFile(File file) {
        uploadFile(null, file)
    }

    @Override
    void uploadFile(String objectKey, File file) {
        handleMessage(objectKey, file, Command.UPLOAD)
    }

    @Override
    void downloadFile(File file) {
        downloadFile(null, file)
    }

    @Override
    void downloadFile(String objectKey, File file) {
        handleMessage(objectKey, file, Command.DOWNLOAD)
    }

    @Override
    void deleteFile(String objectKey) {
        s3Client.deleteObject(new DeleteObjectRequest(s3ConfigurationProperties.bucket, objectKey))
    }

    @Override
    boolean doesFileExists(String objectKey) {
        s3Client.doesObjectExist(s3ConfigurationProperties.bucket, objectKey)
    }

    private void handleMessage(String key, File file, Command command) {
        Message<?> message = MessageBuilder.withPayload(file)
                .setHeader('s3Command', command)
                .build()

        def s3MessageHandler = new S3MessageHandler(s3Client, s3ConfigurationProperties.bucket)
        s3MessageHandler.command = command
        if (key) {
            s3MessageHandler.keyExpression = new ValueExpression<>(key)
        }

        s3MessageHandler.handleMessage(message)
    }
}
