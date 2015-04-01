package com.why3.ws.data;

import java.util.HashMap;
import java.util.Map;

public class SessionContext {
	private Map<String, Object> values; 
	
	public SessionContext() {
		values = new HashMap<String, Object>();
	}
	
	public void set(String key, Object value) {
		values.put(key, value);
	}

	public Object get(String key) throws SessionException {
		Object z = values.get(key);
		if (z == null)
			throw new SessionException("Key '" + key + "' is not defined in the session");
		return z;
	}

	public long getLong(String key) throws SessionException {
		Object z = get(key);
		if (!(z instanceof Long))
			throw new SessionException("Key '" + key + "' is not valid type");
		return (Long) get(key);
	}
	
}
