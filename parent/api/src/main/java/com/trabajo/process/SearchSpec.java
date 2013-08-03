package com.trabajo.process;

import java.util.List;

public interface SearchSpec {
	String getWhich();
	String getQuery();
	List<String> getParams();
}
