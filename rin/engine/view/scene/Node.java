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
}
