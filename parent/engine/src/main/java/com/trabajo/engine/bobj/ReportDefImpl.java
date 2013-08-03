package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ReportDefJPA;
import com.trabajo.process.IReportDef;

public class ReportDefImpl extends CogEntity<ReportDefJPA> implements IReportDef {

	public ReportDefImpl(EntityManager em, ReportDefJPA entity) {
		super(em, entity, null);
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public DefinitionVersion getDefinitionVersion() {
		return entity().getDefinitionVersion().toDefinitionVersion();
	}

	@Override
	public void setCategory(String category) {
		entity().setCategory(category);

	}

	@Override
	public String getReportText() {
		return entity().getReportText();
	}

	@Override
	public void setDescription(String description) {
		entity().setDescription(description);

	}
}
