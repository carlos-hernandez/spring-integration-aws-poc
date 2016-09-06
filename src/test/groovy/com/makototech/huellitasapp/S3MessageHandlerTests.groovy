package com.makototech.huellitasapp

import com.makototech.huellitasapp.aws.s3.S3Service
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.greaterThan
import static org.hamcrest.collection.IsCollectionWithSize.hasSize
import static org.hamcrest.core.IsEqual.equalTo

@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = Application)
@DirtiesContext
class S3MessageHandlerTests {

    private static final String FILENAME = "foo-${System.currentTimeMillis()}.txt"

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Autowired
    private S3Service s3Service

    @Test
    void sendingMessagesToAmazonS3() {
        File file = temporaryFolder.newFile(FILENAME)
        file << 'Working with files the Groovy way is easy.\n'
        def originalContent = file.readLines()
        assertThat(originalContent, hasSize(greaterThan(0)))

        s3Service.uploadFile(file)

        File tempFolder = temporaryFolder.newFolder()
        File destination = new File(tempFolder, FILENAME)

        s3Service.downloadFile(destination)

        def downloadedContent = destination.readLines()
        assertThat(downloadedContent, hasSize(greaterThan(0)))
        assertThat(originalContent, Matchers.is(equalTo(downloadedContent)))

        s3Service.deleteFile(FILENAME)
    }
}
