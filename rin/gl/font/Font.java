package rin.gl.font;

import java.util.ArrayList;
import java.util.HashMap;

import rin.gl.TextureManager;
import rin.engine.Engine;
import rin.gl.lib3d.Poly;
import rin.util.Buffer;
import rin.util.math.Mat4;
import rin.util.math.Vec3;

public class Font {
	public static class Pair {
		public int row;
		public int col;
		public Pair( int one, int two ) {
			this.row = one;
			this.col = two;
		}
	}
	
	private static HashMap<Character, Pair> map = new HashMap<Character, Pair>();
	static {
		Font.map.put( 'A', new Pair( 0, 0 ) );
		Font.map.put( 'B', new Pair( 0, 1 ) );
		Font.map.put( 'C', new Pair( 0, 2 ) );
		Font.map.put( 'D', new Pair( 0, 3 ) );
		Font.map.put( 'E', new Pair( 0, 4 ) );
		
		Font.map.put( 'F', new Pair( 1, 0 ) );
		Font.map.put( 'G', new Pair( 1, 1 ) );
		Font.map.put( 'H', new Pair( 1, 2 ) );
		Font.map.put( 'I', new Pair( 1, 3 ) );
		Font.map.put( 'J', new Pair( 1, 4 ) );
		
		Font.map.put( 'K', new Pair( 2, 0 ) );
		Font.map.put( 'L', new Pair( 2, 1 ) );
		Font.map.put( 'M', new Pair( 2, 2 ) );
		Font.map.put( 'N', new Pair( 2, 3 ) );
		Font.map.put( 'O', new Pair( 2, 4 ) );
		
		Font.map.put( 'P', new Pair( 3, 0 ) );
		Font.map.put( 'Q', new Pair( 3, 1 ) );
		Font.map.put( 'R', new Pair( 3, 2 ) );
		Font.map.put( 'S', new Pair( 3, 3 ) );
		Font.map.put( 'T', new Pair( 3, 4 ) );
		
		Font.map.put( 'U', new Pair( 4, 0 ) );
		Font.map.put( 'V', new Pair( 4, 1 ) );
		Font.map.put( 'W', new Pair( 4, 2 ) );
		Font.map.put( 'X', new Pair( 4, 3 ) );
		Font.map.put( 'Y', new Pair( 4, 4 ) );
		
		Font.map.put( 'Z', new Pair( 5, 4 ) );
	}

	private int texture = -1;
	private String textureFile = "";
	
	public Font( String file ) {
		this.texture = TextureManager.load( file );
		this.textureFile = file;
	}
	
	public void draw3D( String str, float size, float spacing, Vec3 position ) {
		Poly poly = new Poly( "string" );
		
		float startx = 0.0f;
		float starty = 0.0f;
		float startz = 0.0f;
		
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> t = new ArrayList<Float>();
		
		int j = 0;
		for( int i = 0; i < str.length(); i++ ) {
			Pair cur = Font.map.get( str.toUpperCase().charAt( i ) );
			if( cur != null ) {
				v.add( ( j * spacing ) + startx + (j * size) ); v.add( starty ); v.add( startz );
				t.add( cur.col * 8 / 64.0f ); t.add( (cur.row * 8 + 8) / 64.0f );
				v.add( ( j * spacing ) + startx + size + (j * size) ); v.add( starty ); v.add( startz );
				t.add( (cur.col * 8 + 8) / 64.0f ); t.add( (cur.row * 8 + 8) / 64.0f );
				v.add( ( j * spacing ) + startx + (j * size) ); v.add( starty + size ); v.add( startz );
				t.add( cur.col * 8 / 64.0f ); t.add( cur.row * 8 / 64.0f );
				
				v.add( ( j * spacing ) + startx + (j * size) ); v.add( starty + size ); v.add( startz );
				t.add( cur.col * 8 / 64.0f ); t.add( cur.row * 8 / 64.0f );
				v.add( ( j * spacing ) + startx + size + (j * size) ); v.add( starty ); v.add( startz );
				t.add( (cur.col * 8 + 8) / 64.0f ); t.add( (cur.row * 8 + 8) / 64.0f );
				v.add( ( j * spacing ) + startx + size + (j * size) ); v.add( starty + size ); v.add( startz );
				t.add( (cur.col * 8 + 8) / 64.0f ); t.add( cur.row * 8 / 64.0f );
				j++;
			}
		}
		
		poly.addTexture( this.textureFile );
		poly.setMatrix( Mat4.inverse( Engine.getScene().getCamera().getRotationMatrix() ) );
		poly.build( Buffer.toArrayf( v ), new float[0], Buffer.toArrayf( t ), new float[0] );
		poly.render();
	}
}
