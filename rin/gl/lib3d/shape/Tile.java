package rin.gl.lib3d.shape;

import java.util.ArrayList;

import rin.gl.event.GLEvent.*;
import rin.gl.lib3d.properties.Properties;
import rin.util.Buffer;
import rin.util.math.Vec3;

public class Tile extends Shape {
	private static int items = 0;
	
	private float width, height, depth;
	
	private Shape selector;

	public Tile() { this( 1.0f, 0.0f, 1.0f, new Properties() ); }
	public Tile( float width, float height, float depth, Properties p ) {
		super( "Tile-" + Tile.items++, p );
		
		this.width = width;
		this.height = height;
		this.depth = depth;
		
		this.setPicking( true );
		this.setColored( true );
		
		float x = width / 2.0f;
		float y = height / 2.0f;
		float z = depth / 2.0f;
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> n = new ArrayList<Float>();
		
		float[][] verts = new float[][]{
				{ -x, y,  z }, { -x, -y,  z }, { x, -y,  z }, { x, y,  z },		// near square
				{ -x, y, -z }, { -x, -y, -z }, { x, -y, -z }, { x, y, -z }		// far square
		};
		
		int[][] indices = new int[][] {
				{ 0, 1, 2 }, { 0, 2, 3 },	// front
				{ 4, 5, 6 }, { 4, 6, 7 },	// back
				{ 4, 5, 1 }, { 4, 1, 0 },	// left
				{ 3, 2, 6 }, { 3, 6, 7 },	// right
				{ 4, 0, 3 }, { 4, 3, 7 },	// top
				{ 1, 5, 6 }, { 1, 6, 2 }	// bottom
		};
		
		for( int i = 0; i < indices.length; i++ ) {
			// current vertex
			Vec3 v1 = new Vec3( verts[indices[i][0]][0], verts[indices[i][0]][1], verts[indices[i][0]][2] );
			Vec3 v2 = new Vec3( verts[indices[i][1]][0], verts[indices[i][1]][1], verts[indices[i][1]][2] );
			Vec3 v3 = new Vec3( verts[indices[i][2]][0], verts[indices[i][2]][1], verts[indices[i][2]][2] );

			// calculate normal
			Vec3 n1 = Vec3.normalize( Vec3.cross( Vec3.subtract( v2, v1 ), Vec3.subtract( v3, v1 ) ) );
			
			v.add( v1.x );		n.add( n1.x );
			v.add( v1.y );		n.add( n1.y );
			v.add( v1.z );		n.add( n1.z );
			
			v.add( v2.x );		n.add( n1.x );
			v.add( v2.y );		n.add( n1.y );
			v.add( v2.z );		n.add( n1.z );
			
			v.add( v3.x );		n.add( n1.x );
			v.add( v3.y );		n.add( n1.y );
			v.add( v3.z );		n.add( n1.z );
		}
		
		this.build( Buffer.toArrayf( v ), Buffer.toArrayf( n ), new float[0], new float[0] );
		
		v.clear();
		n.clear();
	}
	
	public void createBoundingBox() {
		this.selector = new Shape() {
			private float cur = 1.0f;
			private boolean goingup = false;
			public void processTickEvent( TickEvent e ) {
				if( !this.isIgnoringAnimations() ) {
					if( goingup ) {
						if( cur < 1.0f )
							cur += e.dt;
						else {
							cur -= e.dt;
							this.goingup = false;
						}
					} else {
						if( cur > 0.9f )
							cur -= e.dt;
						else {
							cur += e.dt;
							this.goingup = true;
						}
					}
					System.out.println( cur );
					this.setScale( cur, this.getScale().y, cur );
				}
			}
		};
		this.selector.setBound( false );
		this.selector.setColored( true );
		this.selector.setAnimatable( true );
		this.selector.setIgnoreAnimations( true );
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> c = new ArrayList<Float>();
		
		float X = this.getPosition().x + ( this.width / 2.0f ) - 0.005f;
		float x = this.getPosition().x - ( this.width / 2.0f ) + 0.005f;
		float Y = this.getPosition().y + ( this.height / 2.0f ) + 0.004f;
		float Z = this.getPosition().z + ( this.depth / 2.0f ) - 0.005f;
		float z = this.getPosition().z - ( this.depth / 2.0f ) + 0.005f;
		
		float[][] verts = new float[][] {
				{ x, Y, Z },
				{ x, Y, z },
				{ X, Y, z },
				{ X, Y, Z },
				{ this.getPosition().x, Y, this.getPosition().z }	
		};
		
		int[][] indices = new int[][] {
				{ 0, 1, 4 },
				{ 1, 2, 4 },
				{ 2, 3, 4 },
				{ 3, 0, 4 }	
		};
		
		for( int i = 0; i < indices.length; i++ ) {
			for( int j = 0; j < indices[i].length; j++ ) {
				v.add( verts[indices[i][j]][0] );
				v.add( verts[indices[i][j]][1] );
				v.add( verts[indices[i][j]][2] );
				
				c.add( 0.0f );
				c.add( 0.8f );
				c.add( 0.9f );
				if( indices[i][j] == 4 ) {
					c.add( 0.4f );
				} else {
					c.add( 1.0f );
				}
			}
		}
		
		this.selector.build( Buffer.toArrayf( v ), new float[0], new float[0], Buffer.toArrayf( c ) );
		
		v.clear();
		c.clear();
	}
	
	public void showBoundingBox() {
		this.selector.render();
	}
	
	public void processPickInEvent( PickInEvent e ) {
		super.processPickInEvent( e );
		this.selector.setIgnoreAnimations( false );
	}
	
	public void processPickOutEvent( PickOutEvent e ) {
		super.processPickOutEvent( e );
		this.selector.setIgnoreAnimations( true );
	}
	
	public Tile destroy() {
		super.destroy();
		return null;
	}
}
