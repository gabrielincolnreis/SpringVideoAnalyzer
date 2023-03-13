package com.example.video;

    import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class VideoController {

    @Autowired
    S3Service s3Client;

    @Autowired
    WriteExcel excel;

    @Autowired
    SendMessages sendMessage;

    @Autowired
    VideoDetectFaces detectFaces;

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/video")
    public String photo(){
        return "upload";
    }

    private final String bucketName = "aws-us-east-1-tcc-video";

    //Diferente do original boa parte da classe
    @GetMapping("/getvideo")
    @ResponseBody
    String getImages(){
        return s3Client.ListAllObjects("scottexamplevideo");
    }

    // Upload a MP4 to an Amazon S3 bucket
    @PostMapping("/upload")
    public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file){

        try{
            byte[] bytes = file.getBytes();
            String name = file.getOriginalFilename();

            // Put the MP4 file into an Amazon S3 bucket
            s3Client.putObject(bytes, bucketName, name);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ModelAndView(new RedirectView("video"));
    }

    // generates a report after analyzing a video in an Amazon S3 bucket
    @PostMapping("/report")
    public String report(HttpServletRequest request){

        String email = request.getParameter("email");
        String myKey = s3Client.getKeyName(bucketName);
        String jobNum = detectFaces.StartFaceDetection(bucketName, myKey);
        List<FaceItems> items = detectFaces.GetFaceResults(jobNum);
        InputStream excelDate = excel.exportExcel(items);

        try{
            sendMessage.sendReport(excelDate, email);
        } catch (Exception e ){
            e.printStackTrace();
        }
        return "The "+ myKey +" video has been successfully analyzed and the report is sent to "+email;
    }
}
















