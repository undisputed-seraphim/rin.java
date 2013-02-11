package rin.gl.lib3d.interfaces;

import java.util.ArrayList;

public class Mesh extends Poly {
	private ArrayList<Poly> polys = new ArrayList<Poly>();
	
	private boolean polyPicking = false;
	public boolean isPolyPicking() { return this.polyPicking; }
	public void setPolyPicking( boolean val ) { this.polyPicking = val; }
	
	public void addPoly( float[] vertices, float[] normals, float[] texcoords, String textureFile ) {
		Poly poly = new Poly();
		if( this.isPolyPicking() )
			poly.setPicking( true );
		else
			poly.setUniqueColor( this.getUniqueColor() );
		poly.build( vertices, normals, texcoords, new float[0] );
		poly.addTexture( textureFile );
		this.polys.add( poly );
	}
	
	public void render( boolean unique ) {
		for( Poly p : this.polys ) {
			p.render( unique );
		}
		
		if( this.isMouseOver() && !unique )
			this.showBoundingBox();
	}
}
