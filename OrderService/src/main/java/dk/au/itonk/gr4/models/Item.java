package dk.au.itonk.gr4.models;

import org.bson.Document;

public class Item {
    private Long id;
    private String title;
    private String description;
    private String extendedDescription;
    private String imageUrl;
    private Category category;
    private float price;
    private int amount;

    public Item() {

    }

    public Item(Long id, String title, String description, String extendedDesc, float price, int amount, Category category, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.extendedDescription = extendedDesc;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.amount = amount;
    }

    public Item(Document document) {
        this.id = document.getLong("item_id");
        this.title = document.getString("title");
        this.description = document.getString("description");
        this.extendedDescription = document.getString("extendedDescription");
        this.price = new Float(document.getDouble("price"));
        this.category = Category.valueOf(document.getString("category"));
        this.imageUrl = document.getString("imageUrl");
        this.amount = document.getInteger("amount");
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getExtendedDescription() {
        if (extendedDescription.isEmpty())
            return description;

        return extendedDescription;
    }

    public Category getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getAmount() {
        return amount;
    }

    public Document getMongoDBDocument() {
        Document item = new Document();
        item.put("item_id", this.id);
        item.put("title", this.title);
        item.put("description", this.description);
        item.put("extendedDescription", this.extendedDescription);
        item.put("price", this.price);
        item.put("category", this.category.toString());
        item.put("imageUrl", this.imageUrl);
        item.put("amount", this.amount);

        return item;
    }
}
