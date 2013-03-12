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
		
		float[] bgcolor = new float[] { 0.1f, 0.1f, 0.1f, 1.0f };
		float[] bcolor = new float[] { 1, 1, 1, 1 };
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> t = new ArrayList<Float>();
		ArrayList<Float> c = new ArrayList<Float>();
		
		v.add( verts[0][0] );	t.add( tex[0][0] );
		v.add( verts[0][1] );	t.add( tex[0][1] );
		v.add( verts[0][2] );
		c.add( bgcolor[0] ); c.add( bgcolor[1] ); c.add( bgcolor[2] ); c.add( bgcolor[3] );
		
		v.add( verts[1][0] );	t.add( tex[1][0] );
		v.add( verts[1][1] );	t.add( tex[1][1] );
		v.add( verts[1][2] );
		c.add( bgcolor[0] ); c.add( bgcolor[1] ); c.add( bgcolor[2] ); c.add( bgcolor[3] );
		
		v.add( verts[2][0] );	t.add( tex[2][0] );
		v.add( verts[2][1] );	t.add( tex[2][1] );
		v.add( verts[2][2] );
		c.add( bgcolor[0] ); c.add( bgcolor[1] ); c.add( bgcolor[2] ); c.add( bgcolor[3] );
		
		v.add( verts[2][0] );	t.add( tex[2][0] );
		v.add( verts[2][1] );	t.add( tex[2][1] );
		v.add( verts[2][2] );
		c.add( bgcolor[0] ); c.add( bgcolor[1] ); c.add( bgcolor[2] ); c.add( bgcolor[3] );
		
		v.add( verts[1][0] );	t.add( tex[1][0] );
		v.add( verts[1][1] );	t.add( tex[1][1] );
		v.add( verts[1][2] );
		c.add( bgcolor[0] ); c.add( bgcolor[1] ); c.add( bgcolor[2] ); c.add( bgcolor[3] );
		
		v.add( verts[3][0] );	t.add( tex[3][0] );
		v.add( verts[3][1] );	t.add( tex[3][1] );
		v.add( verts[3][2] );
		c.add( bgcolor[0] ); c.add( bgcolor[1] ); c.add( bgcolor[2] ); c.add( bgcolor[3] );
		
		/* top */
		float mw = 5.0f / this.getWindowWidth();
		float mh = 5.0f / this.getWindowHeight();
		
		v.add( verts[0][0] - mw );
		v.add( verts[0][1] - mh );
		v.add( verts[0][2] );
		c.add( bcolor[0] ); c.add( bcolor[1] ); c.add( bcolor[2] ); c.add( bcolor[3] );
		
		v.add( verts[1][0] + mw );
		v.add( verts[1][1] - mh );
		v.add( verts[1][2] );
		c.add( bcolor[0] ); c.add( bcolor[1] ); c.add( bcolor[2] ); c.add( bcolor[3] );
		
		v.add( verts[0][0] - mw );
		v.add( verts[0][1] - mh * 2 );
		v.add( verts[0][2] );
		c.add( bcolor[0] ); c.add( bcolor[1] ); c.add( bcolor[2] ); c.add( bcolor[3] );
		
		v.add( verts[0][0] - mw );
		v.add( verts[0][1] - mh * 2 );
		v.add( verts[0][2] );
		c.add( bcolor[0] ); c.add( bcolor[1] ); c.add( bcolor[2] ); c.add( bcolor[3] );
		
		v.add( verts[1][0] + mw );
		v.add( verts[1][1] - mh );
		v.add( verts[1][2] );
		c.add( bcolor[0] ); c.add( bcolor[1] ); c.add( bcolor[2] ); c.add( bcolor[3] );
		
		v.add( verts[1][0] + mw );
		v.add( verts[1][1] - mh * 2 );
		v.add( verts[1][2] );
		c.add( bcolor[0] ); c.add( bcolor[1] ); c.add( bcolor[2] ); c.add( bcolor[3] );
		
		this.setPicking( true );
		this.setColored( true );
		this.build( Buffer.toArrayf( v ), new float[0], new float[0], Buffer.toArrayf( c ) );
	}
}
