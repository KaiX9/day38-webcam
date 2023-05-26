package sg.edu.nus.iss.day38practiceserver.repository;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    public static String key;

    public URL upload(MultipartFile file) throws IOException {
        Map<String, String> userData = new HashMap<>();
        userData.put("filename", file.getOriginalFilename());
        userData.put("upload-date", (new Date()).toString());

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentType(file.getContentType());
        metaData.setContentLength(file.getSize());
        metaData.setUserMetadata(userData);

        key = UUID.randomUUID().toString().substring(0, 8);

        PutObjectRequest putReq = new PutObjectRequest("kai", key, 
            file.getInputStream(), metaData);

        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        PutObjectResult result = s3.putObject(putReq);
        System.out.printf(">>> result: %s\n".formatted(result));

        return s3.getUrl("kai", key);
    }

    public void saveToMongo(String comments) {

        System.out.println("key: " + key);

        Document doc = new Document();
        doc.append("post_id", key);
        doc.append("comments", comments);

        mongoTemplate.insert(doc, "posts");

    }
}
