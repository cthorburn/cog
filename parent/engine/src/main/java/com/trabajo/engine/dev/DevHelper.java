package com.trabajo.engine.dev;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DevHelper {
	
	public static void wipeQuartzTables(Connection conn) throws SQLException {
		
		String sql="	delete FROM [trabajo].[dbo].[QRTZ_CRON_TRIGGERS];" + 
				"		  delete FROM [trabajo].[dbo].[QRTZ_TRIGGERS];" + 
				"		  delete FROM [trabajo].[dbo].[QRTZ_CALENDARS];" + 
				"		  delete FROM [trabajo].[dbo].[QRTZ_SCHEDULER_STATE];" + 
				"		  delete FROM [trabajo].[dbo].[QRTZ_BLOB_TRIGGERS];" + 
				"		  delete FROM [trabajo].[dbo].[QRTZ_BLOB_TRIGGERS];" + 
				"		  delete FROM [trabajo].[dbo].[QRTZ_FIRED_TRIGGERS];" + 
				"     delete FROM [trabajo].[dbo].[QRTZ_PAUSED_TRIGGER_GRPS]"+
				"		  delete FROM [trabajo].[dbo].[QRTZ_JOB_DETAILS];" + 
				"		  delete  FROM [trabajo].[dbo].[QRTZ_LOCKS]";
		
		try(PreparedStatement ps=conn.prepareStatement(sql);) {
			ps.executeUpdate();
		}
		
	}		
	
	public static void deleteAllProcesses(Connection conn) throws SQLException {
		
		String sql="	delete FROM [trabajo].[dbo].[taskbarrier]; " + 
				"		  delete FROM [trabajo].[dbo].[taskgroup]; " + 
				"		  delete FROM [trabajo].[dbo].[nodetimer]; " + 
				"		  delete FROM [trabajo].[dbo].[task_roles]; " + 
				"		  delete FROM [trabajo].[dbo].[node]; " + 
				"		  delete FROM [trabajo].[dbo].[instance]; " + 
				"		  delete  FROM [trabajo].[dbo].[procdef]";
		
		try(PreparedStatement ps=conn.prepareStatement(sql);) {
			ps.executeUpdate();
		}
		
	}		
}
