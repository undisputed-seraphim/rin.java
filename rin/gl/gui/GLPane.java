package rin.gl.gui;

import java.util.ArrayList;

import rin.gl.lib3d.properties.Properties;
import rin.util.Buffer;

public class GLPane extends GLGUIComponent<GLPane> {
	private static int items = 0;
	
	public GLPane() { this( "GLPane-" + GLPane.items++ ); }
	public GLPane( String id ) {
		super( id );
		
		float[][] verts = new float[][] {
				{ -0.5f, -0.5f, 0 }, { -0.5f, 0.5f, 0 },
				{ 0.5f, 0.5f, 0 }, { 0.5f, -0.5f, 0 }	
		};
		
		ArrayList<Float> v = new ArrayList<Float>();
		
		v.add( verts[0][0] );
		v.add( verts[0][1] );
		v.add( verts[0][2] );
		
		v.add( verts[1][0] );
		v.add( verts[1][1] );
		v.add( verts[1][2] );
		
		v.add( verts[2][0] );
		v.add( verts[2][1] );
		v.add( verts[2][2] );
		
		v.add( verts[2][0] );
		v.add( verts[2][1] );
		v.add( verts[2][2] );
		
		v.add( verts[3][0] );
		v.add( verts[3][1] );
		v.add( verts[3][2] );
		
		v.add( verts[0][0] );
		v.add( verts[0][1] );
		v.add( verts[0][2] );
		
		this.setPicking( true );
		this.setVisible( false );
		this.build( Buffer.toArrayf( v ), new float[0], new float[0], new float[0] );
	}
}
