package com.trabajo.process;

import java.io.InputStream;
import java.io.OutputStream;

public interface Marshaller {
	void importProcess(InputStream in);
	OutputStream exportProcess();	
}
