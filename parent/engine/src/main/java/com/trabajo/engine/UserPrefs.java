package com.trabajo.engine;

import com.trabajo.jpa.UserPrefsJPA;

public class UserPrefs {

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	private String skin;

	public void save(TSession ts) {
		UserPrefsJPA up;
		
		up=new UserPrefsJPA();
		up.setSkin("DHXBLACK");
		ts.getEntityManager().persist(up);
	}

}
