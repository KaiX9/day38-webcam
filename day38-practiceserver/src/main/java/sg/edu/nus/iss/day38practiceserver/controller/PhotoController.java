package sg.edu.nus.iss.day38practiceserver.controller;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.day38practiceserver.repository.PhotoRepository;
import sg.edu.nus.iss.day38practiceserver.repository.PostRepository;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
// @CrossOrigin(origins="*")
public class PhotoController {
    
    @Autowired
    private PhotoRepository photoRepo;

    @Autowired
    private PostRepository postRepo;

    @PostMapping(path="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postUpload(@RequestPart String comments, 
        @RequestPart MultipartFile file) {
        
        System.out.println(">>> comments: " + comments);
        System.out.println(">>> file: " + file);
        try {
            URL url = photoRepo.upload(file);
            System.out.printf(">>> URL: %s\n".formatted(url.toString()));
            this.photoRepo.saveToMongo(comments);
            this.postRepo.createPost();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String id = PhotoRepository.key;
        JsonObject jObj = Json.createObjectBuilder()
                .add("id", id)
                .build();
        return ResponseEntity.ok(jObj.toString());
    }

    @PostMapping(path="/count", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postCount(@RequestBody String payload) {

        System.out.println("payload: " + payload);
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jObj = reader.readObject();
        Integer likeCount = jObj.getInt("likeCount");
        Integer unlikeCount = jObj.getInt("unlikeCount");
        System.out.println("likeCount: " + likeCount);
        System.out.println("unlikeCount: " + unlikeCount);

        this.postRepo.updateCount(likeCount, unlikeCount);

        JsonObject result = Json.createObjectBuilder()
            .add("likes", likeCount)
            .add("unlikes", unlikeCount)
            .build();

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(result.toString());
    }
     
}
