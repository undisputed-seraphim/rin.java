package rin.engine.resource.model;

import java.util.ArrayList;

public class Mesh {

	private ArrayList<Surface> children = new ArrayList<Surface>();
	private int surfaceCount = 0;
	
	public Surface addSurface( String name ) {
		children.add( new Surface( name ) );
		return children.get( surfaceCount++ );
	}
}
