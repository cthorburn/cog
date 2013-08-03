package com.trabajo.engine;

import java.nio.file.Path;

import com.trabajo.DefinitionVersion;
import com.trabajo.engine.bobj.ReportDefs;
import com.trabajo.process.IReportDef;

public class ReportManager {

	public static void install(TSession ts, Path target, DefinitionVersion dv, String category, String description, String remoteUser, String originalName) {
		IReportDef def=ReportDefs.create(ts.getEntityManager(), target, dv, category, description, originalName);
		ts.getStatus().info("report installed: "+def.getDefinitionVersion().toString());
	}

	public static String get(TSession ts, DefinitionVersion dv) {
		IReportDef def=ReportDefs.findByVersion(ts.getEntityManager(), dv);
		if(def==null) {
			ts.getStatus().error("No such Report: "+dv);
		}
		return def==null ? null:def.getReportText();
	}


	public static String getReportByName(TSession ts, String name) {
		IReportDef def=ReportDefs.findByName(ts.getEntityManager(), name);
		return def.getReportText();
	}
}
