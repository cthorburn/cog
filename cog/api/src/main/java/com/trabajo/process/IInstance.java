package com.trabajo.process;

import com.trabajo.DefinitionVersion;
import com.trabajo.IVisualizer;
import com.trabajo.ProcessCompletion;
import com.trabajo.StateContainer;


public interface IInstance extends IEntity {
	IProcDef  getProcDef();
	void end(ProcessCompletion way);
	StateContainer getState();
	void setState(StateContainer stateContainer);
	DefinitionVersion  version();
	IVisualizer getVisualizer();
	void setVisualizer(IVisualizer visualizer);
	void purge();
}
