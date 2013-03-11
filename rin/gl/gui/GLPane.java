package rin.gl.gui;

import java.util.ArrayList;

import rin.engine.Engine;
import rin.gl.lib3d.properties.Properties;
import rin.util.Buffer;

public class GLPane extends GLGUIComponent<GLPane> {
	private static int items = 0;
	
	public GLPane() { this( "GLPane-" + GLPane.items++ ); }
	public GLPane( String id ) { this( id, new GLGUIFactory.GLGUIParams() ); }
	public GLPane( String id, GLGUIFactory.GLGUIParams p ) {
		super( id, p );
		
		float[][] verts = this.getGUIVertices();
		float[][] tex = new float[][] {
				{ 0.0f, 1.0f }, { 1.0f, 1.0f },
				{ 0.0f, 0.0f }, { 1.0f, 0.0f }
		};
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> t = new ArrayList<Float>();
		
		v.add( verts[0][0] );	t.add( tex[0][0] );
		v.add( verts[0][1] );	t.add( tex[0][1] );
		v.add( verts[0][2] );
		
		v.add( verts[1][0] );	t.add( tex[1][0] );
		v.add( verts[1][1] );	t.add( tex[1][1] );
		v.add( verts[1][2] );
		
		v.add( verts[2][0] );	t.add( tex[2][0] );
		v.add( verts[2][1] );	t.add( tex[2][1] );
		v.add( verts[2][2] );
		
		v.add( verts[2][0] );	t.add( tex[2][0] );
		v.add( verts[2][1] );	t.add( tex[2][1] );
		v.add( verts[2][2] );
		
		v.add( verts[1][0] );	t.add( tex[1][0] );
		v.add( verts[1][1] );	t.add( tex[1][1] );
		v.add( verts[1][2] );
		
		v.add( verts[3][0] );	t.add( tex[3][0] );
		v.add( verts[3][1] );	t.add( tex[3][1] );
		v.add( verts[3][2] );
		
		this.setPicking( true );
		this.build( Buffer.toArrayf( v ), new float[0], Buffer.toArrayf( t ), Engine.IMG_DIR + "gui/bg.png" );
	}
}
