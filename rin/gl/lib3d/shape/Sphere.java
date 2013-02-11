package rin.gl.lib3d.shape;

import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;

import rin.gl.lib3d.Transformation;
import rin.util.Buffer;

public class Sphere extends Shape {
	private static int items = 0;
	
	private ArrayList<Float> sinSlice = new ArrayList<Float>();
	private ArrayList<Float> cosSlice = new ArrayList<Float>();
	private ArrayList<Float> sinStack = new ArrayList<Float>();
	private ArrayList<Float> cosStack = new ArrayList<Float>();
	
	public Sphere( float radius, int slices, int stacks ) { this( radius, slices, stacks, new Transformation(), false ); }
	public Sphere( float radius, int slices, int stacks, boolean wire ) { this( radius, slices, stacks, new Transformation(), wire ); }
	public Sphere( float radius, int slices, int stacks, Transformation t ) { this( radius, slices, stacks, t, false ); }
	public Sphere( float radius, int slices, int stacks, Transformation t, boolean wire ) {
		super( "Sphere-" + Sphere.items++ );
		if( wire )
			this.setRenderMode( GL_LINES );
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> n = new ArrayList<Float>();
		this.computeTables( -slices, stacks * 2 );
		
		float z0 = 1.0f;
		float z1 = cosStack.get( stacks > 0 ? 1 : 0 );
		float r0 = 0.0f;
		float r1 = sinStack.get( stacks > 0 ? 1 : 0 );
		
		/* near end */
		for( int i = slices - 1; i >= 0; i-- ) {
			if( !wire ) {
				v.add( 0.0f );		n.add( 0.0f );
				v.add( 0.0f );		n.add( 0.0f );
				v.add( radius );	n.add( 1.0f );
				
				v.add( cosSlice.get( i ) * r1 * radius );	n.add( cosSlice.get( i ) * r1 );
				v.add( sinSlice.get( i ) * r1 * radius );	n.add( sinSlice.get( i ) * r1 );
				v.add( z1 * radius );						n.add( z1 );
				
				int j = i - 1;
				if( j < 0 )
					j = sinSlice.size() - 1;
				
				v.add( cosSlice.get( j ) * r1 * radius );	n.add( cosSlice.get( j ) * r1 );
				v.add( sinSlice.get( j ) * r1 * radius );	n.add( sinSlice.get( j ) * r1 );
				v.add( z1 * radius );						n.add( z1 );
			} else {
				v.add( 0.0f );
				v.add( 0.0f );
				v.add( radius );
				
				v.add( cosSlice.get( i ) * r1 * radius );
				v.add( sinSlice.get( i ) * r1 * radius );
				v.add( z1 * radius );
				
				v.add( cosSlice.get( i ) * r1 * radius );
				v.add( sinSlice.get( i ) * r1 * radius );
				v.add( z1 * radius );
				
				int j = i - 1;
				if( j < 0 )
					j = sinSlice.size() - 1;
				
				v.add( cosSlice.get( j ) * r1 * radius );
				v.add( sinSlice.get( j ) * r1 * radius );
				v.add( z1 * radius );
				
				v.add( cosSlice.get( j ) * r1 * radius );
				v.add( sinSlice.get( j ) * r1 * radius );
				v.add( z1 * radius );
				
				v.add( 0.0f );
				v.add( 0.0f );
				v.add( radius );
			}
		}
		
		/* between near and far ends */
		for( int i = 1; i < stacks - 1; i++ ) {
			z0 = z1; z1 = cosStack.get( i + 1 );
			r0 = r1; r1 = sinStack.get( i + 1 );
			
			/* strip connected two ends per slice */
			for( int j = 0; j < slices; j++ ) {
				if( !wire ) {
					v.add( cosSlice.get( j ) * r1 * radius );	n.add( cosSlice.get( j ) * r1 );
					v.add( sinSlice.get( j ) * r1 * radius );	n.add( sinSlice.get( j ) * r1 );
					v.add( z1 * radius  );						n.add( z1 );
					
					v.add( cosSlice.get( j ) * r0 * radius );	n.add( cosSlice.get( j ) * r0 );
					v.add( sinSlice.get( j ) * r0 * radius );	n.add( sinSlice.get( j ) * r0 );
					v.add( z0 * radius );						n.add( z0 );
					
					int k = j + 1;
					if( k >= sinSlice.size() )
						k = 0;
					
					v.add( cosSlice.get( k ) * r1 * radius );	n.add( cosSlice.get( k ) * r1 );
					v.add( sinSlice.get( k ) * r1 * radius );	n.add( sinSlice.get( k ) * r1 );
					v.add( z1 * radius  );						n.add( z1 );
					
					v.add( cosSlice.get( k ) * r1 * radius );	n.add( cosSlice.get( k ) * r1 );
					v.add( sinSlice.get( k ) * r1 * radius );	n.add( sinSlice.get( k ) * r1 );
					v.add( z1 * radius  );						n.add( z1 );
					
					v.add( cosSlice.get( j ) * r0 * radius );	n.add( cosSlice.get( j ) * r0 );
					v.add( sinSlice.get( j ) * r0 * radius );	n.add( sinSlice.get( j ) * r0 );
					v.add( z0 * radius );						n.add( z0 );
					
					v.add( cosSlice.get( k ) * r0 * radius );	n.add( cosSlice.get( k ) * r0 );
					v.add( sinSlice.get( k ) * r0 * radius );	n.add( sinSlice.get( k ) * r0 );
					v.add( z0 * radius );						n.add( z0 );
				} else {
					v.add( cosSlice.get( j ) * r1 * radius );
					v.add( sinSlice.get( j ) * r1 * radius );
					v.add( z1 * radius  );
					
					v.add( cosSlice.get( j ) * r0 * radius );
					v.add( sinSlice.get( j ) * r0 * radius );
					v.add( z0 * radius );
					
					v.add( cosSlice.get( j ) * r0 * radius );
					v.add( sinSlice.get( j ) * r0 * radius );
					v.add( z0 * radius );
					
					int k = j + 1;
					if( k >= sinSlice.size() )
						k = 0;
					
					v.add( cosSlice.get( k ) * r1 * radius );
					v.add( sinSlice.get( k ) * r1 * radius );
					v.add( z1 * radius  );
					
					v.add( cosSlice.get( k ) * r1 * radius );
					v.add( sinSlice.get( k ) * r1 * radius );
					v.add( z1 * radius  );
					
					v.add( cosSlice.get( k ) * r1 * radius );
					v.add( sinSlice.get( k ) * r1 * radius );
					v.add( z1 * radius  );
					
					v.add( cosSlice.get( k ) * r1 * radius );
					v.add( sinSlice.get( k ) * r1 * radius );
					v.add( z1 * radius  );
					
					v.add( cosSlice.get( j ) * r0 * radius );
					v.add( sinSlice.get( j ) * r0 * radius );
					v.add( z0 * radius );
					
					v.add( cosSlice.get( j ) * r0 * radius );
					v.add( sinSlice.get( j ) * r0 * radius );
					v.add( z0 * radius );
					
					v.add( cosSlice.get( k ) * r0 * radius );
					v.add( sinSlice.get( k ) * r0 * radius );
					v.add( z0 * radius );
				}
			}
		}
		
		z0 = z1; z1 = cosStack.get( stacks - 1 );
		r0 = r1; r1 = sinStack.get( stacks - 1 );
		
		/* far end */
		for( int i = slices - 1; i >= 0; i-- ) {
			if( !wire ) {
				v.add( 0.0f );		n.add( 0.0f );
				v.add( 0.0f );		n.add( 0.0f );
				v.add( -radius );	n.add( -1.0f );
				
				v.add( cosSlice.get( i ) * r0 * radius );	n.add( cosSlice.get( i ) * r0 );
				v.add( sinSlice.get( i ) * r0 * radius );	n.add( sinSlice.get( i ) * r0 );
				v.add( z0 * radius );						n.add( z0 );
				
				int j = i - 1;
				if( j < 0 )
					j = sinSlice.size() - 1;
				
				v.add( cosSlice.get( j ) * r0 * radius );	n.add( cosSlice.get( j ) * r0 );
				v.add( sinSlice.get( j ) * r0 * radius );	n.add( sinSlice.get( j ) * r0 );
				v.add( z0 * radius );						n.add( z0 );
			} else {
				v.add( 0.0f );
				v.add( 0.0f );
				v.add( -radius );
				
				v.add( cosSlice.get( i ) * r0 * radius );
				v.add( sinSlice.get( i ) * r0 * radius );
				v.add( z0 * radius );
				
				v.add( cosSlice.get( i ) * r0 * radius );
				v.add( sinSlice.get( i ) * r0 * radius );
				v.add( z0 * radius );
				
				int j = i - 1;
				if( j < 0 )
					j = sinSlice.size() - 1;
				
				v.add( cosSlice.get( j ) * r0 * radius );
				v.add( sinSlice.get( j ) * r0 * radius );
				v.add( z0 * radius );
				
				v.add( cosSlice.get( j ) * r0 * radius );
				v.add( sinSlice.get( j ) * r0 * radius );
				v.add( z0 * radius );
				
				v.add( 0.0f );
				v.add( 0.0f );
				v.add( -radius );
			}
		}
		
		this.setPosition( t.getPosition() );
		this.setRotation( t.getRotation() );
		this.setScale( t.getScale() );
		this.build( Buffer.toArrayf( v ), Buffer.toArrayf( n ), new float[0], new float[0] );
		
		v.clear();
		n.clear();
		
		this.sinSlice.clear();
		this.cosSlice.clear();
		this.sinStack.clear();
		this.cosStack.clear();
	}
	
	private void computeTables( int slices, int stacks ) {
		int size = Math.abs( slices );
		float angle = (float)(2 * Math.PI / (( slices == 0 ) ? 1 : slices));
		
		this.sinSlice.add( 0.0f );
		this.cosSlice.add( 1.0f );

		for( int i = 1; i < size; i++ ) {
			this.sinSlice.add( (float)Math.sin( angle * i ) );
			this.cosSlice.add( (float)Math.cos( angle * i ) );
		}
		
		size = Math.abs( stacks );
		angle = (float)(2 * Math.PI / (( stacks == 0 ) ? 1 : stacks));
		
		this.sinStack.add( 0.0f );
		this.cosStack.add( 1.0f );

		for( int i = 1; i < size; i++ ) {
			this.sinStack.add( (float)Math.sin( angle * i ) );
			this.cosStack.add( (float)Math.cos( angle * i ) );
		}
	}
	
	public Sphere destroy() {
		super.destroy();
		return null;
	}
}
