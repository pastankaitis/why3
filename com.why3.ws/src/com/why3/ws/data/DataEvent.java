package com.why3.ws.data;


/**
 * An event describing some data-related phenomena
 * @author alex
 *
 */
public abstract class DataEvent {
	
	/**
	 * 
	 * @throws Exception an exception may be thrown to abort data event processing; the transaction encapsulating event will be rolled backed automatically
	 */
	public abstract void commit(DatabaseConnector connector)  throws Exception;
}
