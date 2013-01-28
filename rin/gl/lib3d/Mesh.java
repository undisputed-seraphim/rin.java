package rin.gl.lib3d;

import java.util.ArrayList;

import rin.gl.Scene;
import rin.gl.lib3d.GLInterleavedBuffer.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh extends Pickable {	
	/* Mesh are broken into polys per texture [opengl effeciency] */
	private ArrayList<Poly> polys = new ArrayList<Poly>();
	
	public void addPoly( String name, float[] v, float[] n, float[] t, String texture ) {
		this.getBounds( v );
		this.polys.add( new Poly( name, v, n, t, texture ) );
	}
	
	private void getBounds( float[] v ) {
		for( int i = 0; i < v.length; i += 3 )
			this.addBounds( v[i], v[i+1], v[i+2] );
	}
	
	private void getPolyData( ArrayList<Float> v, ArrayList<Float> n, ArrayList<Float> t ) {
		int i = 0;
		for( Poly p : this.polys ) {
			p.range[0] = i;
			v.addAll( p.getVertices() );
			n.addAll( p.getNormals() );
			t.addAll( p.getTexcoords() );
			i += p.getVertices().size() / 3;
			p.range[1] = i;
		}
	}
	
	/* final arrays for storing indices, vertex, normal, texture coordinates data from polys */
	private int[] iba;
	private float[] aba;
	
	protected GLBuffer ibuf = null;
	protected GLInterleavedBuffer abuf = null;
	
	/* mesh is ready to be rendered */
	private boolean ready = false;
	
	/* initialize the buffer objects for the mesh */
	public void init() {
		this.ready = false;
		
		for( Poly p : this.polys )
			p.createTexture();
		
		this.build();
		
		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, this.iba );
		this.abuf = new GLInterleavedBuffer( GL_ARRAY_BUFFER, this.aba )
				.addIndex( IndexType.VERTEX, 3, Scene.getAttrib( "vertex" ) )
				.addIndex( IndexType.NORMAL, 3, Scene.getAttrib( "normal" ) )
				.addIndex( IndexType.TEXCOORD, 2, Scene.getAttrib( "texture" ) )
				.build();
		
		if( this.bound )
			this.createBoundingBox();
		
		this.ready = true;
	}
	
	/* combine all poly information into one single array */
	public void build() {		
		ArrayList<Float> verts = new ArrayList<Float>();
		ArrayList<Float> norms = new ArrayList<Float>();
		ArrayList<Float> texts = new ArrayList<Float>();
		this.getPolyData( verts, norms, texts );
		
		this.aba = new float[ verts.size() + norms.size() + texts.size() ];
		this.iba = new int[ verts.size() / 3 ];
		
		for( int i = 0, a = 0; i < verts.size() / 3; i++ ) {
			this.aba[a++] = verts.get( i*3 );
			this.aba[a++] = verts.get( i*3+1 );
			this.aba[a++] = verts.get( i*3+2 );
			this.aba[a++] = norms.get( i*3 );
			this.aba[a++] = norms.get( i*3+1 );
			this.aba[a++] = norms.get( i*3+2 );
			this.aba[a++] = texts.get( i*2 );
			this.aba[a++] = texts.get( i*2+1 );
			this.iba[i] = i;
		}
		
		verts.clear(); verts = null;
		norms.clear(); norms = null;
		texts.clear(); texts = null;
	}
	
	public boolean buffer() {
		this.abuf.buffer();
		return this.ibuf.buffer();
	}
	
	public void render() {
		if( this.ready ) {
			if( this.buffer() ) {
				glUniformMatrix4( Scene.getUniform( "mMatrix"), false, this.matrix.gl() );
				for( Poly p : this.polys ) {
					p.applyTexture();
					int[] cur = p.range;
					glDrawRangeElements( GL_TRIANGLES, 0, cur[1], cur[1] - cur[0], GL_UNSIGNED_INT, cur[0] * 4 );
				}
			}
		}
	}
	
	public Mesh destroy() {
		this.ready = false;
		
		for( Poly p : this.polys )
			p = p.destroy();
		
		this.polys.clear();
		
		this.abuf = this.abuf != null ? this.abuf.destroy() : null;
		this.ibuf = this.ibuf != null ? this.ibuf.destroy() : null;
		
		this.bbox = this.bbox != null ? this.bbox.destroy() : null;
		
		return null;
	}
}
