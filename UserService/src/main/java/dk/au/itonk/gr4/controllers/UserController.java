package dk.au.itonk.gr4.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dk.au.itonk.gr4.models.User;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by aske on 4/27/16.
 */


@RestController
@RequestMapping("/users")
public class UserController {

    private MongoCollection<Document> userCollection;

    private List<User> users;

    public UserController() {
        MongoClientOptions.Builder options = MongoClientOptions.builder();
        options.socketKeepAlive(true);
        MongoClient mongoClient = new MongoClient("82.196.6.106:27017");
        MongoDatabase database = mongoClient.getDatabase("kubecloud-gpr4");
        userCollection = database.getCollection("users");

        checkInitialUsers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<User> getUserWithId(@PathVariable long id) {
        FindIterable<Document> iterableCollection = userCollection.find(eq("user_id", id));
        Document userDocument = iterableCollection.first();

        if (userDocument == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new User(userDocument), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        FindIterable<Document> iterableCollection = userCollection.find(eq("email", user.getEmail())).limit(1);
        Document userDocument = iterableCollection.first();

        if (userDocument != null) {
            Map<String, String> result = new HashMap<>();
            result.put("statusCode", "406");
            result.put("description", "User with that email already exists. Please try again.");
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }

        Long user_id = getNewUserID();
        user.setId(user_id);
        userCollection.insertOne(user.getMongoDBDocument());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Object> deleteUserWithId(@PathVariable long id, @RequestParam("password") String password) {
        FindIterable<Document> iterableCollection = userCollection.find(eq("user_id", id)).limit(1);
        Document userDocument = iterableCollection.first();

        if (userDocument == null) {
            Map<String, String> result = new HashMap<>();
            result.put("statusCode", "404");
            result.put("description", "User doesn't exist.");
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }

        User user = new User(userDocument);
        if (!user.hash(password).equals(user.getPassword())) {
            Map<String, String> result = new HashMap<>();
            result.put("statusCode", "400");
            result.put("description", "Password is incorrect.");

            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        userCollection.deleteOne(eq("user_id", id));

        Map<String, String> result = new HashMap<>();
        result.put("statusCode", "200");
        result.put("description", "User with id '" + id + "' was deleted.");

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public ResponseEntity<Object> userLogin(@RequestParam("email") String email, @RequestParam("password") String password) {
        FindIterable<Document> iterableCollection = userCollection.find(eq("email", email));

        Document userDocument = iterableCollection.first();

        if (userDocument == null) {
            Map<String, String> result = new HashMap<>();
            result.put("statusCode", "404");
            result.put("description", "User doesn't exist. Please try again.");
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        User user = new User(userDocument);
        if (!user.hash(password).equals(user.getPassword())) {
            Map<String, String> result = new HashMap<>();
            result.put("statusCode", "400");
            result.put("description", "Password is incorrect. Please try again.");

            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private Long getNewUserID() {
        FindIterable<Document> iterable = userCollection.find().sort(new BasicDBObject("user_id", -1)).limit(1);
        Document d = iterable.first();

        if (d == null) {
            return 1L;
        }

        return d.getLong("user_id") + 1;
    }

    private void checkInitialUsers() {
        FindIterable<Document> iterableCollection = userCollection.find();
        Document item = iterableCollection.first();

        if (item != null) {
            return;
        }

        List<Document> documentList = new ArrayList<>();
        User aske = new User((long) 1, "Kasper", "Nissen", "Finlandsgade 24", "Aarhus", 8000, "Denmark", 87654321, "kaspernissen@gmail.com", "12345678");
        documentList.add(aske.getMongoDBDocument());

        User sebastian = new User((long) 2, "Martin", "Jensen", "Finlandsgade 23", "Aarhus", 8000, "Denmark", 12345678, "martin2610@gmail.com", "12345678");
        documentList.add(sebastian.getMongoDBDocument());

        userCollection.insertMany(documentList);
    }
}