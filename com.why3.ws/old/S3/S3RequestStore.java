package com.why3.ws.data;


import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
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



public class S3RequestStore {
		
		protected final static String AWSAccessKeyID = "AKIAJ6CCAW5FIIWP3WZA";	
		protected final static String AWSSecretKey = "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";
			
		//static Domain domain;
		static AmazonS3Client s3r;
		
		static Date date;
		
		
		 public static void main(String[] args) throws NoSuchAlgorithmException, ParseException, AmazonServiceException, AmazonClientException, InterruptedException {
		 		  
		  ClientConfiguration clientConfig = new ClientConfiguration();
		  clientConfig.setProtocol(Protocol.HTTP);

		  s3r = new AmazonS3Client( new BasicAWSCredentials( AWSAccessKeyID, AWSSecretKey ), clientConfig  ); //S3 Request
		  
		  populateS3();
		  
		  
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
		  			//s3r.createBucket("why3request");
		  			s3r.setEndpoint("why3request.s3-website-us-east-1.amazonaws.com");
		  			
		  		   				    
				//---------------------    		  			
		  		//Request	
				//---------------------
		  		List<Bucket> buckets = s3r.listBuckets();
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
		  		ObjectListing objects = s3r.listObjects(bucket.getName());
		  		do {
		  		for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
		  		totalSize += objectSummary.getSize();
		  		totalItems++;
		  		}
		  		
		  		objects = s3r.listNextBatchOfObjects(objects);
		  		
		  		} while (objects.isTruncated());
		  		}
		  		
		  		System.out.println("You have " + buckets.size() + " Amazon S3 bucket(s), " +
				  		"containing " + totalItems + " objects with a total size of " + totalSize + " bytes.");
	  			
		  		
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
		  		
		  		System.out.println("[INFO]---------> KEEP CALM: The REQUEST File has been SUCCESSFULLY saved in the S3 bucket :)"+"\n");
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

			    //System.out.println("END of VERIFICATION---------------------------------------------------------"+ "Current Time: "+ Verifier.getDate((long)Verifier.finalTime, "dd/MM/yyyy hh:mm:ss.SSS")+" ms\n\n");	 		
		  		  	
		 }
		 		
			 	//Populate S3 for RESULTS
				 public static void populateS3() throws AmazonServiceException, AmazonClientException, NoSuchAlgorithmException, InterruptedException{
					 try {
				            System.out.println("\n[INFO]---------> Uploading a new object to S3 from a WHY3 Request file...\n");
				            
				            File currentDir = new File("");
							File file = new File(currentDir.getAbsolutePath()+"/modelreq");
							
							//File homedir = new File(System.getProperty("user.home"));
				            //File file = new File(homedir, "workspace/com.why3.ws/src/com/why3/ws/why3models/modelreq");
			                
				            s3r.putObject(new PutObjectRequest(
				            		                 "why3request", Verifier.hash, file));

				         } catch (AmazonServiceException ase) {
				            System.out.println("Caught an AmazonServiceException, which " +
				            		"means your request made it " +
				                    "to Amazon S3, but was rejected with an error response" +
				                    " for some reason.");
				            System.out.println("Error Message:    " + ase.getMessage());
				            System.out.println("HTTP Status Code: " + ase.getStatusCode());
				            System.out.println("AWS Error Code:   " + ase.getErrorCode());
				            System.out.println("Error Type:       " + ase.getErrorType());
				            System.out.println("Request ID:       " + ase.getRequestId());
				        } catch (AmazonClientException ace) {
				            System.out.println("Caught an AmazonClientException, which " +
				            		"means the client encountered " +
				                    "an internal error while trying to " +
				                    "communicate with S3, " +
				                    "such as not being able to access the network.");
				            System.out.println("Error Message: " + ace.getMessage());
				        }
				
				 }
			 
}
