package rin.engine.view.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Node {
	protected Actor target;
	
	protected Node parent;
	protected List<Node> children = new CopyOnWriteArrayList<Node>();
	
	public Node( Actor a ) {
		target = a;
	}
	
	public void add( Node n ) {
		n.parent = this;
		children.add( n );
	}
	
	public void remove( Node n ) {
		children.remove( n );
	}
	
	public void update( long dt ) {
		target.update( dt );
		
		for( Node n : children )
			n.update( dt );
	}
}
