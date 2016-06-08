package dk.au.itonk.gr4.models;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian on 18/04/2016.
 */
public class Order {
    private long id;
    private long userId;
    private Collection<Item> items;
    private STATUS status;
    private Date orderDate;
    private String trackingCode;

    public enum STATUS {
        ACCEPTED, PAYED, PREPARED, SHIPPED, DELIVERED, CANCELLED
    }

    public Order() {
        status = STATUS.ACCEPTED;
        orderDate = new Date();
        trackingCode = "";
    }

    public Order(long id, long userId, Collection<Item> items) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.status = STATUS.ACCEPTED;
        this.orderDate = new Date();
        this.trackingCode = "";
    }

    public Order(Document document) {
        this.id = document.getLong("order_id");
        this.userId = document.getLong("user_id");
        this.status = STATUS.valueOf(document.getString("status"));
        this.orderDate = document.getDate("orderDate");
        this.trackingCode = document.getString("trackingCode");

        this.items = new ArrayList<>();
        Collection<Document> itemDocuments = (Collection<Document>)document.get("items");
        for (Document d : itemDocuments) {
            items.add(new Item(d));
        }
    }

    public void setId(Long id) {this.id = id;}

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public Collection<Item> getItems() {
        return items;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status){ this.status = status;}

    public Date getOrderDate() {
        return orderDate;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public Document getMongoDBDocument() {
        List<Document> itemDocuments = new ArrayList<>();
        for (Item item :items) {
            itemDocuments.add(item.getMongoDBDocument());
        }

        Document d = new org.bson.Document();
        d.put("order_id", this.id);
        d.put("user_id", this.userId);
        d.put("items", itemDocuments);
        d.put("status", this.status.toString());
        d.put("orderDate", this.orderDate);
        d.put("trackingCode", this.trackingCode);

        return d;
    }
}
