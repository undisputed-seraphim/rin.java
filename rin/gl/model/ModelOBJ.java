package rin.gl.model;

import java.util.ArrayList;

import rin.gl.lib3d.Actor;
import rin.engine.Engine;
import rin.gl.lib3d.Mesh;
import rin.util.Buffer;
import rin.util.IO;

public class ModelOBJ implements Model {
	private static enum  Line {
		MTLLIB,
		USEMTL,
		V,
		VN,
		VT,
		O,
		F;
		
		public static Line get( String id ) {
			for( Line l : Line.values() )
				if( l.toString().toLowerCase().equals( id.toLowerCase() ) )
					return l;
			return null;
		}
	}
	
	@Override public Actor fromFile( String file ) {
		String[] data = IO.file.asArray( file );
		String path = file.substring( 0, file.lastIndexOf( Engine.LS ) + 1 );
		ArrayList<OBJMesh> overall = new ArrayList<OBJMesh>();
		ArrayList<float[]> v = new ArrayList<float[]>();
		ArrayList<float[]> n = new ArrayList<float[]>();
		ArrayList<float[]> t = new ArrayList<float[]>();
		OBJMesh current = new OBJMesh( "default" );
		String[] tmp = new String[3];
		String mtl = "";
		
		for( String s : data ) {
			String fword = s.substring( 0, s.indexOf( " " ) );
			String rest = s.substring( s.indexOf( " " ) + 1 );
			switch( Line.get( fword  ) ) {
			
			// mtl file for texture properties
			case MTLLIB: mtl = rest; break;
			// material for current object
			case USEMTL: current.textureFile = rest; break;
			// vertex
			case V:
				tmp = rest.split( " " );
				v.add( new float[]{ Float.parseFloat( tmp[0] ), Float.parseFloat( tmp[1] ), Float.parseFloat( tmp[2] ) } );
				break;
			// normal
			case VN:
				tmp = rest.split( " " );
				n.add( new float[]{ Float.parseFloat( tmp[0] ), Float.parseFloat( tmp[1] ), Float.parseFloat( tmp[2] ) } );
				break;
			// texture
			case VT:
				tmp = rest.split( " " );
				t.add( new float[]{ Float.parseFloat( tmp[0] ), Float.parseFloat( tmp[1] ) } );
				break;
			// object
			case O:
				overall.add( new OBJMesh( rest ) );
				current = overall.get( overall.size() - 1 );
				break;
			// faces
			case F:
				tmp = rest.split( " " );
				for( int i = 0; i < tmp.length; i++ ) {
					String[] face = tmp[i].split( "/" );
					float[] curv = v.get( Integer.parseInt( face[0] ) - 1 );
					float[] curn = n.get( Integer.parseInt( face[2] ) - 1 );
					float[] curt = t.get( Integer.parseInt( face[1] ) - 1 );
					current.v.add( curv[0] ); current.v.add( curv[1] ); current.v.add( curv[2] );
					current.n.add( curn[0] ); current.n.add( curn[1] ); current.n.add( curn[2] );
					current.t.add( curt[0] ); current.t.add( curt[1] );
				}
				break;
			
			}
		}
		
		Mesh mesh = new Mesh();
		for( OBJMesh o : overall ) {
			//System.out.println( o.name + " has " + o.v.size() + " vertices." );
			mesh.addPoly(	Buffer.toArrayf( o.v ),
							Buffer.toArrayf( o.n ),
							Buffer.toArrayf( o.t ),
							path + "textures" + Engine.LS + o.textureFile + ".png" );
			o = o.destroy();
		}
		
		v.clear();
		n.clear();
		t.clear();
		overall.clear();
		data = null;
		
		return mesh;
	}
	
	public static class OBJMesh {
		protected ArrayList<Float> v = new ArrayList<Float>();
		protected ArrayList<Float> n = new ArrayList<Float>();
		protected ArrayList<Float> t = new ArrayList<Float>();
		protected String name = "";
		protected String textureFile;
		
		public OBJMesh( String name ) {
			this.name = name;
		}
		
		public OBJMesh destroy() {
			this.v.clear();
			this.n.clear();
			this.t.clear();
			return null;
		}
	}

}
