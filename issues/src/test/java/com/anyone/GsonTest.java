package com.anyone;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class GsonTest {

	
	public void shouldSerialize() throws Exception {
		
		Class.forName("net.sourceforge.jtds.jdbc.Driver");
		
		try(Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/issues", "colin", "colin");) {
			
			Issue issue=new IssueImpl(conn, 9996, "Issue Title", "description", "application", "reproduce", Impact.HIGH, false);
			Gson gson = new Gson();
			JsonElement jsonElement = gson.toJsonTree(issue);
			jsonElement.getAsJsonObject().addProperty("explainTask", "Determine whether or not this bug represents an issue already raised. If so, supply the id. If not, select new bug");
			System.out.println(gson.toJson(jsonElement));
		}
	}
}
