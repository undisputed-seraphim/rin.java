package rin.gl.lib3d.interfaces;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import rin.gl.TextureManager;
import rin.gl.event.GLEvent;
import rin.gl.event.GLEvent.PickEvent;
import rin.gl.lib3d.GLBuffer;
import rin.gl.lib3d.GLInterleavedBuffer;
import rin.gl.lib3d.GLInterleavedBuffer.IndexType;
import rin.gl.lib3d.shape.BoundingBox;
import rin.util.math.Vec3;

public class Poly extends Actor implements Renderable, Boundable, Pickable {

	public Poly() { this( "No Name Poly" ); }
	public Poly( String name ) { this( name, new Vec3( 0.0f, 0.0f, 0.0f ) ); }
	public Poly( String name, Vec3 position ) { this( name, position, new Vec3( 0.0f, 0.0f, 0.0f ) ); }
	public Poly( String name, Vec3 position, Vec3 rotation ) { this( name, position, rotation, new Vec3( 1.0f, 1.0f, 1.0f ) ); }
	public Poly( String name, Vec3 position, Vec3 rotation, Vec3 scale ) {
		super( name, position, rotation, scale );
	}
	

	/* ------------------ boundable implementation ------------------ */
	private boolean bound = true;
	@Override public boolean isBound() { return this.bound; }
	@Override public void setBound( boolean val ) { this.bound = val; }
	
	private BoundingBox bbox = null;
	@Override public BoundingBox getBoundingBox() { return this.bbox; }
	
	private float	xMin = Float.POSITIVE_INFINITY, xMax = Float.NEGATIVE_INFINITY,
					yMin = Float.POSITIVE_INFINITY, yMax = Float.NEGATIVE_INFINITY,
					zMin = Float.POSITIVE_INFINITY, zMax = Float.NEGATIVE_INFINITY;
	@Override public void computeBounds( float[] vertices ) {
		this.xMin = Float.POSITIVE_INFINITY; this.xMax = Float.NEGATIVE_INFINITY;
		this.yMin = Float.POSITIVE_INFINITY; this.yMax = Float.NEGATIVE_INFINITY;
		this.zMin = Float.POSITIVE_INFINITY; this.zMax = Float.NEGATIVE_INFINITY;
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
	
	@Override public void showBoundingBox() { this.showBoundingBox( this.bbox.getRenderMode(), false ); }
	public void showBoundingBox( int renderMode ) { this.showBoundingBox( renderMode, false ); }
	public void showBoundingBox( int renderMode, boolean unique ) {
		if( this.bbox != null && this.bbox.isReady() ) {
			/*if( unique ) {
				this.bbox.setColor( this.getUniqueColor() );
				this.bbox.setColored( true );*/
				this.bbox.render();
			/*} else {
				this.bbox.render( renderMode );
			}*/
		}
	}
	
	
	/* ---------------- renderable implementation ----------------- */	
	private boolean ready = false;
	@Override public boolean isReady() { return this.ready; }
	
	private boolean visible = true;
	@Override public boolean isVisible() { return this.visible; }
	@Override public void setVisible( boolean val ) { this.visible = val; }
	
	private boolean useUnique = false;
	@Override public boolean isUsingUnique() { return this.useUnique; }
	@Override public void useUniqueColor( boolean val ) { this.useUnique = val; }
	
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
	
	private float[] color = new float[] { 0.5f, 0.0f, 0.5f, 0.5f };
	@Override public void setColor( float r, float g, float b, float a ) { this.color = new float[]{ r, g, b, a }; }
	@Override public float[] getColor() { return this.color; }
	
	private int texture = -1;
	@Override public void addTexture( String textureFile ) {
		int tmp = this.texture;
		this.texture = TextureManager.load( textureFile );
		TextureManager.unload( tmp );
	}
	
	@Override public void bindTexture() {
		if( this.useUnique ) {
			glUniform1i( GL.getUniform( "useColor" ), GL_TRUE );
			float[] color = this.getUniqueColor();
			glUniform4f( GL.getUniform( "color" ), color[0], color[1], color[2], 1.0f );
		} else if( this.colored ) {
			glUniform1i( GL.getUniform( "useColor" ), GL_TRUE );
			glUniform4f( GL.getUniform( "color" ), this.color[0], this.color[1], this.color[2], this.color[3] );
		} else {
			glUniform1i( GL.getUniform( "useColor" ), GL_FALSE );
			if( this.texture != -1 )
				TextureManager.enable( this.texture );
		
			else
				TextureManager.disable();
		}
	}
	
	public void build( float[] vertices, float[] normals, float[] texcoords, String textureFile ) {
		this.build( vertices, normals, texcoords );
		this.addTexture( textureFile );
	}
	
	@Override public void build( float[] vertices, float[] normals, float[] texcoords ) {
		this.ready = false;
		
		if( this.ibuf != null ) this.ibuf = this.ibuf.destroy();
		if( this.abuf != null ) this.abuf = this.abuf.destroy();
		
		this.indexCount = vertices.length / 3;
		float[] aba = new float[ vertices.length + /*this.indexCount * 4 +*/ normals.length + texcoords.length ];
		int[] iba = new int[ this.indexCount ];
		
		//float[] color = this.getUniqueColor();
		try {
			for( int i = 0, a = 0; i < vertices.length / 3; i++ ) {
				aba[a++] = vertices[ i*3 ];
				aba[a++] = vertices[ i*3+1 ];
				aba[a++] = vertices[ i*3+2 ];
				
				/*aba[a++] = color[0];
				aba[a++] = color[1];
				aba[a++] = color[2];
				aba[a++] = 1.0f;*/
				
				if( normals.length > 0 ) {
					aba[a++] = normals[ i*3 ];
					aba[a++] = normals[ i*3+1 ];
					aba[a++] = normals[ i*3+2 ];
				}
				
				if( texcoords.length > 0 ) {
					aba[a++] = texcoords[ i*2 ];
					aba[a++] = texcoords[ i*2+1 ];
				}
				
				iba[i] = i;
			}
		} catch( ArrayIndexOutOfBoundsException e ) {
			System.out.println( "[WARNING] Invalid indexed arrays. Poly rejected." );
			return;
		}

		this.ibuf = new GLBuffer( GL_ELEMENT_ARRAY_BUFFER, iba );
		this.abuf = new GLInterleavedBuffer( GL_ARRAY_BUFFER, aba )
				.addIndex( IndexType.VERTEX, 3, GL.getAttrib( "vertex" ) );
				//.addIndex( IndexType.COLOR, 4, GL.getAttrib( "unique" ) );
		if( normals.length > 0 ) this.abuf.addIndex( IndexType.NORMAL, 3, GL.getAttrib( "normal" ) );
		if( texcoords.length > 0 ) this.abuf.addIndex( IndexType.TEXCOORD, 2, GL.getAttrib( "texture" ) );
		this.abuf.build();

		if( this.bound ) {
			this.computeBounds( vertices );
			this.createBoundingBox();
		}
		
		this.ready = true;
	}
	
	@Override public boolean buffer() {
		this.abuf.buffer();
		return this.ibuf.buffer();
	}
	
	@Override public void render() {
		if( this.ready && this.visible ) {
			glUniformMatrix4( GL.getUniform( "mMatrix"), false, this.getMatrix().gl() );
			//glDrawRangeElements( GL_TRIANGLES, 0, cur[1], cur[1] - cur[0], GL_UNSIGNED_INT, cur[0] * 4 );
			if( this.buffer() ) {
				this.bindTexture();
				glDrawElements( this.renderMode, this.indexCount, GL_UNSIGNED_INT, 0 );
			}
		}
	}
	
	
	/* --------------------  pickable implementation -------------------- */
	private boolean pickableListening = false;
	
	private boolean picking = false;
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
	
	@Override public void processPickEvent( PickEvent e ) {
		System.out.println( "pick eventz" );
		this.showBoundingBox();
	}
	
	
	public Poly destroy() {
		super.destroy();
		
		if( this.abuf != null ) this.abuf = this.abuf.destroy();
		if( this.ibuf != null ) this.ibuf = this.ibuf.destroy();
		
		this.bbox = this.bbox != null ? this.bbox.destroy() : null;
		
		if( this.isPicking() )
			this.setPicking( false );
		
		if( this.texture != -1 )
			TextureManager.unload( this.texture );
		
		return null;
	}
}
