package sg.edu.nus.iss.day38practiceserver.controller;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.day38practiceserver.repository.PhotoRepository;

@Controller
@RequestMapping
@CrossOrigin(origins="*")
public class PhotoController {
    
    @Autowired
    private PhotoRepository photoRepo;

    @PostMapping(path="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postUpload(@RequestPart String comments, 
        @RequestPart MultipartFile file) {
        
        System.out.println(">>> comments: " + comments);
        System.out.println(">>> file: " + file);
        try {
            URL url = photoRepo.upload(comments, file);
            System.out.printf(">>> URL: %s\n".formatted(url.toString()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ResponseEntity.ok("{}");
    }
}
