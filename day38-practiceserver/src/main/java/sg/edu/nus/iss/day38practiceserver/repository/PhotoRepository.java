package sg.edu.nus.iss.day38practiceserver.repository;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Repository
public class PhotoRepository {
    
    @Autowired
    private AmazonS3 s3;

    public URL upload(String comments, MultipartFile file) throws IOException {
        Map<String, String> userData = new HashMap<>();
        userData.put("comments", comments);
        userData.put("filename", file.getOriginalFilename());
        userData.put("upload-date", (new Date()).toString());

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentType(file.getContentType());
        metaData.setContentLength(file.getSize());
        metaData.setUserMetadata(userData);

        PutObjectRequest putReq = new PutObjectRequest("kai", file.getOriginalFilename(), 
            file.getInputStream(), metaData);

        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult result = s3.putObject(putReq);
        System.out.printf(">>> result: %s\n".formatted(result));

        return s3.getUrl("kai", file.getOriginalFilename());
    }
}
