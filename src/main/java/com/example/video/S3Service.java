package com.example.video;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class S3Service {

    private S3Client s3;

    private S3Client getClient() {

        Region region = Region.US_EAST_1;
        return S3Client.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(region)
                .build();

        //Diferente do original
    }

    // Places an image into a S3 bucket
    public String putObject(byte[] data, String bucketName, String objectKey) {

        s3 = getClient();

        // Delete the existing video - this use case can only have 1 MP4 file
        String objectName = getKeyName(bucketName);
        ArrayList<ObjectIdentifier> toDelete = new ArrayList<ObjectIdentifier>();
        toDelete.add(ObjectIdentifier.builder().key(objectName).build());

        try {

            DeleteObjectsRequest objectsRequest = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder().objects(toDelete).build())
                    .build();
            s3.deleteObjects(objectsRequest);

            // Put a MP4 into the bucket
            PutObjectResponse response = s3.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .build(),
                    RequestBody.fromBytes(data));

            return response.eTag();

        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }

    public String ListAllObjects(String bucketName){

        s3 = getClient();
        BucketItem myItem;
        //Diferente do original

        List<BucketItem> bucketItems = new ArrayList<>();

        try{
            ListObjectsRequest listObjects = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse response = s3.listObjects(listObjects);
            List<S3Object> objects = response.contents();

            //Diferente do original
            for (S3Object myValue : objects) {
                myItem = new BucketItem();
                myItem.setKey(myValue.key());
                myItem.setOwner(myValue.owner().displayName());
                myItem.setSize(String.valueOf(myValue.size() / 1024));
                myItem.setDate(String.valueOf(myValue.lastModified()));

                // Push the items to the list
                bucketItems.add(myItem);
            }

            return convertToString(toXml(bucketItems));

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return null;
    }

    public String getKeyName(String bucketName) {

        s3 = getClient();
        String keyName = "";

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse response = s3.listObjects(listObjects);
            List<S3Object> objects = response.contents();

            //Diferente do original
            for (S3Object myValue : objects){
                keyName = myValue.key();
            }

            return keyName;

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return null;
    }

    // Convert Bucket item data into XML to pass back to the view
    private Document toXml(List<BucketItem> itemList){

        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            // Start building the XML
            Element root = document.createElement( "Items" );
            document.appendChild(root);

           //Diferente do original

            //Iterate through the collection
            for (BucketItem myItem : itemList) {

                // Get the WorkItem object from the collection
                Element item = document.createElement("Item");
                root.appendChild(item);

                // Set Key
                Element id = document.createElement( "Key" );
                id.appendChild( document.createTextNode(myItem.getKey()) );
                item.appendChild( id );

                // Set Owner
                Element name = document.createElement( "Owner" );
                name.appendChild( document.createTextNode(myItem.getOwner() ) );
                item.appendChild( name );

                // Set Date
                Element date = document.createElement( "Date" );
                date.appendChild( document.createTextNode(myItem.getDate() ) );
                item.appendChild( date );

                // Set Size
                Element desc = document.createElement( "Size" );
                desc.appendChild( document.createTextNode(myItem.getSize() ) );
                item.appendChild( desc );
            }

            return document;
        } catch (ParserConfigurationException e){
            e.printStackTrace();
        }
        return null;
    }

    private String convertToString(Document xml){
        try{
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(xml);
            transformer.transform(source,result);
            return result.getWriter().toString();

        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }
}
