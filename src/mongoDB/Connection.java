package mongoDB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Connection {
    //funtion that returns the mongo database used for this application
    public MongoCollection getDatabase(){
        MongoClientURI uri = new MongoClientURI(
                "mongodb://root:root@npl-shard-00-00-xcjea.mongodb.net:27017,npl-shard-00-01-xcjea.mongodb.net:27017,npl-shard-00-02-xcjea.mongodb.net:27017/test?ssl=true&replicaSet=npl-shard-0&authSource=admin&retryWrites=true");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("test");
        return database.getCollection("npl");
    }
}
