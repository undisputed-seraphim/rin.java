package rin.engine.resource.model;

import java.util.ArrayList;

import rin.engine.resource.ResourceContainer;

public class ModelContainer extends ResourceContainer {

	private ArrayList<Surface> surfaces = new ArrayList<Surface>();
	
	public ArrayList<Surface> getSurfaces() {
		return surfaces;
	}
	
	public Surface addSurface( String name ) {
		surfaces.add( new Surface( name ) );
		return surfaces.get( surfaces.size() - 1 );
	}
}
