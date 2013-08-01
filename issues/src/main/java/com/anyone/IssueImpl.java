package com.anyone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class IssueImpl implements Issue {
	
	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getApplication() {
		return application;
	}

	public String getReproduce() {
		return reproduce;
	}

	public Impact getImpact() {
		return impact;
	}

	public boolean isInternal() {
		return internal;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int id;
	private String title;
	private String description;
	private String application;
	private String reproduce;
	private Impact impact;
	private boolean internal;
	@SuppressWarnings("unused")
	private Integer existingId;

	@Expose(serialize=false, deserialize=false)
	private transient Connection conn;
	
	public IssueImpl() {
		//for Gson's benefit
	}
	
	public IssueImpl(Connection conn, int id, String title, String description, String application, String reproduce, Impact impact, boolean internal) {
		super();
		this.conn=conn;
		this.id=id;
		this.title = title;
		this.description = description;
		this.application = application;
		this.reproduce = reproduce;
		this.impact = impact;
		this.internal = internal;
		insert();
	}

	public IssueImpl(Connection conn, ResultSet rs) throws SQLException {
		this.conn=conn;
		rs.next();
		this.id=rs.getInt("id");
		this.title = rs.getString("title");
		this.description = rs.getString("description");
		this.application = rs.getString("application");;
		this.reproduce = rs.getString("reproduce");
		this.impact = Impact.valueOf(rs.getString("impact"));
		this.internal = rs.getBoolean("internal");
		System.out.println("issues version 1.1.2");
	}

	public void insert() {
		try {
			PreparedStatement ps=conn.prepareStatement("insert into issue (title, description, application, reproduce, impact, internal, id) values(?,?,?,?,?,?,?)");
			ps.setInt(7, id);
			ps.setString(1, title);
			ps.setString(2, description);
			ps.setString(3, application);
			ps.setString(4, reproduce);
			ps.setString(5, impact.toString());
			ps.setBoolean(6, internal);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	public int getId() {
		return id;
	}

	public Note createNote(String user, String noteText) {
		return new NoteImpl(conn, this, user, noteText);
	}

	public void setDuplicateOf(Integer existingId) {
		this.existingId=existingId;
		try(PreparedStatement ps=conn.prepareStatement("update issue set duplicate_of=? where id=?");) {
			if(existingId==null)
				ps.setNull(1, Types.INTEGER);
			else 
				ps.setInt(1, existingId);
			
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Note> getNotes() {
		List<Note> result=new ArrayList<>();
				
				try(PreparedStatement ps=conn.prepareStatement("select issue, created, createdby, note from note where issue=?");) {
					ps.setInt(1, getId());
					ResultSet rs = ps.executeQuery();
					while(rs.next()) {
						result.add(new NoteImpl(this, rs));
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
		return result;
	}
}
