package com.trabajo.values;

import com.trabajo.ValidationException;


public class CreateRoleSpec {

	private RoleName    name;
	private Description desc;
	private Category cat;
	
	public CreateRoleSpec(
			String cat,
			String name, 
			String desc) throws ValidationException {
		
		this.name=new RoleName(name);
		this.desc=new Description(desc, 255);
		this.cat=new Category(cat);
		
	}

	public RoleName getName() {
		return name;
	}

	public Description getDesc() {
		return desc;
	}

	public Category getCategory() {
		return cat;
	}
}
