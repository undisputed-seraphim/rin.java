package rin.engine.resource.model.gmo;

import java.util.ArrayList;

public class GmoSpec {

	public static final char[] MAGIC = new char[] {
		'O', 'M', 'G', '.', '0', '0', '.', '1', 'P', 'S', 'P', '\0', '\0', '\0', '\0', '\0'
	};
	
	private static int b( String ... bytes ) {
		String res = "";
		for( String s : bytes )
			res += s;
		return Integer.parseInt( res, 2 );
	}
	
	public static final int TEXEL_INDEX = 0;
	public static final int COLOR_INDEX = 2;
	public static final int NORMAL_INDEX = 5;
	public static final int VERTEX_INDEX = 7;
	public static final int WEIGHT_INDEX = 9;
	public static final int INDEX_INDEX = 11;
	public static final int NUMWEIGHTS_INDEX = 14;
	public static final int NUMVERTS_INDEX = 18;
	public static final int BYPASS_INDEX = 23;
	
	public static final int MASK_1 = b( "1" );
	public static final int MASK_2 = b( "11" );
	public static final int MASK_3 = b( "111" );
	
	public static final int BMASK_0 = b( "0" );
	public static final int BMASK_1 = b( "1" );
	
	public static final int BMASK_b00 = b( "00" );
	public static final int BMASK_b01 = b( "01" );
	public static final int BMASK_b10 = b( "10" );
	public static final int BMASK_b11 = b( "11" );
	
	public static final int BMASK_000 = b( "000" );
	public static final int BMASK_001 = b( "001" );
	public static final int BMASK_010 = b( "010" );
	public static final int BMASK_011 = b( "011" );
	public static final int BMASK_100 = b( "100" );
	public static final int BMASK_101 = b( "101" );
	public static final int BMASK_110 = b( "110" );
	public static final int BMASK_111 = b( "111" );
	
	public static final int C_ROOT = 0x0002;
	public static final int C_SUB = 0x0003;
		public static final int C_WAT = 0x8042;
	public static final int C_BIAS = 0x8015;
		public static final int T_BIAS_VERTEX = 384;
		public static final int T_BIAS_TEXTURE = 3;
		
	public static final int C_BONE = 0x0004;
		public static final int C_BONE_PARENT = 0x8041;
		public static final int C_BONE_REF = 0x8044;
		public static final int C_BONE_MATRIX = 0x8045;
		public static final int C_BONE_TRANS = 0x8048;
		public static final int C_BONE_QUAT = 0x804B;
		public static final int C_BONE_SURFACE = 0x804E;
		
	public static final int C_SURFACE = 0x0005;
		public static final int C_MESH = 0x0006;
			public static final int C_MESH_MATERIAL = 0x8061;
			public static final int C_MESH_WEIGHTS = 0x8062;
			public static final int C_MESH_INDICES = 0x8066;
				public static final int T_MESH_INDEXED = 0; 
				public static final int T_MESH_TRI = 3;
				public static final int T_MESH_QUAD = 4;
			public static final int C_MESH_WAT = 0x80F1;
		public static final int C_VERTICES = 0x0007;
	
	public static final int C_MATERIAL = 0x0008;
		public static final int C_TEXTURE_LAYER = 0x0009;
		public static final int C_TEXTURE_REF = 0x8091;
	public static final int C_TEXTURE = 0x000A;
		public static final int C_TEXTURE_FILE = 0x8012;
		public static final int C_TEXTURE_DATA = 0x8013;
			public static final int T_TEXTURE_GIM_IMG = 0x04;
			public static final int T_TEXTURE_GIM_PAL = 0x05;
				public static final int T_GIM_PAL_RGBA8888 = 0x03;
	
	public static final int C_ANIMATION = 0x000B;
		public static final int C_ANIMATION_TIMES = 0x80B1;
		public static final int C_ANIMATION_T1 = 0x80B2;
		public static final int C_ANIMATION_LINKS = 0x80B3;
			public static final int T_ANIMATION_TRANS = 0x4B;
			public static final int T_ANIMATION_MATRIX = 0x47;
			public static final int T_ANIMATION_QUAT = 0x48;
		public static final int C_ANIMATION_T2 = 0x80B4;
		public static final int C_ANIMATION_FRAME = 0x000C;
	public static final int C_TEXTURE_ANIMATION = 0x000F;
		
	public static class VertexBias {
		public float bx, by, bz;
		public float sx, sy, sz;
	}
	
	public static class TextureBias {
		public float bu, bv;
		public float su, sv;
	}
	
	public static float applyBias( float t, float scale, float bias ) {
		return t = ( t * scale * 2.0f ) + bias;
	}
	
	public static class Bone {
		public String name;
		public int parent;
		public int surface;
		float[] tran;
		float[] quat;
		
		public Bone( String n ) {
			name = n;
		}
	}
	
	public static class Skeleton {
		public ArrayList<Bone> bones = new ArrayList<Bone>();
		public int[] refs;
		public float[][] mats;
	}
	
	public static class Mesh {
		public String name;
		public int material;
		public int vertices;
		public int type;
		public int indexed;
		public int[] i;
		public int stride;
		public int count;
		
		public int[] weighted = new int[0];
		
		public float[] v = new float[0];
		public float[] n = new float[0];
		public float[] t = new float[0];
		
		public Mesh( String n ) {
			name = n;
		}
	}
	
	public static class Vertices {
		public String name;
		public int texelFormat = 0;
		public int colorFormat = 0;
		public int normalFormat = 0;
		public int vertexFormat = 0;
		public int weightFormat = 0;
		public int indexFormat = 0;
		public int weightCount = 0;
		public int morphCount = 0;
		public int bypass = 0;
		public int count = 0;
		
		public float[] v;
		public float[] n;
		public float[] t;
		
		public int getStride() {
			int stride = 0;
			
			stride += texelFormat;
			stride += colorFormat;
			stride += normalFormat;
			stride += vertexFormat;
			stride += weightFormat;
			stride += indexFormat;
			stride += weightCount;
			stride += morphCount;
			
			if( stride % 2 != 0 )
				stride++;
			
			return stride;
		}
		
		public Vertices( String n ) {
			name = n;
		}
	}
	
	public static class MeshGroup {
		public int texture;
		public ArrayList<Float> v = new ArrayList<Float>();
		public ArrayList<Float> t = new ArrayList<Float>();
		
		public MeshGroup( int tex ) {
			texture = tex;
		}
	}
	
	public static class Surface {
		public String name;
		public ArrayList<Mesh> meshes = new ArrayList<Mesh>();
		public ArrayList<MeshGroup> groups = new ArrayList<MeshGroup>();
		public ArrayList<Vertices> vertices = new ArrayList<Vertices>();
		
		public MeshGroup getGroup( int texture ) {
			for( MeshGroup mg : groups )
				if( mg.texture == texture )
					return mg;
			
			groups.add( new MeshGroup( texture ) );
			return groups.get( groups.size() - 1 );
		}
		
		public Surface( String n ) {
			name = n;
		}
	}
	
	public static class Material {
		public String name;
		public int texture = -1;
		
		public Material( String n ) {
			name = n;
		}
	}
	
	public static class Texture {
		public String name;
		public String file;
		public int width = 0;
		public int height = 0;
		public short[] rawData;
		
		public Texture( String n ) {
			name = n;
		}
	}
	public static class KeyFrame {
		public float time;
		public float[] data;
	}
	
	public static class FrameSet {
		public String name;
		public int type;
		public int count;
		public int stride;
		
		public FrameSet( String n ) {
			name = n;
		}
	}
	
	public static class AnimationLink {
		public int bone;
		public int type;
		public int frame;
	}
	
	public static class Animation {
		public String name;
		public float start;
		public float finish;
		public float unknown1;
		public int unknown2;
		public ArrayList<AnimationLink> links = new ArrayList<AnimationLink>();
		public ArrayList<FrameSet> frames = new ArrayList<FrameSet>();
		
		public Animation( String n ) {
			name = n;
		}
		
		public void print() {
			System.out.println( "Animation: " + name );
			System.out.println( "Links: " + links.size() );
			for( AnimationLink link : links )
				System.out.println( "\tbone: " + link.bone + " type: " + link.type + " frame: " + link.frame );
			System.out.println( "frames: " + frames.size() );
		}
	}
	
	public static class SubFile {
		public String name;
		public Skeleton skeleton = new Skeleton();
		public VertexBias vbias;
		public TextureBias tbias;
		public ArrayList<Animation> anims = new ArrayList<Animation>();
		public ArrayList<Surface> surfaces = new ArrayList<Surface>();
		public ArrayList<Texture> textures = new ArrayList<Texture>();
		public ArrayList<Material> materials = new ArrayList<Material>();
		
		public SubFile( String n ) {
			name = n;
		}
	}
	
	public static class GMO {
		public String name;
		public ArrayList<SubFile> files = new ArrayList<SubFile>();
		
		public GMO( String n ) {
			name = n;
		}
	}
}

