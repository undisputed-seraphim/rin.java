package rin.gl.lib3d;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.GL;
import rin.gl.TextureManager;
import rin.gl.event.GLEvent;
import rin.gl.event.Transition;
import rin.gl.event.GLEvent.*;
import rin.gl.lib3d.interfaces.*;
import rin.gl.lib3d.data.GLBuffer;
import rin.gl.lib3d.data.GLInterleavedBuffer;
import rin.gl.lib3d.data.GLInterleavedBuffer.IndexType;
import rin.gl.lib3d.properties.Color;
import rin.gl.lib3d.properties.Properties;
import rin.gl.lib3d.shape.BoundingBox;
import rin.util.math.Mat4;

public class Poly extends Actor implements Renderable, Boundable, Pickable, Transitionable {
	private static int items = 0;
	
	private static final float	POS_INF = Float.POSITIVE_INFINITY,
								NEG_INF = Float.NEGATIVE_INFINITY;
	
	public Poly() { this( "Poly-" + Poly.items++, new Properties() ); }
	public Poly( String name ) { this( name, new Properties() ); }
	public Poly( Properties p ) { this( "Poly-" + Poly.items++, p ); }
	public Poly( String name, Properties p ) { super( name, p ); this.applyProperties( p ); }

	public Properties getProperties() { return new Properties( this.getTransformation(), this.getColor() ); }
	public void applyProperties( Properties p ) {
		this.setTransformation( p.getTransformation() );
		this.setColor( p.getColor() );
	}
	
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
		this.bbox = new BoundingBox( this.xMin, this.yMin, this.zMin, this.xMax, this.yMax, this.zMax, this.getProperties() );
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
	
	private boolean applyTransform = true;
	public boolean isApplyingTransform() { return this.applyTransform; }
	public void setApplyTransform( boolean val ) { this.applyTransform = val; }
	
	private GLBuffer ibuf = null;
	private GLInterleavedBuffer abuf = null;
	
	private int indexCount = 0;
	@Override public int getIndexCount() { return this.indexCount; }
	
	private boolean colored = false;
	@Override public boolean isColored() { return this.colored; }
	@Override public void setColored( boolean val ) { this.colored = val; }
	
	private float[] color = new float[] { 1.0f, 0.0f, 0.0f, 1.0f };
	public Color getColor() { return new Color( this.color[0], this.color[1], this.color[2], this.color[3] ); }
	public void setColor( Color c ) { this.setColor( c.getR(), c.getG(), c.getB(), c.getA() ); }
	@Override public void setColor( float r, float g, float b, float a ) { this.color = new float[]{ r, g, b, a }; }
	@Override public float[] getColorAsArray() { return this.color; }
	
	private int texture = -1;
	@Override public void addTexture( String textureFile ) {
		if( !textureFile.equals( "" ) ) {
			int tmp = this.texture;
			this.texture = TextureManager.load( textureFile );
			TextureManager.unload( tmp );
		}
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
		float[] color = this.getColorAsArray();
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

		/*GLBuffer tmp = GL.<GLBuffer>createBuffer( GLBuffer.class, GL_ELEMENT_ARRAY_BUFFER, iba );
		System.out.println( Buffer.toString( tmp.readi() ) );
		return;*/
		
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
	
	public GLBuffer getIBuffer() { return this.ibuf; }
	
	@Override public boolean buffer() {
		this.abuf.buffer();
		return this.ibuf.buffer();
	}
	
	private Mat4 lock = new Mat4();
	@Override public void render() { this.render( false ); }
	public void render( boolean unique ) {
		if( this.ready && this.visible ) {
			if( this.applyTransform )
				synchronized( this.lock ) {
					glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
				}
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
	
	@Override public void applyTransition( Transition t ) {
	}
	
	@Override public void onTransitionBegin(Transition t) {
		System.out.println( "Transition starting!" );
	}
	
	@Override public void onTransitionEnd(Transition t) {
		System.out.println( "Transition ending!" );
	}
}