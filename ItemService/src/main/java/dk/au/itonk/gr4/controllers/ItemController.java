package dk.au.itonk.gr4.controllers;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dk.au.itonk.gr4.models.Category;
import dk.au.itonk.gr4.models.Item;
import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Sebastian on 18/04/2016.
 */

@RestController
@RequestMapping("/items")
public class ItemController {

    private MongoCollection<Document> itemCollection;

    private List<Item> items = new ArrayList<>();

    public ItemController() {
        MongoClientOptions.Builder options = MongoClientOptions.builder();
        options.socketKeepAlive(true);
        MongoClient mongoClient = new MongoClient("128.199.62.185:27017", options.build());

        MongoDatabase database = mongoClient.getDatabase("itonk");
        itemCollection = database.getCollection("items");

        checkInitialItems();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<Item>> getAllItems() {
        FindIterable<Document> iterableCollection = itemCollection.find();

        Iterator<Document> it = iterableCollection.iterator();
        List<Item> result = new ArrayList<>();

        while (it.hasNext()) {
            Document d = it.next();
            Item item = new Item(d);
            result.add(item);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Item> getItemWithId(@PathVariable long id) {
        FindIterable<Document> iterableCollection = itemCollection.find(eq("item_id", id)).limit(1);
        Document itemDocument = iterableCollection.first();

        if (itemDocument == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new Item(itemDocument), HttpStatus.OK);
    }

    private void checkInitialItems() {
        FindIterable<Document> iterableCollection = itemCollection.find();
        Document item = iterableCollection.first();

        if (item != null) {
            return;
        }

        List<Document> documentList = new ArrayList<>();
        Item bugattiVeyron = new Item(1L, "Bugatti Veyron",
            "The Bugatti Veyron 16.4 with over 1000 hp and a top speed of more than 400 km/h is considered to be the ultimate super sports car.",
            "Since its launch in 2005, the Bugatti Veyron has been regarded as a supercar of superlative quality. It was a real challenge for developers to fulfil the specifications that the new supercar was supposed to meet: over 1,000 hp, a top speed of over 400 km/h and the ability to accelerate from 0 to 100 in under three seconds. Even experts thought it was impossible to achieve these performance specs on the road. But that was not all.",
            4000000f, Category.SUPERCAR, "http://www.bilsektionen.dk/wp-content/uploads/2011/01/Bugatti-Veyron.jpg");
        documentList.add(bugattiVeyron.getMongoDBDocument());

        Item koenigseggOne = new Item(2L, "Koenigsegg One",
            " The One:1 is the first homologated production car in the world with one Megawatt of power, thereby making it the world´s first series produced Megacar.",
            "The One:1 was introduced in 2014. The hp-to-kg curb weight ratio is an astonishing 1:1. This is the “dream” equation, previously thought impossible when it comes to fully road legal and usable sports cars. The One:1 is the first homologated production car in the world with one Megawatt of power, thereby making it the world´s first series produced Megacar.\n" +
                "Thus, the name One:1.bThe One:1 truly raised the performance bar. The One:1 featured new, unique solutions that enhance track performance without compromising top speed or everyday usability, with a stunning visual appearance to go with it.\n" +
                "Examples of features: unique track-optimized aero winglets, extended venturi tunnels and side splitters, Le-Mans inspired active wing configuration and optimized active under trim air management. Large air vents for improved cooling, roof air scoop to support the 1 Megawatt of power, 8250 RPM rev limit, complemented with custom-made Michelin Cup Tires, upgraded rear Triplex suspension with carbon bevel springs and active shock absorbers and ride height, combined with upgraded revolutionary Koenigsegg Aircore carbon fiber wheels.\n" +
                "Koenigsegg was the first extreme car manufacturer to take steps toward green technology with the release of the biofuel CCXR in 2007. The One:1, a sibling of the highly competitive Agera R, follows in the footsteps of the CCXR as it also runs on E85 biofuel, race fuel or normal gasoline.",
            2200000f, Category.SUPERCAR, "http://images.cdn.autocar.co.uk/sites/autocar.co.uk/files/styles/gallery_slide/public/koenigsegg-one-1-014.jpg?itok=m4qCuo7D");
        documentList.add(koenigseggOne.getMongoDBDocument());

        Item audiR8 = new Item(3L, "Audi R8",
            "Designed to exhilarate, the 2017 Audi R8 Coupe blends explosive power and an exotic presence. This is performance engineered to its fullest.",
            "Utilizing an engine and chassis derived directly from the R8 LMS racecar, the all-new Audi R8 boldly goes where no car has gone before. Boasting a powerful 540-hp V10 or 610-hp V10 plus, the R8 is ready to drive innovation forward. Buckle up. This supercar was built for speed, with a high-revving, naturally aspirated 5.2-liter V10 engine that delivers up to 610 hp in the R8 V10 plus.2 Add to that the exceptional traction of an exclusive quattro® all-wheel drive system and advanced ASF® multi-material construction, and it’s clear the R8 has a mission: to make every drive a thrilling one. ",
            1500000f, Category.SUPERCAR, "http://www.blogcdn.com/slideshows/images/slides/285/550/1/S2855501/slug/l/01-2014-audi-r8-v8-review-1.jpg");
        documentList.add(audiR8.getMongoDBDocument());

        Item hennesseyVenom = new Item(4L, "Hennessey Venom",
            "The Hennessey Venom GT is an American sports car based on a heavily modified Lotus Exige chassis, manufactured by Texas-based Hennessey Performance Engineering.",
            "Note: The Hennessey Venom GT design is based on the Lotus Elise / Exige. The Venom GT is created from a base Lotus Elise / Exige and utilizes components including but not limited to the roof, doors, side glass, windscreen, dash, cockpit, floorpan, HVAC system, wiper and headlights. Hennessey Performance and the Venom GT are not associated with Lotus Cars.",
            5000000f, Category.SUPERCAR, "http://a38898d4011a160a051fb191.gearheads.netdna-cdn.com/wp-content/uploads/2015/08/hennessey-venom-gt.jpg");
        documentList.add(hennesseyVenom.getMongoDBDocument());


        Item porsche918 = new Item(6L, "Porsche 918",
            "It took Porsche years to develop a worthy successor to the vaunted Carrera GT supercar, but at last, the 918 is here—and it's a plug-in hybrid!",
            "The Porsche 918 Spyder is a mid-engined plug-in hybrid sports car designed by Porsche. The Spyder is powered by a naturally-aspirated 4.6 litre V8 engine, developing 608 horsepower (453 kW), with two electric motors delivering an additional 279 horsepower (208 kW) for a combined output of 887 horsepower (661 kW). The 918 Spyder's 6.8 kWh lithium-ion battery pack delivers an all-electric range of 19 km (12 mi) under EPA's five-cycle tests. The car has a top speed of around 340 km/h (210 mph).",
            2200000f, Category.SUPERCAR, "http://blog.caranddriver.com/wp-content/uploads/2014/10/2015-porsche-918-spyder-photo-637166-s-1280x782-626x366.jpg");
        documentList.add(porsche918.getMongoDBDocument());

        Item teslaModelS = new Item(7L, "Tesla Model S",
            "With unparalleled performance delivered through Tesla’s unique, all-electric powertrain, Model S accelerates from 0 to 60 mph in as little as 2.8 seconds. ",
            "Dual Motor Model S is a categorical improvement on conventional all-wheel drive systems. With two motors, one in the front and one in the rear, Model S digitally and independently controls torque to the front and rear wheels. The result is unparalleled traction control in all conditions.\n" +
                "\n" +
                "Conventional all-wheel drive cars employ complex mechanical linkages to distribute power from a single engine to all four wheels. This sacrifices efficiency in favor of all weather traction. In contrast, each Model S motor is lighter, smaller and more efficient than its rear wheel drive counterpart, providing both improved range and faster acceleration.\n" +
                "\n" +
                "Model S Performance comes standard with All-Wheel Drive Dual Motor, pairing the high performance rear motor with a high efficiency front motor to achieve supercar acceleration, from zero to 60 miles per hour in 2.8 seconds.",
            1000000f, Category.SUPERCAR, "http://media.caranddriver.com/images/14q4/638369/2015-tesla-model-s-p85d-first-drive-review-car-and-driver-photo-648964-s-original.jpg");
        documentList.add(teslaModelS.getMongoDBDocument());

        Item bmwi8 = new Item(8L, "BMW i8",
            "The BMW i8 is ready to revolutionise its vehicle class. As the first sports car with the consumption and emission values of a compact car.",
            "The central part of the i8 is a new architecture called LifeDrive that was inaugurated by the all-electric i3 city car. Crafted out of aluminum, the Drive module includes the gas-burning engine, the electric motor, the lithium-ion battery pack and all of the car's electronic components. The Life module is built out of carbon fiber-reinforced plastic (CFRP) and mainly consists of the 2+2 passenger compartment.\n" +
                "\n" +
                "Thanks to the LifeDrive architecture, the i8 tips the scale at a claimed 3,285 pounds and boasts a perfect 50/50 weight distribution.",
            3000000f, Category.SUPERCAR, "http://o.aolcdn.com/dims-global/dims3/GLOB/legacy_thumbnail/750x422/quality/95/http://www.blogcdn.com/slideshows/images/slides/258/894/4/S2588944/slug/l/01-2015-bmw-i8-fd-1.jpg");
        documentList.add(bmwi8.getMongoDBDocument());

        Item ferrari458 = new Item(9L, "Ferrari 458",
            "458 Speciale, the Ferrari's V8 sports cars with the best performances ever: extreme technology for special emotions.",
            "As with all Ferrari special series, the 458 Speciale boasts an array of advanced technical solutions that make it a completely unique model designed for owners looking for an even more focused sports car offering extreme driving emotions. \n" +
                "The innovations span the entire car and include both Ferrari patents and world firsts, particularly with regard to the engine, active aerodynamics and vehicle dynamics. In fact, they set the 458 Speciale apart from the 458 Italia from which it is derived, to an extent unprecedented in the previous Challenge Stradale and 430 Scuderia special series.",
            2600000f, Category.SUPERCAR, "http://auto.ferrari.com/en_US/wp-content/uploads/sites/7/2014/10/458-speciale-thumb.jpg");
        documentList.add(ferrari458.getMongoDBDocument());


        Item mcLarenP1 = new Item(10L, "McLaren P1",
            "The McLaren P1™ is the ultimate expression of McLaren's engineering expertise. Intelligent, adaptive and blisteringly quick.",
            "McLaren’s £866,000, 903bhp hybrid hypercar that promises to be the most involving car to drive on road and track. That’s a big claim. The heart is the familiar 3.8-litre V8 biturbo that sees duty in the 12C and 650S, but with bigger turbos to produce 727bhp and 531lb ft. The ‘leccy bit pushes out 176bhp and 192lb ft (twice the KERS system on a 2013 F1 car), and handily fills in the torque hole left by turbo lag. It all drives exclusively to the rear wheels via McLaren’s seven-speed twin-clutch, giving 0-62mph in 2.8secs and a quarter mile in 9.8secs @ 152mph. That’s ruddy quick. And it’ll run in pure electric mode when you want to go quietly, and plug in to the mains, too. Neat. ",
            3500000f, Category.SUPERCAR, "https://upload.wikimedia.org/wikipedia/commons/c/cb/P1-3.png");
        documentList.add(mcLarenP1.getMongoDBDocument());

        itemCollection.insertMany(documentList);
    }

    private Long getNewItemID() {
        FindIterable<Document> iterable = itemCollection.find().sort(new BasicDBObject("item_id", -1)).limit(1);
        Document d = iterable.first();

        if (d == null) {
            return 1L;
        }

        return d.getLong("item_id") + 1;
    }
}
