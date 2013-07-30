package com.trabajo.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.trabajo.engine.EngineStatus.Msg;

public class DeferredMessages implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Msg> deferred = new ArrayList<>();
		
		public void defer(Msg msg) {
			deferred.add(msg);
		}

		public List<Msg> getDeferred() {
			return deferred;
		}

		public List<Msg> copyList() {
			List<Msg> result=deferred;
			deferred=new ArrayList<>();
			return result;
		}

}
