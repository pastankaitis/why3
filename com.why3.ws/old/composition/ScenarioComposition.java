package com.why3.ws.composition;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;

public class ScenarioComposition {
	
	public static String tCvc3;
	public static String tAltergo;
	public static String tMetis;
	public static String tSpass;
	public static String finalRes;
	public static String option;
	static long TIMEOUT = 7500;
	public static String tGappa;
	public static String tYices;
	
	public static void main(String[] args) {
		
		System.out.println("*************************");
		System.out.println("Parallel Composition:    ");
		System.out.println("*************************");
		option="par";
		ParaComposition();
		
		System.out.println("");
		
		System.out.println("*************************");
		System.out.println("Sequential Composition:  ");
		System.out.println("*************************");
		option="seq";
		SeqComposition();
					 
	}
	
	/**
	 * Sequential Composition
	 * using:(newSingleThreadExecutor())
	 * 
	 * Creates an Executor that uses a single worker thread operating off 
	 * an unbounded queue. (Note however that if this single thread 
	 * terminates due to a failure during execution prior to shutdown, 
	 * a new one will take its place if needed to execute subsequent tasks.) 
	 * Tasks are guaranteed to execute sequentially, and no more than one 
	 * task will be active at any given time. Unlike the otherwise equivalent 
	 * newFixedThreadPool(1) the returned executor is guaranteed not to be 
	 * reconfigurable to use additional threads.
	 */
	public static void SeqComposition(){
		ThreadCvc3 callable1 = new ThreadCvc3();
		ThreadAltergo callable2 = new ThreadAltergo();
		ThreadMetis callable3 = new ThreadMetis();
		ThreadSpass callable4 = new ThreadSpass();
		ThreadGappa callable5 = new ThreadGappa();
		ThreadYices callable6 = new ThreadYices();
		
			        FutureTask<String> futureTask1 = new FutureTask<String>(callable1);
			        FutureTask<String> futureTask2 = new FutureTask<String>(callable2);
			        FutureTask<String> futureTask3 = new FutureTask<String>(callable3);
			        FutureTask<String> futureTask4 = new FutureTask<String>(callable4);
			        FutureTask<String> futureTask5 = new FutureTask<String>(callable5);
			        FutureTask<String> futureTask6 = new FutureTask<String>(callable6);
			        
			        ExecutorService executor = Executors.newSingleThreadExecutor();
			        executor.execute(futureTask1);
			        executor.execute(futureTask2);
			        executor.execute(futureTask4);
			        executor.execute(futureTask3);
			        executor.execute(futureTask5);
			        executor.execute(futureTask6);
			       
			        while (true) {
			            try {
			            	//Enforcing a TIMEOUT of 3 seconds
		                	Thread.sleep(TIMEOUT);
		                	
			                if(futureTask1.isDone() && futureTask2.isDone()){
			                	if(futureTask3.isDone() && futureTask4.isDone()){
			                		if(futureTask5.isDone() && futureTask6.isDone()){
					                	//Result
						                finalResult();
						                
						                //Negates Final Results
						                negateResult();
						                
						                //shut down executor service
						                executor.shutdown();
						                
			                	}
			                    return;
			                }else {  
		                		//wait indefinitely for future task to complete
				                System.out.println("[TIMEOUT] Unfinished Task1 output="+futureTask1.get());
				                System.out.println("[TIMEOUT] Unfinished Task2 output="+futureTask2.get());
				                System.out.println("[TIMEOUT] Unfinished Task3 output="+futureTask3.get());
				                System.out.println("[TIMEOUT] Unfinished Task4 output="+futureTask4.get());
				                System.out.println("[TIMEOUT] Unfinished Task5 output="+futureTask5.get());
				                System.out.println("[TIMEOUT] Unfinished Task6 output="+futureTask6.get());
				                
				                //Result
				                finalResult();
				                
				                //Negates Final Results
				                negateResult();
				                
				                //shut down executor service
				                executor.shutdown();
				                
				                //threadpool.shutdownNow();   //Currently running tasks will be interrupted and the tasks not started will not be started at all.
		                		}
			                }
			            } catch (Exception  e) {
			                e.printStackTrace();
			            }
			            
			        }
	}
	
	/**
	 * Parallel Composition
	 * using:(newScheduledThreadPool(N))
	 */
	public static void ParaComposition(){
		ThreadCvc3 callablePara1 = new ThreadCvc3();
		ThreadAltergo callablePara2 = new ThreadAltergo();
		ThreadMetis callablePara3 = new ThreadMetis();
		ThreadSpass callablePara4 = new ThreadSpass();
		ThreadGappa callablePara5 = new ThreadGappa();
		ThreadYices callablePara6 = new ThreadYices();
			 
			        FutureTask<String> futureTask1 = new FutureTask<String>(callablePara1);
			        FutureTask<String> futureTask2 = new FutureTask<String>(callablePara2);
			        FutureTask<String> futureTask3 = new FutureTask<String>(callablePara3);
			        FutureTask<String> futureTask4 = new FutureTask<String>(callablePara4);
			        FutureTask<String> futureTask5 = new FutureTask<String>(callablePara5);
			        FutureTask<String> futureTask6 = new FutureTask<String>(callablePara6);
			    
			        ExecutorService executor = Executors.newScheduledThreadPool(4);
			        executor.execute(futureTask1);
			        executor.execute(futureTask2);
			        executor.execute(futureTask3);
			        executor.execute(futureTask4);
			        executor.execute(futureTask5);
			        executor.execute(futureTask6);
			 
			        while (true) {
			            try {
			            	//Enforcing a TIMEOUT of 3 seconds
		                	Thread.sleep(TIMEOUT);
			            	
			                if(futureTask1.isDone() && futureTask2.isDone()){
			                	if(futureTask3.isDone() && futureTask4.isDone()){
			                		if(futureTask5.isDone() && futureTask6.isDone()){
				                    //Result
					                finalResult();
					                
					                //Negates Final Results
					                negateResult();
					                
					                //shut down executor service
					                executor.shutdown();
					                
					                System.out.println("*Done*");
				                    
				                }
			                	return;
			                }else {  
			                		//wait indefinitely for future task to complete [TRUE within the set TIMEOUT]
					                System.out.println("[TIMEOUT] Unfinished Parallel Task1 output="+futureTask1.get());
					                System.out.println("[TIMEOUT] Unfinished Parallel Task2 output="+futureTask2.get());
					                System.out.println("[TIMEOUT] Unfinished Parallel Task3 output="+futureTask3.get());
					                System.out.println("[TIMEOUT] Unfinished Parallel Task4 output="+futureTask4.get());
					                System.out.println("[TIMEOUT] Unfinished Parallel Task5 output="+futureTask5.get());
					                System.out.println("[TIMEOUT] Unfinished Parallel Task6 output="+futureTask6.get());
					                
					                //Result
					                finalResult();
					                
					                //Negates Final Results
					                negateResult();
					                
					                //shut down executor service
					                executor.shutdown();
					                
					                //threadpool.shutdownNow();  //Currently running tasks will be interrupted and the tasks not started will not be started at all.
			                	}
			               
			                }
			            } catch (InterruptedException | ExecutionException e) {
			                e.printStackTrace();}
			            
			        }   
	}
	
	/**
	 * Final Results
	 * 
	 * @return finalRes
	 */
	public static WHY3_RESULT finalResult(){
		int count = 0;
		int cvc3Counter = 0;
		int altergoCounter = 0;
		int metisCounter = 0;
		int spassCounter = 0;
		int gappaCounter = 0;
		int yicesCounter = 0;
		
		//CVC3:
		if(tCvc3.equalsIgnoreCase("valid")){cvc3Counter +=1;}
		else if(tCvc3.equalsIgnoreCase("failure")){cvc3Counter +=-1;}
		else if(tCvc3.equalsIgnoreCase("unknown")){cvc3Counter +=0;}
		else if(tCvc3.equalsIgnoreCase("undefined")){cvc3Counter +=0;}
		
		//Alt-Ergo:
		if(tAltergo.equalsIgnoreCase("valid")){altergoCounter +=1;}
		else if(tAltergo.equalsIgnoreCase("failure")){altergoCounter +=-1;}
		else if(tAltergo.equalsIgnoreCase("unknown")){altergoCounter +=0;}
		else if(tAltergo.equalsIgnoreCase("undefined")){altergoCounter +=0;}
		
		//Metis:
		if(tMetis.equalsIgnoreCase("valid")){metisCounter +=1;}
		else if(tMetis.equalsIgnoreCase("failure")){metisCounter +=-1;}
		else if(tMetis.equalsIgnoreCase("unknown")){metisCounter +=0;}
		else if(tMetis.equalsIgnoreCase("undefined")){metisCounter +=0;}
				
		//Spass:
		if(tSpass.equalsIgnoreCase("valid")){spassCounter +=1;}
		else if(tSpass.equalsIgnoreCase("failure")){spassCounter +=-1;}
		else if(tSpass.equalsIgnoreCase("unknown")){spassCounter +=0;}
		else if(tSpass.equalsIgnoreCase("undefined")){spassCounter +=0;}
		
		//Gappa:
		if(tGappa.equalsIgnoreCase("valid")){spassCounter +=1;}
		else if(tGappa.equalsIgnoreCase("failure")){spassCounter +=-1;}
		else if(tGappa.equalsIgnoreCase("unknown")){spassCounter +=0;}
		else if(tGappa.equalsIgnoreCase("undefined")){spassCounter +=0;}
		
		//Gappa:
		if(tYices.equalsIgnoreCase("valid")){spassCounter +=1;}
		else if(tYices.equalsIgnoreCase("failure")){spassCounter +=-1;}
		else if(tYices.equalsIgnoreCase("unknown")){spassCounter +=0;}
		else if(tYices.equalsIgnoreCase("undefined")){spassCounter +=0;}
		
		count = cvc3Counter + altergoCounter + metisCounter 
				+ spassCounter + gappaCounter + yicesCounter;
		System.out.println("\n        [TEST]-------> Counter Value: "+count);
		
		if(count < 0){
			finalRes = "Failure";
		}else
			if(count == 0){
				//If 3 counters were valid (1 each) [i.e. 1+(-1)+1+(-1)+1+(-1) = 0]
				if((cvc3Counter + altergoCounter+ metisCounter )==3){
					finalRes = "Valid";
				}else if((cvc3Counter + metisCounter + spassCounter)==3){
					finalRes = "Valid";
				}else if((cvc3Counter + spassCounter + gappaCounter)==3){
					finalRes = "Valid";
				}
				else if((cvc3Counter + gappaCounter + yicesCounter)==3){
					finalRes = "Valid";
				}
				else if((cvc3Counter + metisCounter + yicesCounter)==3){
					finalRes = "Valid";
				}				
				//If most (at least 3) counters are zero (0) or less (-1)
				else if(((cvc3Counter <=0) && ((cvc3Counter + metisCounter)==0))){
						if(((metisCounter <=0) && ((metisCounter + altergoCounter)==0))){
							if(((altergoCounter <=0) && ((altergoCounter + gappaCounter)==0))){
								if(((gappaCounter <=0) && ((gappaCounter + yicesCounter)==0))){
									finalRes = "Failure";
								}
						}
					}
				}
		}else if(count > 3){
			finalRes = "Valid";
		}else{finalRes = "Failure";}
		
		System.out.println("        [TEST]-------> Final Results: "+ finalRes);
		
		
		return WHY3_RESULT.fromString(finalRes);
		
	} 

	/**
	 * Negate Final Results
	 * 
	 * @return negateFinalRes
	 */
	private static String negateResult(){
		String negate;
				
		if(finalRes.equalsIgnoreCase("Valid")){
			negate = "Invalid";
			
		}else if(finalRes.equalsIgnoreCase("Failure")){
			negate ="Valid";
		}else{negate ="Unknown";}
		
		System.out.println("        [TEST]-------> Negate Final Results: "+ negate);
		
		return negate;
		
	} 
}
