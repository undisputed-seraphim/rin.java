package rin.gl.lib3d;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.GL;
import rin.gl.TextureManager;
import rin.gl.event.GLEvent;
import rin.gl.event.GLEvent.*;
import rin.gl.lib3d.interfaces.*;
import rin.gl.lib3d.GLBuffer;
import rin.gl.lib3d.GLInterleavedBuffer;
import rin.gl.lib3d.Transformation;
import rin.gl.lib3d.GLInterleavedBuffer.IndexType;
import rin.gl.lib3d.shape.BoundingBox;
import rin.util.Buffer;
import rin.util.math.Vec3;

public class Poly extends Actor implements Renderable, Boundable, Pickable {
	private static final float	POS_INF = Float.POSITIVE_INFINITY,
								NEG_INF = Float.NEGATIVE_INFINITY;
	
	public Poly() { this( "No Name Actor", new Transformation() ); }
	public Poly( String name ) { this( name, new Transformation() ); }
	public Poly( Transformation t ) { this( "No Name Actor", t ); }
	public Poly( String name, Transformation t ) { super( name, t ); }

	/* ------------------ boundable implementation ------------------ */
	private boolean bound = true;
	@Override public boolean isBound() { return this.bound; }
	@Override public void setBound( boolean val ) { this.bound = val; }
	
	private BoundingBox bbox = null;
	@Override public BoundingBox getBoundingBox() { return this.bbox; }
	
	private float xMin = POS_INF, xMax = NEG_INF, yMin = POS_INF, yMax = NEG_INF, zMin = POS_INF, zMax = NEG_INF;
	@Override public void computeBounds( float[] vertices ) {
		this.xMin = POS_INF; this.xMax = NEG_INF;
		this.yMin = POS_INF; this.yMax = NEG_INF;
		this.zMin = POS_INF; this.zMax = NEG_INF;
		for( int i = 0; i < vertices.length; i += 3 )
			this.addBounds( vertices[i], vertices[i+1], vertices[i+2] );
	}
	
	private void addBounds( float x, float y, float z ) {
		xMin = Math.min( xMin, x );
		yMin = Math.min( yMin, y );
		zMin = Math.min( zMin, z );
		
		xMax = Math.max( xMax, x );
		yMax = Math.max( yMax, y );
		zMax = Math.max( zMax, z );
	}
	
	@Override public void createBoundingBox() {
		this.bbox = new BoundingBox( this.xMin, this.yMin, this.zMin, this.xMax, this.yMax, this.zMax, this.getPosition() );
		this.ready = true;
	}
	
	@Override public void showBoundingBox() {
		if( this.bbox != null ) {
			this.bbox.setMatrix( this.getMatrix() );
			this.bbox.render();
		}
	}
	
	
	/* ---------------- renderable implementation ----------------- */	
	private boolean ready = false;
	@Override public boolean isReady() { return this.ready; }
	
	private boolean visible = true;
	@Override public boolean isVisible() { return this.visible; }
	@Override public void setVisible( boolean val ) { this.visible = val; }
	
	private int renderMode = GL_TRIANGLES;
	@Override public int getRenderMode() { return this.renderMode; }
	@Override public void setRenderMode( int renderMode ) { this.renderMode = renderMode; }
	
	private GLBuffer ibuf = null;
	private GLInterleavedBuffer abuf = null;
	
	private int indexCount = 0;
	@Override public int getIndexCount() { return this.indexCount; }
	
	private boolean colored = false;
	@Override public boolean isColored() { return this.colored; }
	@Override public void setColored( boolean val ) { this.colored = val; }
	
	private float[] color = new float[] { 1.0f, 0.0f, 0.0f, 1.0f };
	@Override public void setColor( float r, float g, float b, float a ) { this.color = new float[]{ r, g, b, a }; }
	@Override public float[] getColor() { return this.color; }
	
	private int texture = -1;
	@Override public void addTexture( String textureFile ) {
		int tmp = this.texture;
		this.texture = TextureManager.load( textureFile );
		TextureManager.unload( tmp );
	}
	
	@Override public void bindTexture() {
		if( this.colored ) {
			glUniform1i( GL.getUniform( "useColor" ), GL_TRUE );
		} else {
			glUniform1i( GL.getUniform( "useColor" ), GL_FALSE );
			if( this.texture != -1 )
				TextureManager.enable( this.texture );
		
			else
				TextureManager.disable();
		}
	}
	
	public void build( float[] vertices, float[] normals, float[] texcoords, String textureFile ) {
		this.build( vertices, normals, texcoords, new float[0] );
		this.addTexture( textureFile );
	}
	
	@Override public void build( float[] vertices, float[] normals, float[] texcoords, float[] colors ) {
		this.ready = false;
		
		if( this.ibuf != null ) this.ibuf = this.ibuf.destroy();
		if( this.abuf != null ) this.abuf = this.abuf.destroy();
		
		this.indexCount = vertices.length / 3;
		float[] aba = new float[ this.indexCount * 4 * 4 ];
		int[] iba = new int[ this.indexCount ];
		
		float[] unique = this.getUniqueColor();
		float[] color = this.getColor();
		try {
			for( int i = 0, a = 0; i < this.indexCount; i++ ) {
				aba[a++] = vertices[ i*3 ];
				aba[a++] = vertices[ i*3+1 ];
				aba[a++] = vertices[ i*3+2 ];
				aba[a++] = unique[0];
				
				/* if color data obtained, use. else, default red */
				if( colors.length > 0 ) {
					aba[a++] = colors[ i*4 ];
					aba[a++] = colors[ i*4+1 ];
					aba[a++] = colors[ i*4+2 ];
					aba[a++] = colors[ i*4+3 ];
				} else {
					aba[a++] = color[0];
					aba[a++] = color[1];
					aba[a++] = color[2];
					aba[a++] = color[3];
				}
				
				if( normals.length > 0 ) {
					aba[a++] = normals[ i*3 ];
					aba[a++] = normals[ i*3+1 ];
					aba[a++] = normals[ i*3+2 ];
				} else {
					aba[a++] = 0.0f;
					aba[a++] = 0.0f;
					aba[a++] = 0.0f;
				}
				aba[a++] = unique[1];
				
				if( texcoords.length > 0 ) {
					aba[a++] = texcoords[ i*2 ];
					aba[a++] = texcoords[ i*2+1 ];
					aba[a++] = 1.0f;
				} else {
					aba[a++] = 0.0f;
					aba[a++] = 0.0f;
					aba[a++] = 0.0f;
				}
				aba[a++] = unique[2];
				
				iba[i] = i;
			}
		} catch( ArrayIndexOutOfBoundsException e ) {
			System.out.println( "[WARNING] Invalid indexed arrays. Poly rejected." );
			return;
		}

		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, iba );
		this.abuf = new GLInterleavedBuffer( GL_ARRAY_BUFFER, aba )
				.addIndex( IndexType.VERTEX, 4, GL.getAttrib( "vertex" ) )
				.addIndex( IndexType.COLOR, 4, GL.getAttrib( "color" ) )
				.addIndex( IndexType.NORMAL, 4, GL.getAttrib( "normal" ) )
				.addIndex( IndexType.TEXCOORD, 4, GL.getAttrib( "texture" ) )
				.build();

		if( this.bound ) {
			this.computeBounds( vertices );
			this.createBoundingBox();
		}
		
		aba = null;
		iba = null;
		this.ready = true;
	}
	
	@Override public boolean buffer() {
		this.abuf.buffer();
		return this.ibuf.buffer();
	}
	
	@Override public void render() { this.render( false ); }
	public void render( boolean unique ) {
		if( this.ready && this.visible ) {
			glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
			//glDrawRangeElements( GL_TRIANGLES, 0, cur[1], cur[1] - cur[0], GL_UNSIGNED_INT, cur[0] * 4 );
			if( this.buffer() ) {
				this.bindTexture();
				glDrawElements( this.renderMode, this.indexCount, GL_UNSIGNED_INT, 0 );
			}
			
			if( this.isMouseOver() && !unique )
				this.showBoundingBox();
		}
	}
	
	
	/* --------------------  pickable implementation -------------------- */
	private boolean mouseOver = false;
	public boolean isMouseOver() { return this.mouseOver; }
	public void setMouseOver( boolean val ) { this.mouseOver = val; }
	
	private boolean picking = false;
	private boolean pickableListening = false;
	@Override public boolean isPicking() { return this.picking; }
	@Override public void setPicking( boolean val ) {
		this.picking = val;
		if( val && !this.pickableListening ) {
			GLEvent.addPickEventListener( this );
			this.pickableListening = true;
		} else if( this.pickableListening ) {
			GLEvent.removePickEventListener( this );
			this.pickableListening = false;
		}
	}
	
	@Override public void processPickInEvent( PickInEvent e ) {
		System.out.println( "in" );
		this.setMouseOver( true );
	}
	
	@Override public void processPickOutEvent( PickOutEvent e ) {
		System.out.println( "out" );
		this.setMouseOver( false );
	}
	
	@Override public void processPickRepeatEvent( PickRepeatEvent e ) {
		//System.out.println( "pick repeat " + this.getName() );
	}
	
	public Poly destroy() {
		super.destroy();
		
		if( this.abuf != null ) this.abuf = this.abuf.destroy();
		if( this.ibuf != null ) this.ibuf = this.ibuf.destroy();
		
		if( this.bbox != null ) this.bbox = this.bbox.destroy();
		
		if( this.isPicking() )
			this.setPicking( false );
		
		if( this.texture != -1 )
			TextureManager.unload( this.texture );
		
		return null;
	}
}