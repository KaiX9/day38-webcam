package sg.edu.nus.iss.day38practiceserver.repository;

import java.io.StringReader;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Repository
public class PostRepository {
    
    @Autowired @Qualifier("post")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private TaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    @PostConstruct
    public void init() {
        ((ThreadPoolTaskScheduler) taskScheduler).initialize();
    }

    public void createPost() {

        JsonObject jObj = Json.createObjectBuilder()
            .add("post_id", PhotoRepository.key)
            .add("post_created", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
            .add("likes", 0)
            .add("unlikes", 0)
            .add("lastupdated_count", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
            .build();
        
        this.redisTemplate.opsForValue().set(PhotoRepository.key, jObj.toString());

        taskScheduler.schedule(this::savePostToMongo, Instant.now().plus(Duration.ofSeconds(30)));

    }

    public void savePostToMongo() {

        String postJson = this.redisTemplate.opsForValue().get(PhotoRepository.key);

        Document postDoc = Document.parse(postJson);

        mongoTemplate.insert(postDoc, "count");
        redisTemplate.delete(PhotoRepository.key);
    
    }


    public void updateCount(Integer likes, Integer unlikes) {

        String post = this.redisTemplate.opsForValue().get(PhotoRepository.key);
        if (post != null) {
            JsonReader reader = Json.createReader(new StringReader(post));
            JsonObject res = reader.readObject();
            String post_id = res.getString("post_id");
            String post_created = res.getString("post_created");

            JsonObject newResult = Json.createObjectBuilder()
                .add("post_id", post_id)
                .add("post_created", post_created)
                .add("likes", likes)
                .add("unlikes", unlikes)
                .add("lastupdated_count", LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .build();

            this.redisTemplate.opsForValue().set(post_id, newResult.toString());
        } else {
            Query q = new Query();
            q.addCriteria(Criteria.where("post_id").is(PhotoRepository.key));

            Update updateOps = new Update()
                            .set("likes", likes)
                            .set("unlikes", unlikes)
                            .set("lastupdated_count", LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

            UpdateResult updResult = mongoTemplate.updateFirst(q, updateOps, Document.class, "count");
            System.out.println("Success: " + updResult.getModifiedCount());
        }
    }
}
