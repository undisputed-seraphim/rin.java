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
		super.setPolyPickable( true );
		
		int z = axis;
		int x = axis == X_AXIS ? Y_AXIS : axis == Y_AXIS ? Z_AXIS : X_AXIS;
		int y = x == X_AXIS ? Y_AXIS : x == Y_AXIS ? Z_AXIS : X_AXIS;
		System.out.println( z + " " + x + " " + y );
		
		float topleftx = pos.x - rows * size / 2;
		float topleftz = pos.z - cols * size / 2;
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < cols; j++ ) {
				float[] v = new float[18];
				/*v[0] = topleftx + (j * size);
				v[1] = pos.y;
				v[2] = topleftz + (i * size);
				
				v[3] = topleftx + (j * size) + size;
				v[4] = pos.y;
				v[5] = topleftz + (i * size);
				
				v[6] = topleftx + (j * size);
				v[7] = pos.y;
				v[8] = topleftz + (i * size) + size;*/
				
				v[x] = topleftx + (j * size) + ( space * j );
				v[y] = topleftz + (i * size) + ( space * j );
				v[z] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				
				v[x+3] = topleftx + (j * size) + size + ( space * j );
				v[y+3] = topleftz + (i * size) + ( space * j );
				v[z+3] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				
				v[x+6] = topleftx + (j * size) + ( space * j );
				v[y+6] = topleftz + (i * size) + size + ( space * j );
				v[z+6] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				
				v[x+9] = topleftx + (j * size) + size + ( space * j );
				v[y+9] = topleftz + (i * size) + ( space * j );
				v[z+9] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				
				v[x+12] = topleftx + (j * size) + ( space * j );
				v[y+12] = topleftz + (i * size) + size + ( space * j );
				v[z+12] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				
				v[x+15] = topleftx + (j * size) + size + ( space * j );
				v[y+15] = topleftz + (i * size) + size + ( space * j );
				v[z+15] = z == X_AXIS ? pos.x : z == Y_AXIS ? pos.y : pos.z;
				super.addPoly( "row-" + i + "-" + j, v, new float[0], new float[0], "" );
				super.getPoly( i + j ).setPickable( true );
			}
		}
		//super.addPoly( "row", v, n, t, texture)
		
		//super.init();
	}
}
