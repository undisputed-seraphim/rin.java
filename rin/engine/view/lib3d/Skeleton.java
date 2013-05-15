package rin.engine.view.lib3d;

import rin.engine.system.ident.NodeTree;

public class Skeleton extends NodeTree<JointNode> {
	
	public Skeleton( JointNode j ) { super( j, false ); }
	public Skeleton( JointNode j, boolean cache ) { super( j, cache ); }
	
	public void update( double dt ) {
		// update this skeletons joints, traversing in the proper order by default
		for( JointNode jn : this )
			jn.update( dt );
	}
}
