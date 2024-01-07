package com.example.video.util;


import software.amazon.awssdk.services.s3.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.s3.model.S3Object;


import java.io.InputStream;
import java.util.List;

@Component
public class CompareFaces {

    String bucketName = "tcc-imagens";

    private RekognitionClient getRecClient() {
        Region region = Region.US_EAST_1;
        return RekognitionClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(region)
                .build();
    }

    public ResponseEntity<?> compareFaces(InputStream sourceImage, String s3Image) {
        RekognitionClient rekognitionClient = getRecClient();
        Float similarityThreshold = 8F;

        if(!isNumeric(s3Image) || Integer.parseInt(s3Image) > 41){
            s3Image = "1";
        }

        ResponseInputStream<GetObjectResponse> s3Object = S3Service.getClient().getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(s3Image + "-0.png")
                        .build()
        );

        try {
            String match = compareTwoFaces(
                    rekognitionClient,
                    similarityThreshold,
                    sourceImage,
                    s3Object);
            return ResponseEntity.ok(match);

        } catch (RekognitionException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    public void listBucketObjects() {

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse res = S3Service.getClient().listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                System.out.print("\n The name of the key is " + myValue.key());
                System.out.print("\n The object is " + calKb(myValue.size()) + " KBs");
                System.out.print("\n The owner is " + myValue.owner());
            }

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    //convert bytes to kbs.
    private static long calKb(Long val) {
        return val / 1024;
    }


    //Entender como pegar as fotos correspondentes do S3 a partir de algum identificador, podendo ser o nome da foto.
    //Em seguida pensar em uma maneira de ler a imagem sem baixar.

    public static String compareTwoFaces(RekognitionClient rekClient, Float similarityThreshold, InputStream sourceImage, InputStream targetImage) {
        try {

            SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceImage);
            SdkBytes targetBytes = SdkBytes.fromInputStream(targetImage);

            // Create an Image object for the source image.
            Image souImage = Image.builder()
                    .bytes(sourceBytes)
                    .build();

            Image tarImage = Image.builder()
                    .bytes(targetBytes)
                    .build();

            CompareFacesRequest facesRequest = CompareFacesRequest.builder()
                    .sourceImage(souImage)
                    .targetImage(tarImage)
                    .similarityThreshold(similarityThreshold)
                    .build();

            // Compare two images.
            CompareFacesResponse compareFacesResult = rekClient.compareFaces(facesRequest);
            List<CompareFacesMatch> faceDetails = compareFacesResult.faceMatches();
            String result = "Any face found";

            for (CompareFacesMatch match: faceDetails){
                ComparedFace face = match.face();

                result = "Matched Face with " + face.confidence() + "% confidence. " + "and with " + match.similarity() + " of similarity";
            }

            List<ComparedFace> unmatchedFaces = compareFacesResult.unmatchedFaces();
            for (ComparedFace comparedFace: unmatchedFaces){
                Float confidence = comparedFace.confidence();

                result = "Unmatched Face with " + confidence + "% confidence.";
            }

            return result;

        } catch (RekognitionException e) {
            System.out.println("Failed to load source image " + sourceImage);
            System.exit(1);
            return "0";

        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}