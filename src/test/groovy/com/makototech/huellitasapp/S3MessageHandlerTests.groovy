package com.makototech.huellitasapp

import com.amazonaws.services.s3.AmazonS3
import com.makototech.huellitasapp.aws.s3.S3Service
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.greaterThan
import static org.hamcrest.Matchers.not
import static org.hamcrest.collection.IsCollectionWithSize.hasSize
import static org.hamcrest.core.IsEqual.equalTo
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString

@RunWith(SpringRunner)
@SpringBootTest
class S3MessageHandlerTests {

    private static final String FOLDER = "test/txt"

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()

    @Autowired
    private S3Service s3Service

    @Autowired
    private AmazonS3 s3Client

    @Test
    void sendFile() {
        def filename = "foo-${UUID.randomUUID()}.txt"

        boolean fileExists = s3Service.doesFileExists(filename)
        assertThat(fileExists, Matchers.is(false))

        def fileToUpload = temporaryFolder.newFile(filename)
        fileToUpload << 'Working with files the Groovy way is easy.\n'

        def originalContent = fileToUpload.readLines()
        assertThat(originalContent, hasSize(greaterThan(0)))

        s3Service.uploadFile(fileToUpload)

        fileExists = s3Service.doesFileExists(filename)
        assertThat(fileExists, Matchers.is(true))

        def url = s3Service.getUrl(filename)
        assertThat(url, not(isEmptyOrNullString()))

        def destination = new File(temporaryFolder.newFolder(), filename)
        s3Service.downloadFile(destination)

        def downloadedContent = destination.readLines()
        assertThat(downloadedContent, hasSize(greaterThan(0)))
        assertThat(originalContent, Matchers.is(equalTo(downloadedContent)))

        // NOTE: Comment the lines until the end of this tests to keep files in the S3 bucket.
        s3Service.deleteFile(filename)

        fileExists = s3Service.doesFileExists(filename)
        assertThat(fileExists, Matchers.is(false))
    }

    @Test
    void sendFileWithKey() {
        def objectKey = "${FOLDER}/foo-${UUID.randomUUID()}.txt"

        boolean fileExists = s3Service.doesFileExists(objectKey)
        assertThat(fileExists, Matchers.is(false))

        def fileToUpload = temporaryFolder.newFile()
        fileToUpload << 'Working with files the Groovy way is easy.\n'

        def originalContent = fileToUpload.readLines()
        assertThat(originalContent, hasSize(greaterThan(0)))

        s3Service.uploadFile(objectKey, fileToUpload)

        fileExists = s3Service.doesFileExists(objectKey)
        assertThat(fileExists, Matchers.is(true))

        def url = s3Service.getUrl(objectKey)
        assertThat(url, not(isEmptyOrNullString()))

        def destination = temporaryFolder.newFile()
        s3Service.downloadFile(objectKey, destination)

        def downloadedContent = destination.readLines()
        assertThat(downloadedContent, hasSize(greaterThan(0)))
        assertThat(originalContent, Matchers.is(equalTo(downloadedContent)))

        // NOTE: Comment the lines until the end of this tests to keep files in the S3 bucket.
        s3Service.deleteFile(objectKey)

        fileExists = s3Service.doesFileExists(objectKey)
        assertThat(fileExists, Matchers.is(false))
    }
}
