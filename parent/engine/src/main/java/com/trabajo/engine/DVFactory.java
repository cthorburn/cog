package com.trabajo.engine;
import com.trabajo.DefinitionVersion;
import com.trabajo.vcl.KeyFactory;

public class DVFactory implements KeyFactory<DefinitionVersion> {

	@Override
	public DefinitionVersion parse(String s) {
		return DefinitionVersion.parse(s);
	}

}
