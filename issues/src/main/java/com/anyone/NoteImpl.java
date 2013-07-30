package com.anyone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

public class NoteImpl implements Note {


	private Connection conn;
	private Issue issue;
	private String user;
	private String noteText;
	private GregorianCalendar createTime;
	
	public NoteImpl(Connection conn, Issue issue, String user, String noteText)  {
		super();
		
		this.conn = conn;
		this.issue=issue;
		this.user = user;
		this.noteText = noteText;
		this.createTime=new GregorianCalendar();
		save();
	}

	public NoteImpl(Issue issue, ResultSet rs) throws SQLException {
		this.issue=issue;
		this.user=rs.getString("createdBy");
		this.noteText=rs.getString("note");
		this.createTime=new GregorianCalendar();
		this.createTime.setTime(new Date(rs.getTimestamp("created").getTime()));
	}

	public void save() {
		try(PreparedStatement ps=conn.prepareStatement("insert into note (issue, created, createdBy, note) values(?,?,?,?)");) {
			ps.setInt(1, issue.getId());
			ps.setTimestamp(2, new Timestamp(createTime.getTime().getTime()));
			ps.setString(3, user);
			ps.setString(4, noteText);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	public String getCreator() {
		return user;
	}

	public String getNoteText() {
		return noteText;
	}

	public GregorianCalendar getCreateTime() {
		return createTime;
	}
}
