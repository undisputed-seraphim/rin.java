package rin.gl.lib3d;

import static org.lwjgl.opengl.GL20.glUniformMatrix4;

import java.util.ArrayList;

import rin.gl.GL;
import rin.gl.lib3d.Poly;

public class Mesh extends Poly {
	private ArrayList<Poly> polys = new ArrayList<Poly>();
	
	private boolean polyPicking = false;
	public boolean isPolyPicking() { return this.polyPicking; }
	public void setPolyPicking( boolean val ) { this.polyPicking = val; }
	
	public Mesh() {}
	public Mesh( String name ) { super( name ); }
	
	public void addPoly( float[] vertices, float[] normals, float[] texcoords, String textureFile ) {
		Poly poly = new Poly();
		if( this.isPolyPicking() )
			poly.setPicking( true );
		else
			poly.setUniqueColor( this.getUniqueColor() );
		poly.build( vertices, normals, texcoords, new float[0] );
		poly.addTexture( textureFile );
		poly.setApplyTransform( false );
		this.polys.add( poly );
	}
	
	public void render( boolean unique ) {
		glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
		for( Poly p : this.polys ) {
			p.render( unique );
		}
		
		if( this.isMouseOver() && !unique )
			this.showBoundingBox();
	}
}
