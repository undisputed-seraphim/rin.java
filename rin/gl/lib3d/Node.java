package rin.gl.lib3d;

import java.util.ArrayList;

public class Node extends Poly {
	protected ModelScene scene;
	private String name;

	protected Node parent;
	protected ArrayList<Node> children = new ArrayList<Node>();
	
	public Node( String n ) { name = n; }
	
	public ArrayList<Node> getChildren() { return children; }
	public String getName() { return name; }
	
	public void add( Node n ) {
		n.scene = scene;
		n.parent = this;
		children.add( n );
		scene.update();
	}
	
	public void remove( Node n ) {
		children.remove( n );
		scene.update();
	}
	
	@Override
	public void update( long dt ) {}
}
