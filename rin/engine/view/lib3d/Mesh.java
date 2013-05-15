package rin.engine.view.lib3d;

import rin.engine.system.ident.NodeTree;

public class Mesh extends NodeTree<RenderNode> {

	public Mesh( RenderNode j ) { super( j, false ); }
	public Mesh( RenderNode j, boolean cache ) { super( j, cache ); }
	
	public void update( double dt ) {}
	public void render() {
		for( RenderNode rn : this )
			rn.render();
	}
}
