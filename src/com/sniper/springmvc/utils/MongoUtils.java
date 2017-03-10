package com.sniper.springmvc.utils;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.TextSearchOptions;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * http://www.2cto.com/database/201408/322384.html
 * 
 * @author suzhen
 * 
 */
public class MongoUtils {

	MongoClient client;
	MongoDatabase database;
	MongoCollection<Document> collection;

	public MongoUtils() {
		MongoClientOptions.Builder builder = MongoClientOptions.builder();
		builder.connectionsPerHost(50); // 与目标数据库能够建立的最大connection数量为50
		builder.threadsAllowedToBlockForConnectionMultiplier(50); // 如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
		/*
		 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
		 * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
		 * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
		 */
		builder.maxWaitTime(1000 * 60 * 2);
		builder.connectTimeout(1000 * 60 * 1); // 与数据库建立连接的timeout设置为1分钟
		MongoClientOptions myOptions = builder.build();

		// client = new MongoClient("127.0.0.1", 27017);
		client = new MongoClient("127.0.0.1:27017", myOptions);
		database = client.getDatabase("test");
		collection = database.getCollection("col");
		// MongoClient mongoClient = new MongoClient(Arrays.asList(
		// new ServerAddress("localhost", 27017), new ServerAddress(
		// "localhost", 27018), new ServerAddress("localhost",
		// 27019)));

	}

	public void init() {
		database.createCollection("col");

		database.createCollection("cappedCollection",
				new CreateCollectionOptions().capped(true)
						.sizeInBytes(0x100000));

	}

	public void count() {
		collection = database.getCollection("col");
		System.out.println(collection.count());
	}

	public void insert() {

		Document doc = new Document("name", "MongoDB")
				.append("type", "database").append("count", 1)
				.append("info", new Document("x", 203).append("y", 102));
		collection.insertOne(doc);
	}

	public void find() {
		Document myDoc = collection.find().first();
		System.out.println(myDoc.toJson());

		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}

		for (Document cur : collection.find()) {
			System.out.println(cur.toJson());
		}

		myDoc = collection.find(eq("i", 71)).first();

		System.out.println(myDoc.toJson());

		// now use a range query to get a larger subset
		Block<Document> printBlock = new Block<Document>() {
			@Override
			public void apply(final Document document) {
				System.out.println(document.toJson());
			}
		};
		collection.find(gt("i", 50)).forEach(printBlock);

		collection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);

		myDoc = collection.find(exists("i")).sort(descending("i")).first();
		System.out.println(myDoc.toJson());

		myDoc = collection.find().projection(excludeId()).first();
		System.out.println(myDoc.toJson());

		collection
				.aggregate(asList(match(gt("i", 0)), project(Document
						.parse("{ITimes10: {$multiply: ['$i', 10]}}"))));

		// myDoc = collection.aggregate(
		// singletonList(group(null, sum("total", "$i")))).first();
		// System.out.println(myDoc.toJson());
	}

	public void update() {
		// 根条件更新
		collection.updateOne(eq("i", 10), set("i", 110));
		// 根据条件跟新多条
		UpdateResult updateResult = collection.updateMany(lt("i", 100),
				inc("i", 100));
		System.out.println(updateResult.getModifiedCount());
	}

	public void delete() {
		collection.deleteOne(eq("i", 110));

		DeleteResult deleteResult = collection.deleteMany(gte("i", 100));
		System.out.println(deleteResult.getDeletedCount());
	}

	public void bulkWrite() {
		// 2. Ordered bulk operation - order is guarenteed

		List<? extends WriteModel<? extends Document>> list1 = Arrays.asList(
				new InsertOneModel<>(new Document("_id", 4)),
				new InsertOneModel<>(new Document("_id", 5)),
				new InsertOneModel<>(new Document("_id", 6)));

		collection.bulkWrite(list1);

	}

	public void index() {

		collection.createIndex(Indexes.ascending("i"));
	}

	public void test1() {
		// Insert some documents
		collection.insertOne(new Document("_id", 0).append("content",
				"textual content"));
		collection.insertOne(new Document("_id", 1).append("content",
				"additional content"));
		collection.insertOne(new Document("_id", 2).append("content",
				"irrelevant content"));

		// Find using the text index
		long matchCount = collection.count(Filters
				.text("textual content -irrelevant"));
		System.out.println("Text search matches: " + matchCount);

		// Find using the $language operator
		Bson textSearch = Filters.text("textual content -irrelevant",
				new TextSearchOptions().language("english"));
		matchCount = collection.count(textSearch);
		System.out.println("Text search matches (english): " + matchCount);

		// Find the highest scoring match
		Document projection = new Document("score", new Document("$meta",
				"textScore"));
		Document myDoc = collection.find(textSearch).projection(projection)
				.first();
		System.out.println("Highest scoring document: " + myDoc.toJson());
	}

	public void asyncText() {
		collection.insertMany(new ArrayList<Document>());

		collection.count();

		SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
			@Override
			public void onResult(final Void result, final Throwable t) {
				System.out.println("Operation Finished!");
			}
		};

	}

	public void document() {
		BsonDocument bsonDocument = new BsonDocument().append("a",
				new BsonString("MongoDB"))
				.append("b",
						new BsonArray(Arrays.asList(new BsonInt32(1),
								new BsonInt32(2))));

	}

	public void close() {
		client.close();
	}

	public static void main(String[] args) {
		MongoUtils mongoUtils = new MongoUtils();
		mongoUtils.insert();
		mongoUtils.count();
		
	}
}
