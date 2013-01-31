package rin.gl.lib3d.shape;

import rin.util.math.Vec3;

public class Grid extends ComplexShape {
	public static final int X_AXIS = 0,
							Y_AXIS = 1,
							Z_AXIS = 2;
	
	private static int items = 0;
	
	public Grid( int rows, int cols, float size ) { this( rows, cols, size, new Vec3( 0.0f, 0.0f, 0.0f ) ); }
	public Grid( int rows, int cols, float size, Vec3 pos ) { this( rows, cols, size, pos, Y_AXIS ); }
	public Grid( int rows, int cols, float size, Vec3 pos, int axis ) { this( rows, cols, size, pos, axis, 0.0f ); }
	public Grid( int rows, int cols, float size, Vec3 pos, int axis, float space ) {
		super( "Grid-" + Grid.items++ );
		super.setInterleaved( false );
		super.bound = false;
		super.setPolyPicking( true );
		
		if( rows == 0 ) rows = 1;
		if( cols == 0 ) cols = 1;
		
		int z = axis;
		int x = axis == X_AXIS ? Y_AXIS : axis == Y_AXIS ? Z_AXIS : X_AXIS;
		int y = x == X_AXIS ? Y_AXIS : x == Y_AXIS ? Z_AXIS : X_AXIS;
		System.out.println( z + " " + x + " " + y );
		
		float topleftx = 0.0f, topleftz = 0.0f;
		if( z == X_AXIS ) {
			topleftx = pos.y - rows * size / 2 - (space * (rows - 1) / 2 );
			topleftz = pos.z - cols * size / 2 - (space * (cols - 1) / 2 );
		} else if( z == Y_AXIS ) {
			topleftx = pos.x - rows * size / 2 - (space * (rows - 1) / 2 );
			topleftz = pos.z - cols * size / 2 - (space * (cols - 1) / 2 );
		} else if( z == Z_AXIS ) {
			topleftx = pos.x - rows * size / 2 - (space * (rows - 1) / 2 );
			topleftz = pos.y - cols * size / 2 - (space * (cols - 1) / 2 );
		}
		
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < cols; j++ ) {
				float[] v = new float[18];
				
				if( z == X_AXIS ) {
					v[x] = topleftx + (j * size) + ( space * j );
					v[y] = topleftz + (i * size) + ( space * i );
					v[z] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+3] = topleftx + (j * size) + size + ( space * j );
					v[y+3] = topleftz + (i * size) + ( space * i );
					v[z+3] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+6] = topleftx + (j * size) + ( space * j );
					v[y+6] = topleftz + (i * size) + size + ( space * i );
					v[z+6] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+9] = topleftx + (j * size) + size + ( space * j );
					v[y+9] = topleftz + (i * size) + ( space * i );
					v[z+9] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+12] = topleftx + (j * size) + ( space * j );
					v[y+12] = topleftz + (i * size) + size + ( space * i );
					v[z+12] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				
					v[x+15] = topleftx + (j * size) + size + ( space * j );
					v[y+15] = topleftz + (i * size) + size + ( space * i );
					v[z+15] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				}
				
				if( z == Y_AXIS ) {
					v[x] = topleftz + (i * size) + ( space * i );
					v[y] = topleftx + (j * size) + ( space * j );
					v[z] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+3] = topleftz + (i * size) + ( space * i );
					v[y+3] = topleftx + (j * size) + size + ( space * j );
					v[z+3] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+6] = topleftz + (i * size) + size + ( space * i );
					v[y+6] = topleftx + (j * size) + ( space * j );
					v[z+6] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+9] = topleftz + (i * size) + ( space * i );
					v[y+9] = topleftx + (j * size) + size + ( space * j );
					v[z+9] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+12] = topleftz + (i * size) + size + ( space * i );
					v[y+12] = topleftx + (j * size) + ( space * j );
					v[z+12] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				
					v[x+15] = topleftz + (i * size) + size + ( space * i );
					v[y+15] = topleftx + (j * size) + size + ( space * j );
					v[z+15] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				}
				
				if( z == Z_AXIS ) {
					v[x] = topleftx + (j * size) + ( space * j );
					v[y] = topleftz + (i * size) + ( space * i );
					v[z] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+3] = topleftx + (j * size) + size + ( space * j );
					v[y+3] = topleftz + (i * size) + ( space * i );
					v[z+3] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+6] = topleftx + (j * size) + ( space * j );
					v[y+6] = topleftz + (i * size) + size + ( space * i );
					v[z+6] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+9] = topleftx + (j * size) + size + ( space * j );
					v[y+9] = topleftz + (i * size) + ( space * i );
					v[z+9] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
					
					v[x+12] = topleftx + (j * size) + ( space * j );
					v[y+12] = topleftz + (i * size) + size + ( space * i );
					v[z+12] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				
					v[x+15] = topleftx + (j * size) + size + ( space * j );
					v[y+15] = topleftz + (i * size) + size + ( space * i );
					v[z+15] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				}
				
				super.addPoly( this.getName() + ":" + "cell-" + i + "," + j, v, new float[0], new float[0], "" );
				super.getPoly( i + j ).setPicking( true );
			}
		}
	}
}
