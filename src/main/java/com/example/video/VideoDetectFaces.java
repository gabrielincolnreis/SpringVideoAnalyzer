package com.example.video;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class VideoDetectFaces {

    String topicArn = "arn:aws:sns:us-east-1:967567166871:AmazonRekognition";
    String roleArn = "arn:aws:iam::967567166871:role/tcc-role-rekognition-sns";

    private RekognitionClient getRecClient() {
        Region region = Region.US_EAST_1;
        return RekognitionClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(region)
                .build();
    }

    private NotificationChannel getChannel() {
        return NotificationChannel.builder()
                .snsTopicArn(topicArn)
                .roleArn(roleArn)
                .build();
    }

    public String StartFaceDetection(String bucket, String video) {

        String startJobId = "";

        try {
            RekognitionClient rekognitionClient = getRecClient();
            software.amazon.awssdk.services.rekognition.model.S3Object s3Object = S3Object.builder()
                    .bucket(bucket)
                    .name(video)
                    .build();

            Video vibOb = Video.builder()
                    .s3Object(s3Object)
                    .build();

            StartFaceDetectionRequest faceDetectionRequest = StartFaceDetectionRequest.builder()
                    .jobTag("Faces")
                    .notificationChannel(getChannel())
                    .faceAttributes(FaceAttributes.ALL)
                    .video(vibOb)
                    .build();

            StartFaceDetectionResponse startFaceDetectionResponse = rekognitionClient.startFaceDetection(faceDetectionRequest);
            startJobId = startFaceDetectionResponse.jobId();
            return startJobId;

        } catch (RekognitionException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    // Processes the Job and returns of List of labels
    public List<FaceItems> GetFaceResults(String startJobId) {

        List<FaceItems> items = new ArrayList<>();
        try {

            RekognitionClient rekognitionClient = getRecClient();
            GetFaceDetectionResponse faceDetectionResponse = null;
            boolean finished = false;
            String status = "";
            int counter = 0;

            do {
                GetFaceDetectionRequest recognitionRequest = GetFaceDetectionRequest.builder()
                        .jobId(startJobId)
                        .nextToken(null)
                        .maxResults(10)
                        .build();

                // Wait until the job succeeds
                while (!finished) {
                    faceDetectionResponse = rekognitionClient.getFaceDetection(recognitionRequest);
                    status = faceDetectionResponse.jobStatusAsString();

                    if (status.compareTo("SUCCEEDED") == 0)
                        finished = true;
                    else {
                        System.out.println(counter + "status is: " + status);
                        Thread.sleep(1000);
                    }
                    counter++;
                }

                finished = false;

                // Push face information to the list
                List<FaceDetection> faces = faceDetectionResponse.faces();

                FaceItems faceItem;
                for (FaceDetection face : faces) {

                    faceItem = new FaceItems();

                    String age = face.face().ageRange().toString();
                    String beard = face.face().beard().toString();
                    String eyeglasses = face.face().eyeglasses().toString();
                    String eyesOpen = face.face().eyesOpen().toString();
                    String mustache = face.face().mustache().toString();
                    String smile = face.face().smile().toString();

                    faceItem.setAgeRange(age);
                    faceItem.setBeard(beard);
                    faceItem.setEyeglasses(eyeglasses);
                    faceItem.setEyesOpen(eyesOpen);
                    faceItem.setMustache(mustache);
                    faceItem.setSmile(smile);

                    items.add(faceItem);
                }
            } while (faceDetectionResponse.nextToken() != null);

            return items;

        } catch (RekognitionException | InterruptedException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
}















