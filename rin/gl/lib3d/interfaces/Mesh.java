package rin.gl.lib3d.interfaces;

import java.util.ArrayList;

public class Mesh extends Poly {
	private ArrayList<Poly> polys = new ArrayList<Poly>();
	
	public void addPoly( float[] vertices, float[] normals, float[] texcoords, String textureFile ) {
		Poly poly = new Poly();
		poly.build( vertices, normals, texcoords, new float[0] );
		poly.addTexture( textureFile );
		poly.setPicking( true );
		this.polys.add( poly );
	}
	
	public void render() {
		for( Poly p : this.polys ) {
			if( this.isUsingUnique() ) {
				p.useUniqueColor( true );
				p.render();
			} else {
				p.useUniqueColor( false );
				p.render();
			}
		}
	}
}
