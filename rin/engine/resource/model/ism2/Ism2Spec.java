package rin.engine.resource.model.ism2;

import java.util.ArrayList;
import java.util.HashMap;

public class Ism2Spec {

	public static final int tmp = 0x14;
	
	public static final char[] MAGIC = new char[] { 'I', 'S', 'M', '2' };
	
	public static final int C_STRINGS = 0x21;
	public static final int C_TEXTURELIST = 0x2E;
	public static final int C_TEXTURE = 0x2D;
	
	public static final int C_SAMPLERLIST = 0x61;
	public static final int C_SAMPLER = 0x0D;
	public static final int C_SAMPLELIST = 0x6C;
	public static final int C_SAMPLE = 0x6A;
	public static final int C_SAMPLEPROPERTY = 0x6B;
	
	public static final int C_FXLIST = 0x62;
	public static final int C_FX = 0x63;
	public static final int C_FXPROPERTY = 0x65;
	
	public static final int C_MESHLIST = 0x0B;
	public static final int C_MESH = 0x0A;
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
	public static final int C_BOUNDINGBOX = 0x6E;
	
	public static final int C_3 = 0x03;
	public static final int C_4 = 0x04;
	public static final int C_5 = 0x05;
	public static final int C_91 = 0x5B;
	public static final int C_92 = 0x5C;
	public static final int C_20 = 0x14;
	public static final int C_114 = 0x72;
	public static final int C_115 = 0x73;
	public static final int C_116 = 0x74;
	public static final int C_117 = 0x75;
	public static final int C_118 = 0x76;
	public static final int C_119 = 0x77;
	public static final int C_MATERIALLIST = 0x4C;
	public static final int C_MATERIAL = 0x4B;
	
	public static final int C_SKINLIST = 0x32;
	public static final int C_SKIN = 0x31;
	public static final int C_SKELETON = 0x30;
	
	public static final int C_ANIMATION = 0x34;
	public static final int C_ANIMATIONFRAME = 0x50;
	public static final int C_FRAMETRANSFORM = 0x0F;
	
	public static final int C_TRANSFORMDATA = 0x44;
		public static final int T_TRANSFORM_BONE = 5;
		public static final int T_TRANSFORM_MATRIX = 12;
		public static final int T_TRANSFORM_FRAME = 18;
	
	public static class Ism2TransformData {
		public String mesh;
		public int type;
		public int count;
		public int stride;
		float[] time;
		float[][] data;
		
		public Ism2TransformData( String m ) {
			mesh = m;
		}
	}
	
	public static class Ism2KeyFrame {
		public String name;
		ArrayList<Ism2TransformData> transforms = new ArrayList<Ism2TransformData>();
		
		public Ism2KeyFrame( String n ) {
			name = n;
		}
		
		public Ism2TransformData addTransform( String mesh ) {
			Ism2TransformData transform = new Ism2TransformData( mesh );
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
					System.out.println( "\t\tTransform: " + t.mesh );
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
		
		@Override public String toString() {
			String res = " type ["+type+"]: ";
			switch( type ) {
			case T_VERTEX_POSITION: res += "position, "; break;
			case T_VERTEX_NORMAL: res += "normal, "; break;
			case T_VERTEX_3: res += "v3, "; break;
			case T_VERTEX_14: res += "v14, "; break;
			case T_VERTEX_15: res += "v15, "; break;
			case T_WEIGHT_BONE: res += "bone, "; break;
			case T_WEIGHT_WEIGHT: res += "weight, "; break;
			default: res += "Unknown!, "; break;
			}
			res += "count: " + count + ", ";
			res += "vtype: " + vtype + ", ";
			res += "vsize: " + vsize + ", ";
			res += "voffset: " + voffset + ", ";
			res += "offset: " + offset;
			return res;
		}
	}
	
	public static class Ism2VertexData {
		public float[] v = new float[0];
		public float[] n = new float[0];
		public float[] t = new float[0];
		
		public float[] b = new float[0];
		public float[] w = new float[0];
		
		public boolean normaled = false;
	}
	
	public static class Ism2Mesh {
		public String name;
		
		public float[] v = new float[0];
		public float[] n = new float[0];
		public float[] t = new float[0];
		
		public float[] b = new float[0];
		public float[] w = new float[0];
		
		public Ism2Mesh( String n ) {
			name = n;
		}
	}
	
	public static class Ism2Model {
		public Ism2Texture[] textures;
		public Ism2VertexData[] vdata;
		public HashMap<String, String> materialToSampler = new HashMap<String, String>();
		public HashMap<String, String> samplerToTexture = new HashMap<String, String>();
		public HashMap<String, String> textureMap = new HashMap<String, String>();
		public ArrayList<Ism2Mesh> meshes = new ArrayList<Ism2Mesh>();
	}
}
