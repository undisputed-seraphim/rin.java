package rin.gl.lib3d.data;

import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Poly;

public class GLSource {
	public float[] v = new float[0], n = new float[0], t = new float[0], c = new float[0];
	public String type = "";
	
	public Actor createActor() {
		if( this.type.equals( "Poly" ) ) {
			Poly poly = new Poly();
			poly.build( this.v, this.n, this.t, this.c );
			return poly;
		}
		return null;
	}
	
	public GLSource destroy() {
		return null;
	}
}
