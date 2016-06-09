package dk.au.itonk.gr4.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dk.au.itonk.gr4.models.Order;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Sebastian on 18/04/2016.
 */

@RestController
@RequestMapping("/orders")
public class OrderController {

    private MongoCollection<Document> orderCollection;
    private List<Order> orders;

    public OrderController() {
        MongoClientOptions.Builder options = MongoClientOptions.builder();
        options.socketKeepAlive(true);
        MongoClient mongoClient = new MongoClient("82.196.6.106:27017");
        MongoDatabase database = mongoClient.getDatabase("kubecloud-gpr4");
        orderCollection = database.getCollection("orders");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getOrdersForUserWithId(@RequestParam(value = "userId", defaultValue = "null") Long userId) {
        FindIterable<Document> iterableCollection = orderCollection.find(eq("user_id", userId));

        Iterator<Document> it = iterableCollection.iterator();
        List<Order> result = new ArrayList<>();

        while (it.hasNext()) {
            Document d = it.next();
            Order order = new Order(d);
            result.add(order);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Order> getOrderWithId(@PathVariable Long id) {
        FindIterable<Document> iterableCollection = orderCollection.find(eq("order_id", id));

        Document orderDocument = iterableCollection.first();

        if (orderDocument == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new Order(orderDocument), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Long order_id = getNewOrderID();
        order.setId(order_id);
        orderCollection.insertOne(order.getMongoDBDocument());
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam(value = "status", defaultValue = "null") String status) {
        FindIterable<Document> iterableCollection = orderCollection.find(eq("order_id", id));
        Document orderDocument = iterableCollection.first();

        if (orderDocument == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Order order = new Order(orderDocument);
        order.setStatus(Order.STATUS.valueOf(status));

        Document updateRecord = new Document("$set", order.getMongoDBDocument());
        orderCollection.updateOne(eq("order_id", id), updateRecord);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    private Long getNewOrderID() {
        FindIterable<Document> iterable = orderCollection.find().sort(new BasicDBObject("order_id", -1)).limit(1);
        Document d = iterable.first();

        if (d == null) {
            return 1L;
        }

        return d.getLong("order_id") + 1;
    }
}
