package com.trabajo;



public interface IVisualizer {

	IVizInstance instance();

	IVizTask task(String taskName);

	String generate(IGraphEntityFinder  graphEntityFinder);
}
