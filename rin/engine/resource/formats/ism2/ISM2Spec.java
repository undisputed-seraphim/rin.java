package rin.engine.resource.formats.ism2;

import java.util.ArrayList;

public class ISM2Spec {

	// chunks
	public static final int C_PROPERTIES = 0x21;
	public static final int C_TEXTURES = 0x2E;
	public static final int C_VERTEXDATA = 0x0B;
	
	// nodes
	public static final int N_TEXTURE = 0x2D;
	public static final int N_MESH = 0x0A;
	public static final int N_VERTICES = 0x59;
	public static final int N_TRIANGLES = 0x46;
	public static final int N_INDICES = 0x45;
	
	// key identifiers
	public static final int T_VERT_VERTICES = 1;
	public static final int T_VERT_VERTICES_VERTEX = 0;
	public static final int T_VERT_VERTICES_NORMAL = 2;
	public static final int T_VERT_TEXCOORDS = 2;
	public static final int T_VERT_WEIGHTS = 3;
	
	public static final int T_INDEX_INT16 = 5;
	public static final int T_INDEX_INT32 = 7;
	
	public static class VertexType {
		int type;
		int count;
		int vtype;
		int vsize;
		long voffset;
		long offset;
		
		@Override
		public String toString() {
			String res = "Type: " + type + "\n";
			res += "Count: " + count + "\n";
			res += "Vertex Type: " + vtype + "\n";
			res += "Vertex Size: " + vsize + "\n";
			res += "Item Offset: " + voffset + "\n";
			res += "Offset: " + offset;
			return res;
		}
	}
	
	public static class ISM2VertexData {
		public float[] v;
		public float[] n;
		public float[] t;
	}
	
	public static class ISM2IndexData {
		public int[] i;
	}
	
	public static class ISM2Mesh {
		ArrayList<ISM2VertexData> vdata = new ArrayList<ISM2VertexData>();
		ArrayList<ISM2IndexData> idata = new ArrayList<ISM2IndexData>();
	}
	
	public static class ISM2Data {
		protected int meshCount = 0;
		public ISM2Mesh[] meshList;
		public float[] v;
		public float[] n;
		public float[] t;
	}
}
