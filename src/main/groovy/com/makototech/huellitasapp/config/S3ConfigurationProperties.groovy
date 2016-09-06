package com.makototech.huellitasapp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = 'app.aws.s3')
class S3ConfigurationProperties {

    String bucket
}
