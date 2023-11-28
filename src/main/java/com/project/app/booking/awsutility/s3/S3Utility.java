package com.project.app.booking.awsutility.s3;

/*

*Courtesy :https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/java_s3_code_examples.html
**/

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;


public class S3Utility {

    private S3Client s3Client;
    private static final  S3Utility s3Utility = new S3Utility();
    private S3Utility(){
    }

    public static S3Utility build(String region){
        s3Utility.s3Client=S3Client.builder()
                .region(Region.of(region))
                .build();
        return s3Utility;
    }

    public void uploadObject(String bucketName, String objectKey, byte[] file) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(file)));
    }

    public byte[] getObject(String bucketName, String objectKey) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
        try {
            return response.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteObject(String bucketName, String objectKey){
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        s3Client.deleteObject(request);

    }

    public List<S3Object> getAllObjects(String bucketName ) {

        try {
            ListObjectsRequest listObjects = ListObjectsRequest.builder().bucket(bucketName).build();

            List<S3Object> contents = s3Client.listObjects(listObjects).contents();
            return contents;
        } catch (S3Exception e) {
            throw e;
        }

    }

}
