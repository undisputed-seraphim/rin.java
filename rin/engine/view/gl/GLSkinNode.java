package rin.engine.view.gl;

import rin.engine.view.lib3d.SkinNode;

public class GLSkinNode extends SkinNode {

	public GLSkinNode( String id ) { super( id ); }
	@Override public GLSkinNode actual() { return this; }
	
	@Override
	public void render() {
		System.out.println( "rendering skin node " + name );
	}
}
