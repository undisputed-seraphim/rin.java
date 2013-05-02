package rin.engine.resource.model.ism2;

import java.util.ArrayList;

public class Ism2Spec {

	public static final char[] MAGIC = new char[] { 'I', 'S', 'M', '2' };
	
	public static final int C_STRINGS = 0x21;
	public static final int C_TEXTURE_LIST = 0x2E;
	public static final int C_TEXTURE = 0x2D;
	
	public static final int C_11 = 0x0B;
	public static final int C_10 = 0x0A;
	public static final int C_TRIANGLES = 0x46;
	public static final int C_INDICES = 0x45;
	public static final int C_VERTICES = 0x59;
		public static final int T_VERTICES_VERTEX = 1;
			public static final int T_VERTEX_POSITION = 0;
			public static final int T_VERTEX_NORMAL = 2;
			public static final int T_VERTEX_3 = 3;
			public static final int T_VERTEX_14 = 14;
			public static final int T_VERTEX_15 = 15;
		public static final int T_VERTICES_TEXCOORD = 2;
		public static final int T_VERTICES_WEIGHT = 3;
			public static final int T_WEIGHT_BONE = 7;
			public static final int T_WEIGHT_WEIGHT = 1;
	
	public static final int C_3 = 0x03;
	public static final int C_4 = 0x04;
	
	public static final int C_50 = 0x32;
	public static final int C_49 = 0x31;
	public static final int C_48 = 0x30;
	
	public static final int C_ANIMATION = 0x34;
	public static final int C_ANIMATION_FRAME = 0x50;
	public static final int C_FRAME_TRANSFORM = 0x0F;
	
	public static final int C_TRANSFORM_DATA = 0x44;
		public static final int T_TRANSFORM_INDEX = 5;
		public static final int T_TRANSFORM_MATRIX = 12;
		public static final int T_TRANSFORM_FRAME = 18;
	
	public static class Ism2TransformData {
		public String name1;
		public String name2;
		public int type;
		public int count;
		public int stride;
		short[] time;
		float[][] data;
		
		public Ism2TransformData( String n1, String n2 ) {
			name1 = n1;
			name2 = n2;
		}
	}
	
	public static class Ism2KeyFrame {
		public String name;
		ArrayList<Ism2TransformData> transforms = new ArrayList<Ism2TransformData>();
		
		public Ism2KeyFrame( String n ) {
			name = n;
		}
		
		public Ism2TransformData addTransform( String name1, String name2 ) {
			Ism2TransformData transform = new Ism2TransformData( name1, name2 );
			transforms.add( transform );
			return transform;
		}
	}
	
	public static class Ism2Animation {
		public String name;
		ArrayList<Ism2KeyFrame> frames = new ArrayList<Ism2KeyFrame>();
		
		public Ism2Animation( String n ) {
			name = n;
		}
		
		public Ism2KeyFrame addFrame( String name ) {
			Ism2KeyFrame frame = new Ism2KeyFrame( name );
			frames.add( frame );
			return frame;
		}
		
		public void print() {
			System.out.println( "Animation: " + name );
			for( Ism2KeyFrame kf : frames ) {
				System.out.println( "\tFrame: " + kf.name );
				for( Ism2TransformData t : kf.transforms ) {
					System.out.println( "\t\tTransform: " + t.name1 );
					System.out.println( "\t\tTime: " + t.time.length );
					System.out.println( "\t\tData: " + t.data.length );
				}
			}
		}
	}
	
	public static class Ism2Texture {
		public String[] data = new String[7];
	}
	
	public static class Ism2VertexInfo {
		public int type;
		public int count;
		public int vtype;
		public int vsize;
		public int voffset;
		public int offset;
	}
	
	public static class Ism2VertexData {
		public float[] v = new float[0];
		public float[] n = new float[0];
		public float[] t = new float[0];
		
		public float[][] b = new float[0][0];
		public float[][] w = new float[0][0];
		
		public boolean normaled = false;
	}
	
	public static class Ism2Model {
		Ism2Texture[] textures;
		Ism2VertexData[] vdata;
	}
}
