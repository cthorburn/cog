package com.anyone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.trabajo.process.ServiceInfo;

public class IssueSvcImpl implements Issues {

	private ServiceInfo config;
	private Connection conn; 
	
	public IssueSvcImpl(ServiceInfo config) {
		this.config=config;
		conn=getConnection();
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
		}
		
	}
	public Issue createIssue(int id, String title, String description, String application, String reproduce, Impact impact, boolean internal) {
		return new IssueImpl(conn, id, title, description, application, reproduce, impact, internal);
	}

	public String getGridXML() {
		XMLSBHelper sb=new XMLSBHelper(new StringBuilder());
		sb.append("<rows>");
		
		try(PreparedStatement ps = conn.prepareStatement("select id, title, description, application, reproduce, impact, internal, duplicate_of from issue")) {
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				row(sb, rs);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		sb.append("</rows>");
		return sb.toString();
	}
	
	private void row(XMLSBHelper sb, ResultSet rs) throws SQLException {
		sb.elm("row").attr("id", String.valueOf(rs.getInt("id"))).closeTag()
		.elm("cell").closeTag().
		append(String.valueOf(rs.getInt("id"))).closeElm("cell")
		.elm("cell").closeTag().
		append(rs.getString("title")).closeElm("cell")
		.elm("cell").closeTag().
		append(rs.getString("application")).closeElm("cell")
		.elm("cell").closeTag().
		append(rs.getString("impact")).closeElm("cell")
		.elm("cell").closeTag().
		append(rs.getString("description")).closeElm("cell")
		.closeElm("row");
	}

	public Issue getIssue(Integer id) {
		try(PreparedStatement ps = conn.prepareStatement("select id, title, description, application, reproduce, impact, internal, duplicate_of from issue where id=?")) {
			ps.setInt(1,  id.intValue());
			return new IssueImpl(conn, ps.executeQuery());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Connection getConnection() {
		try {
			return DriverManager.getConnection(config.get("url"), config.get("username"), config.get("password"));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

}
