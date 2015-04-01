package com.why3.ws.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Single-threaded event processor
 * @author alex
 *
 */
public class EventProcessor {
	public static final EventProcessor INSTANCE = new EventProcessor();
	
	private ExecutorService pool;
	private DatabaseConnector connector;
	
	private EventProcessor() {
		pool = Executors.newSingleThreadExecutor();
		connector = new DatabaseConnector();
	}

	public void queue(DataEvent event) {
		if (event != null)
			pool.submit(new DataEventJob(connector, event));
	}

	static class DataEventJob implements Runnable {
		private DatabaseConnector connector;
		private DataEvent event;

		public DataEventJob(DatabaseConnector connector, DataEvent event) {
			this.connector = connector;
			this.event = event;
		}

		@Override
		public void run() {
			try {
				connector.startTransaction();
				event.commit(connector);
				connector.commitTransaction();
			} catch (Throwable e) {
				e.printStackTrace();
				try {
					connector.rollbackTransaction();
				} catch (Throwable e1) {
					// ignore
				}
			}
		}

	}

}

