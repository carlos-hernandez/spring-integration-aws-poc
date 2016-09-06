package com.makototech.huellitasapp.config

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Configuration {

    @Bean
    public AmazonS3 amazonS3(AWSCredentialsProvider awsCredentialsProvider) {
        new AmazonS3Client(awsCredentialsProvider)
    }
}
