package com.why3.ws.data;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.codehaus.jettison.json.JSONObject;

/**
 * This event describes session initiation; tables sessions, theorems and users are (possibly) updated
 * @author alex
 *
 */
public class SessionInitiatedDataEvent extends DataEvent {
	private String host;
	private JSONObject request;
	private SessionContext sessionContext;
	
	public SessionInitiatedDataEvent(String host, JSONObject request, SessionContext sessionContext) {
		this.host = host;
		this.request = request;
		this.sessionContext = sessionContext;
	}

	@Override
	public void commit(DatabaseConnector connector) throws Exception {
		int ip = 0;  
		try {
			InetAddress addr = InetAddress.getByName(host);
			for (byte b: addr.getAddress())  
			    ip = ip << 8 | (b & 0xFF);
		} catch (UnknownHostException e) {
			// ignore
		}  

		long userId = request.getLong("uuid");
		sessionContext.set("uuid", userId);
		String scenario = request.getString("uuid");
		long scenarioId = connector.queryScenario(scenario);
		sessionContext.set("scenario", scenarioId);
		long theoremId = connector.insertTheorem(
					request.getString("types"),
					request.getString("definitions"),
					request.getString("hypothesis"),
					request.getString("goal")
					);
		sessionContext.set("theorem", theoremId);
		connector.insertUser(userId, ip);
		connector.insertSession(userId, scenarioId, theoremId);
	}
}
