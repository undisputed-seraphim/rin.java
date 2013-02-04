package rin.gl.lib3d;

import java.util.ArrayList;

import rin.gl.GL;
import rin.gl.event.GLEvent;
import rin.gl.lib3d.GLInterleavedBuffer.*;
import rin.util.math.Mat4;
import rin.util.math.Vec3;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Mesh extends Controllable {
	/* Mesh are broken into polys per texture [opengl effeciency] */
	private ArrayList<Poly> polys = new ArrayList<Poly>();
	
	public Mesh() { this( "No Name" ); }
	public Mesh( String str ) { this.setName( str ); }
	
	public Poly getPoly( int index ) { return this.polys.get( index ); }
	public ArrayList<Poly> getPolys() { return this.polys; }
	public void addPoly( String name, float[] v, float[] n, float[] t, String texture ) {
		this.getBounds( v );
		this.polys.add( new Poly( name, v, n, t, texture ) );
	}
	
	private void getPolyData() {
		int i = 0;
		this.v.clear();
		this.n.clear();
		this.t.clear();
		for( Poly p : this.polys ) {
			p.range[0] = i;
			this.v.addAll( p.getVertices() );
			this.n.addAll( p.getNormals() );
			this.t.addAll( p.getTexcoords() );
			i += p.getVertices().size() / 3;
			p.range[1] = i;
		}
	}
	
	/* final arrays for storing indices, vertex, normal, texture coordinates data from polys */
	private int[] iba;
	private float[] aba;
	
	protected GLInterleavedBuffer abuf = null;
	
	/* mesh will merge all poly data into one interleaved array */
	private boolean interleaved = true;
	public void setInterleaved( boolean val ) { this.interleaved = val; }
	public boolean isInterleaved() { return this.interleaved; }
	
	/* mesh contains pickable polys */
	private boolean polyPicking = false;
	public void setPolyPicking( boolean val ) { this.polyPicking = val; }
	public boolean isPolyPicking() { return this.polyPicking; }
	
	/* initialize the buffer objects for the mesh */
	public void init() {
		this.ready = false;
		
		if( this.bound ) {
			this.createBoundingBox();
			/* physics */
			this.setHeight( this.yMax - this.yMin );
		}
		
		if( this.isPolyPicking() )
			for( Poly p : this.polys )
				p.listenForPicking();
		
		else if( this.isPicking() )
			this.listenForPicking();
		
		if( this.interleaved ) {
			for( Poly p : this.polys ) {
				p.createTexture();
			}
			
			this.build();
			
			this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, this.iba );
			this.abuf = new GLInterleavedBuffer( GL_ARRAY_BUFFER, this.aba )
					.addIndex( IndexType.VERTEX, 3, GL.getAttrib( "vertex" ) );
			if( this.n.size() > 0 )
				this.abuf.addIndex( IndexType.NORMAL, 3, GL.getAttrib( "normal" ) );
			
			if( this.t.size() > 0 )
				this.abuf.addIndex( IndexType.TEXCOORD, 2, GL.getAttrib( "texture" ) );
			
			this.abuf.build();
		}
		
		else {
			for( Poly p : this.polys ) {
				p.init();
				if( this.isPolyPicking() )
					p.listenForPicking();
			}
		}
		
		this.ready = true;
	}
	
	/* combine all poly information into one single array */
	public void build() {
		this.getPolyData();
		
		this.aba = new float[ this.v.size() + this.n.size() + this.t.size() ];
		this.iba = new int[ this.v.size() / 3 ];
		
		for( int i = 0, a = 0; i < this.v.size() / 3; i++ ) {
			this.aba[a++] = this.v.get( i*3 );
			this.aba[a++] = this.v.get( i*3+1 );
			this.aba[a++] = this.v.get( i*3+2 );
			
			if( this.n.size() > 0 ) {
				this.aba[a++] = this.n.get( i*3 );
				this.aba[a++] = this.n.get( i*3+1 );
				this.aba[a++] = this.n.get( i*3+2 );
			}
			
			if( this.t.size() > 0 ) {
				this.aba[a++] = this.t.get( i*2 );
				this.aba[a++] = this.t.get( i*2+1 );
			}
			
			this.iba[i] = i;
		}
	}
	
	public boolean buffer() {
		this.abuf.buffer();
		return this.ibuf.buffer();
	}
	
	public void render( int renderMode, boolean unique ) {
		if( this.ready ) {
			if( this.interleaved ) {
				if( this.buffer() ) {
					this.matrix = Mat4.apply( this.matrix, GL.getScene().getGravity(), new Vec3( 0.0f, -1.0f, 0.0f ) );
					glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.matrix.gl() );
					for( Poly p : this.polys ) {
						if( unique ) {
							p.setColored( true );
							p.setColor( this.getUniqueColor() );
						} else p.setColored( false );
						p.applyTexture();
						int[] cur = p.range;
						glDrawRangeElements( GL_TRIANGLES, 0, cur[1], cur[1] - cur[0], GL_UNSIGNED_INT, cur[0] * 4 );
					}
				}
			}
			
			else {
				for( Poly p : this.polys ) {
					if( unique ) {
						p.setColor( this.getUniqueColor() );
						p.setColored( true );
						p.render( p.getRenderMode(), true, true );
					} else {
						p.setColored( false );
						p.render();
					}
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
		
		if( this.isPickListening() );
			//GLEvent.removePickEventListener( this );
		
		return null;
	}
}
