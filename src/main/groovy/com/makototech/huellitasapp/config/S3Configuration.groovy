package com.makototech.huellitasapp.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.aws.outbound.S3MessageHandler

@Configuration
class S3Configuration {

    @Bean
    @ServiceActivator(inputChannel = 's3DownloadChannel', outputChannel = 's3UploadChannel')
    S3MessageHandler s3MessageHandler(AWSCredentialsProvider awsCredentialsProvider, S3ConfigurationProperties s3ConfigurationProperties) {
        def amazonS3 = amazonS3(awsCredentialsProvider)
        return new S3MessageHandler(amazonS3, s3ConfigurationProperties.bucket)
    }

    @Bean
    public AmazonS3 amazonS3(AWSCredentialsProvider awsCredentialsProvider) {
        new AmazonS3Client(awsCredentialsProvider)
    }
}
