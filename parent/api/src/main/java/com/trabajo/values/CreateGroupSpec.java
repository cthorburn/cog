package com.trabajo.values;

import com.trabajo.ValidationException;


public class CreateGroupSpec {

	private String    	name;
	private Description desc;
	private Category 		cat	;
	
	public CreateGroupSpec(
			String cat,
			String name, 
			String desc) throws ValidationException {
		
		this.name=name;
		this.desc=new Description(desc, 255);
		this.cat=new Category(cat);
		
	}

	public String getName() {
		return name;
	}

	public Description getDesc() {
		return desc;
	}

	public Category getCategory() {
		return cat;
	}
}
