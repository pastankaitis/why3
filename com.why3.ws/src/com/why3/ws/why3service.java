package com.why3.ws;

import java.io.IOException;




import javax.servlet.http.HttpServletRequest;
//import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
//import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.why3.ws.connector.Why3CallResult;
import com.why3.ws.connector.Why3CallResult.WHY3_RESULT;
import com.why3.ws.connector.Why3ProverList;
import com.why3.ws.connector.Why3ToolInfo;

/**
 * The leading slash here is necessary for tomcat v7.0!
 * @author alex
 *
 */
@Path("service")
public class why3service {

	@POST
	@Path("/run")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response serverEntryPoint(@Context HttpServletRequest request, JSONObject json) throws IOException, JSONException, InterruptedException {

		Verifier v = new Verifier();
		try {
			Why3CallResult response = v.process(request, json);
			JSONObject reply = new JSONObject();
			reply.put("status", response.getStatus().toString());
			reply.put("tool", response.getTool());
			reply.put("time", response.getTime());
			return Response.ok(reply, MediaType.APPLICATION_JSON).build();
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(200)
					.entity(WHY3_RESULT.TOOL_FAILURE.toString()).build();
		}

	}
	
	/**
	 * Returns list of provers installed locally (i.e., at the server)
	 * @return a json reply made of <prover-id> <prover-version> key/value pairs.  
	 * @throws IOException
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	
	@POST
	@Path("/listprovers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listProvers() throws IOException, JSONException, InterruptedException {

		try {
			JSONObject reply = new JSONObject();
			for(Why3ToolInfo info: Why3ProverList.getToolList()) {
				reply.put(info.getName(), info.getVersion());
			}
			return Response.ok(reply, MediaType.APPLICATION_JSON).build();
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(200)
					.entity(e.getMessage()).build();
		}
	}	
}
