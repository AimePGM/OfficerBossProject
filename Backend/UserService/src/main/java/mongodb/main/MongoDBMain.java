package mongodb.main;

import java.net.UnknownHostException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;

public class MongoDBMain {
	 
    public static final String DB_NAME = "usertest";
    public static final String COLLECTION = "usertestonly";
    public static final String MONGO_HOST = "localhost";
    public static final int MONGO_PORT = 27017;
    private static MongoClient mongo;
 
    public static void run() {
    	try {
    		
        	System.out.println("connecting to mongodb at"+MONGO_HOST+":"+MONGO_PORT+"...\ndatabase name: "+DB_NAME+"\ncollection: "+COLLECTION);
            if (mongo == null){
        	mongo = new MongoClient(
                    MONGO_HOST, MONGO_PORT);
            }
            System.out.println("connected!");
             
        } catch (UnknownHostException e) {
        	System.err.println("Cannot connect to "+MONGO_HOST+":"+MONGO_PORT);
            e.printStackTrace();
        }
    }
    public static void close(){
    	mongo.close();
    }
    public static MongoClient getMongoClient(){
    	return mongo;
    }
    public static String getDBName(){
    	return DB_NAME;
    }
    public static String getCollection(){
    	return COLLECTION;
    }
    public static String getHost(){
    	return MONGO_HOST;
    }
    public static int getPort(){
    	return MONGO_PORT;
    }
 
}