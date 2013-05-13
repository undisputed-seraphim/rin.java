package rin.gl.lib3d;

import java.util.ArrayList;

public class Node extends Poly {
	protected ModelScene scene;
	protected String name;

	protected Node parent;
	protected ArrayList<Node> children = new ArrayList<Node>();
	
	public Node( String n ) { name = n; }
	
	public ArrayList<Node> getChildren() { return children; }
	public String getName() { return name; }
	
	public Node add( Node n ) {
		n.scene = scene;
		n.parent = this;
		children.add( n );
		scene.update();
		return children.get( children.size() - 1 );
	}
	
	public void remove( Node n ) {
		children.remove( n );
		scene.update();
	}
	
	public Node find( String name ) {
		for( Node n : children )
			if( n.getName().equalsIgnoreCase( name ) )
				return n;
		return null;
	}
	
	public void update( double dt ) {
		if( parent != null ) {
		}
	}
	
	@Override
	public void render() {}
}
