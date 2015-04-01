package com.why3.ws.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.why3.ws.Verifier;
import com.why3.ws.connector.Why3Tool;
import com.xerox.amazonws.sdb.Domain;
import com.xerox.amazonws.sdb.Item;
import com.xerox.amazonws.sdb.ItemAttribute;
import com.xerox.amazonws.sdb.SDBException;
import com.xerox.amazonws.sdb.SimpleDB;

public class SimpleDBAdmin {

	protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";
	protected final static String AWSSecretKey = "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";

	static Domain domain;
	static AmazonS3Client s3;
	static Why3Tool tool = null;
	// static AmazonS3Client s3r;

	static Date date;

	public static void main(String[] args) throws NoSuchAlgorithmException,
			ParseException, AmazonServiceException, AmazonClientException,
			InterruptedException {
		SimpleDB sdb = new SimpleDB(AWSAccessKeyID, AWSSecretKey, true); // SimpleDB

		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);

		s3 = new AmazonS3Client(new BasicAWSCredentials(AWSAccessKeyID,
				AWSSecretKey), clientConfig); // S3

		
												/*
												 * if ( null != sdb ) ((AmazonSimpleDB)
												 * sdb).setEndpoint("https://eu-west-1.ec2.amazonaws.com");
												 */

		try {
			// Creating the Domain
			// domain = sdb.createDomain("WHY3DBdomain");
			domain = sdb.getDomain("WHY3DBdomain");

			// List the Domain(s)
			com.xerox.amazonws.sdb.ListDomainsResult domainsResult = sdb
					.listDomains();
			List<Domain> domains = domainsResult.getDomainList();
			for (Domain dom : domains) {
				System.out.println("Domain : " + dom.getName());
			}

			//populateS3(); // Populate AWS S3 with Results of Verification with
							// WHY3

			//S3RequestStore.main(args); // Populate AWS S3 with Client Requests
										// of Verification with WHY3

			putItem(); // Manipulating Items

												// Retrieve Items from Domain
												/*
												 * String queryString = "SELECT * FROM `WHY3DBdomain`"; int
												 * itemCount = 0; String nextToken = null; do {
												 * QueryWithAttributesResult queryResults =
												 * domain.selectItems(queryString, nextToken); Map<String,
												 * List<ItemAttribute>> items = queryResults.getItems(); for (String
												 * id : items.keySet()) { System.out.println("Item : " + id); for
												 * (ItemAttribute attr : items.get(id)) {
												 * System.out.println(attr.getName() + " = " + attr.getValue()); }
												 * itemCount++; } nextToken = queryResults.getNextToken(); } while
												 * (nextToken != null && !nextToken .trim().equals(""));
												 */
									
												// Retrieving Individual Items from the Domain 
												/*
												 * Item req =
												 * domain.getItem(Verifier.makeSHA1Hash(Verifier.s+Verifier
												 * .curTime)); List<ItemAttribute> itemAttrs = req.getAttributes();
												 * for (ItemAttribute attr : itemAttrs) {
												 * System.out.println(attr.getName() + " = " + attr.getValue()); }
												 */

		} catch (SDBException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Amazon S3
		 * 
		 * The AWS S3 client allows you to manage buckets and programmatically
		 * put and get objects to those buckets.
		 * 
		 * In this sample, we use an S3 client to iterate over all the buckets
		 * owned by the current user, and all the object metadata in each
		 * bucket, to obtain a total object and space usage count. This is done
		 * without ever actually downloading a single object -- the requests
		 * work with object metadata only.
		 */
		try {
				// s3.createBucket("why3storage");
				s3.setEndpoint("why3storage.s3-website-us-east-1.amazonaws.com");
	
				
	
				// ---------------------
				// Results
				// ---------------------
				List<Bucket> buckets = s3.listBuckets();
				long totalSize = 0;
				int totalItems = 0;
				for (Bucket bucket : buckets) {
					/*
					 * In order to save bandwidth, an S3 object listing does not
					 * contain every object in the bucket; after a certain point the
					 * S3ObjectListing is truncated, and further pages must be
					 * obtained with the AmazonS3Client.listNextBatchOfObjects()
					 * method.
					 */
					ObjectListing objects = s3.listObjects(bucket.getName());
					do {
						for (S3ObjectSummary objectSummary : objects
								.getObjectSummaries()) {
							totalSize += objectSummary.getSize();
							totalItems++;
						}
	
						objects = s3.listNextBatchOfObjects(objects);
	
					} while (objects.isTruncated());
				}
	
				System.out.println("You have " + buckets.size()
						+ " Amazon S3 bucket(s), " + "containing " + totalItems
						+ " objects with a total size of " + totalSize + " bytes.");

		} catch (AmazonServiceException ase) {
			/*
			 * AmazonServiceExceptions represent an error response from an AWS
			 * services, i.e. your request made it to AWS, but the AWS service
			 * either found it invalid or encountered an error trying to execute
			 * it.
			 */
			System.out.println("Error Message: " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code: " + ase.getErrorCode());
			System.out.println("Error Type: " + ase.getErrorType());
			System.out.println("Request ID: " + ase.getRequestId());

			System.out
					.println("[INFO]---------> KEEP CALM: The RESULTS File has been SUCCESSFULLY saved in the S3 bucket :)"
							+ "\n");
		} catch (AmazonClientException ace) {
			/*
			 * AmazonClientExceptions represent an error that occurred inside
			 * the client on the local host, either while trying to send the
			 * request to AWS or interpret the response. For example, if no
			 * network connection is available, the client won't be able to
			 * connect to AWS to execute a request and will throw an
			 * AmazonClientException.
			 */
			System.out.println("Error Message: " + ace.getMessage());
		}

		System.out
				.println("END of VERIFICATION---------------------------------------------------------"
						+ "Current Time: "
						+ Verifier.getDate(Long.parseLong(String.valueOf(System.currentTimeMillis())),
								"dd/MM/yyyy hh:mm:ss.SSS") + " ms\n\n");

	}

	// Manipulating Items
	public static void putItem() throws SDBException, NoSuchAlgorithmException,
			ParseException, IOException, NullPointerException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		date = formatter.parse(Verifier.getDate(
				Long.parseLong(String.valueOf(System.currentTimeMillis())),
				"dd/MM/yyyy hh:mm:ss.SSS")); // Verifier.tim
		System.out.println("Verification Time: " + date);

		// GeneratePresignedUrlRequest request = new
		// GeneratePresignedUrlRequest(bucket.getName(), "why3results");
		URL s3res = s3.generatePresignedUrl("why3storage",
				Verifier.hash,
				date);

		URL s3req = S3RequestStore.s3r.generatePresignedUrl("why3request",
				Verifier.hash,
				date);

		//Why3Tool tool = new Why3Tool(Verifier.modelReq);

		Item item = domain.getItem(Verifier.hash);
		List<ItemAttribute> list = new ArrayList<ItemAttribute>();
		list.add(new ItemAttribute("ToolSet", tool.getToolArguments(), false)); // Verifier.option
		list.add(new ItemAttribute("Request", s3req.toString(), false)); // Verifier.s
																			// Verifier.s.substring(0,
																			// 14)
		list.add(new ItemAttribute("Results", s3res.toString(), false)); // Verifier.verification
		list.add(new ItemAttribute("VerificationTime", String.valueOf(Verifier.end), false)); // Long.toString(Verifier.tim)
																				// ----->
																				// date.toString()
		item.putAttributes(list);

	}

	// Populate S3 for RESULTS
	public static void populateS3() throws AmazonServiceException,
			AmazonClientException, NoSuchAlgorithmException,
			InterruptedException {
		try {

			System.out
					.println("[INFO]---------> Uploading a new object to S3 from a WHY3 Results file...\n");

			File currentDir = new File("");
			File file = new File(currentDir.getAbsolutePath() + "/modelres");

			// File homedir = new File(System.getProperty("user.home"));
			// File file = new File(homedir,
			// "workspace/com.why3.ws/src/com/why3/ws/why3models/modelres");

			s3.putObject(new PutObjectRequest("why3storage", Verifier.hash, file));

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which "
					+ "means your request made it "
					+ "to Amazon S3, but was rejected with an error response"
					+ " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which "
					+ "means the client encountered "
					+ "an internal error while trying to "
					+ "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

	}
}
