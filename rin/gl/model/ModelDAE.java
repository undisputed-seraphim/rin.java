package rin.gl.model;

import java.util.ArrayList;

import rin.gl.lib3d.Actor;
import rin.engine.Engine;
import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.gl.lib3d.Mesh;
import rin.util.Buffer;
import rin.util.IO;
import rin.util.XML;

public class ModelDAE implements Model {
	@Override public Actor fromResource( Resource resource ) {
		ArrayList<Polylist> polylists = new ArrayList<Polylist>();
		
		/* create an XML object using file contents */
		XML xml = new XML( IO.file.asString( resource.getTarget().getPath() ) );
		//xml.printTree();
		
		/* initialize sources and add any vertices as additional sources */
		Sources sources = new Sources( xml.getTagsByName( "source" ) );
		sources.addVertices( xml.getTagsByName( "vertices" ) );
		
		XML.Tag[] polys = xml.getTagsByName( "polylist" );
		for( XML.Tag t : polys )
			polylists.add( new Polylist( t ) );
		
		//TODO: everything below this line needs tidying up... badly
		/* DAE models will utilize the mesh class */
		Mesh mesh = new Mesh( resource.getBaseName() );
		mesh.setPicking( true );
		//mesh.setInterleaved( false );
		float[] V_SRC, N_SRC, T_SRC;
		String path = resource.getDirectory().getPath();
		ArrayList<Float> v = new ArrayList<Float>();
		ArrayList<Float> n = new ArrayList<Float>();
		ArrayList<Float> t = new ArrayList<Float>();
		for( Polylist p : polylists ) {
			int[] prim = Buffer.toArrayi( p.getPrim() );
			
			/* sources */
			V_SRC = sources.getSource( p.getSource( "vertex" ) ).getFloatArray();
			N_SRC = sources.getSource( p.getSource( "normal" ) ).getFloatArray();
			T_SRC = sources.getSource( p.getSource( "texcoord" ) ).getFloatArray();
			
			/* stride is equal the amount of items being used (e.g. normals + vertices = 2, vertices only = 1) */
			//TODO: needs to work with any stride instead of assuming it's 3
			int stride = (V_SRC.length > 0 ? 1 : 0) + (N_SRC.length > 0 ? 1 : 0) + (T_SRC.length > 0 ? 1 : 0);

			mesh.addPoly( Buffer.getIndexedValues( V_SRC, prim, p.getOffset( "vertex" ), stride, 3 ),
						  Buffer.getIndexedValues( N_SRC, prim, p.getOffset( "normal" ), stride, 3 ),
						  Buffer.getIndexedValues( T_SRC, prim, p.getOffset( "texcoord" ), stride, 2 ),
						  path + "textures" + Engine.LS + p.getName() + ".png" );
			
			v.addAll( Buffer.getIndexedValuesAL( V_SRC, prim, p.getOffset( "vertex" ), stride, 3 ) );
			n.addAll( Buffer.getIndexedValuesAL( N_SRC, prim, p.getOffset( "normal" ), stride, 3 ) );
			t.addAll( Buffer.getIndexedValuesAL( T_SRC, prim, p.getOffset( "texcoord" ), stride, 2 ) );
			
			/*mesh.addPoly( p.getName(),
						  Buffer.getIndexedValues( V_SRC, Buffer.toArrayi( prim ), p.getOffset( "vertex" ), stride, 3 ),
						  Buffer.getIndexedValues( N_SRC, Buffer.toArrayi( prim ), p.getOffset( "normal" ), stride, 3 ),
						  Buffer.getIndexedValues( T_SRC, Buffer.toArrayi( prim ), p.getOffset( "texcoord" ), stride, 2 ),
						  path + "textures" + Scene.LS + p.getName() + ".png" );*/
			/*Poly poly = new Poly( p.getName() );
			poly.build( Buffer.getIndexedValues( V_SRC, Buffer.toArrayi( prim ), p.getOffset( "vertex" ), stride, 3 ),
						Buffer.getIndexedValues( N_SRC, Buffer.toArrayi( prim ), p.getOffset( "normal" ), stride, 3 ),
						Buffer.getIndexedValues( T_SRC, Buffer.toArrayi( prim ), p.getOffset( "texcoord" ), stride, 2 ),
						path + "textures" + Scene.LS + p.getName() + ".png" );*/
			//mesh.getPolys().add( poly );
			/*poly.setControlled( true );
			poly.setPicking( true );
			Engine.getScene().addActor( poly );*/
			//poly = poly.destroy();
		}

		//mesh.setPicking( true );
		//mesh.setPolyInfo( ranges, unique );
		mesh.build( Buffer.toArrayf( v ), Buffer.toArrayf( n ), Buffer.toArrayf( t ), new float[0] );
		V_SRC = N_SRC = T_SRC = null;
		
		xml = xml.destroy();
		sources = null;
		polys = null;
		
		return mesh;
	}

	@Override
	public Actor fromContainer(ModelContainer container) {
		// TODO Auto-generated method stub
		return null;
	}
}

class Polylist {
	private final int 	TYPE	= 0,
						SOURCE	= 1,
						OFFSET	= 2;
	private ArrayList<String[]> inputs = new ArrayList<String[]>();
	private String name = "";
	private ArrayList<Integer>	prim = new ArrayList<Integer>(),
								vcount = new ArrayList<Integer>();
	
	public Polylist( XML.Tag t ) {
		this.name = t.getAttribute( "material" ).getValue();
		String content = "";
		
		XML.Tag[] inputs = t.getChildren( "input" );
		if( inputs.length > 0 ) {
			for( XML.Tag tag : inputs )
				this.inputs.add( new String[]{
						tag.getAttribute( "semantic" ).getValue(),
						tag.getAttribute( "source").getValue().substring(1),
						tag.getAttribute( "offset" ).getValue() } );
			
			XML.Tag p = t.getChild( "p" );
			if( p != null ) {
				content = p.getContent();
				for( String s : content.split( " " ) )
					this.prim.add( Integer.parseInt( s ) );
			}
			
			XML.Tag vcount = t.getChild( "vcount" );
			if( vcount != null ) {
				content = vcount.getContent();
				for( String s : content.split( " " ) )
					this.vcount.add( Integer.parseInt( s ) );
			}
		}
	}
	
	public String getName() { return this.name; }
	public ArrayList<Integer> getPrim() { return this.prim; }
	public ArrayList<Integer> getVcount() { return this.vcount; }
	public int getOffset( String type ) {
		for( String[] s : this.inputs )
			if( s[this.TYPE].equals( type ) )
				return Integer.parseInt( s[this.OFFSET] );
		return 0;
	}
	public String getSource( String type ) {
		for( String[] s : this.inputs )
			if( s[this.TYPE].equals( type ) )
				return s[this.SOURCE];
		return "";
	}
	public ArrayList<String[]> getInputs() { return this.inputs; }
	
	public void debug() {
		String str = "Polylist[ "+this.name+" ]\n";
		str += "|---offsets:\n";
		for( String[] s : this.inputs )
			str += "|------" + s[this.TYPE] + " " + s[this.SOURCE] + " " + s[this.OFFSET] + "\n";
		System.out.println( str );
	}
}

class Sources {
	ArrayList<Source> sources = new ArrayList<Source>();
	
	public Sources( XML.Tag[] tags ) {
		for( XML.Tag t : tags )
			sources.add( new Source( t ) );
	}
	
	public Source getSource( String name ) { for( Source s : this.sources ) if( s.getName().equals(name) ) return s; return null; }
	public void addVertices( XML.Tag[] tags ) {
		for( XML.Tag t : tags ) {
			Source tmp = new Source( this.getSource( t.getChild( "input" ).getAttribute( "source" ).getValue().substring(1) ) );
			tmp.setName( t.getAttribute( "id" ).getValue() );
			this.sources.add( tmp );
		}
	}
}

class Source {
	private int stride = 0, count = 0;
	private String name = "", type = "";
	
	private float[] f_array = new float[0], m_array = new float[0];
	private String[] n_array = new String[0];
	
	public Source( Source src ) {
		if( src == null )
			return;
		this.name = src.getName();
		this.stride = src.getStride();
		this.count = src.getCount();
		this.type = src.getType();
		this.f_array = src.getFloatArray();
		this.m_array = src.getMatrixArray();
		this.n_array = src.getNameArray();
	}
	
	public Source( XML.Tag t ) {
		this.name = t.getAttribute( "id" ).getValue();
		XML.Tag accessor = t.getChild( "accessor" );
		if( accessor != null ) {
			XML.Attribute stride = accessor.getAttribute( "stride" );
			if( stride != null )
				this.stride = Integer.parseInt( stride.getValue() );
			XML.Attribute count = accessor.getAttribute( "count" );
			if( count != null )
				this.count = Integer.parseInt( count.getValue() );
		}
		
		XML.Tag param = t.getChild( "param" );
		if( param != null ) {
			XML.Attribute type = param.getAttribute( "type" );
			if( type != null )
				this.type = type.getValue();
		}
		
		this.fillData( t, type );
	}
	
	public String getName() { return this.name; }
	public String getType() { return this.type; }
	public int getCount() { return this.count; }
	public int getStride() { return this.stride; }
	public float[] getFloatArray() { return this.f_array; }
	public float[] getMatrixArray() { return this.m_array; }
	public String[] getNameArray() { return this.n_array; }
	
	public void setName( String name ) { this.name = name; }
	public void fillData( XML.Tag t, String type ) {
		String content = "";
		int count = 0;
		if( type.equals( "float" ) ) {
			content = t.getChild( "float_array" ).getContent();
			this.f_array = new float[this.count * this.stride];
			for( String s : content.split( " " ) )
				this.f_array[count++] = Float.parseFloat( s );
		} else if( type.equals( "float4x4" ) ) {
			content = t.getChild( "float_array" ).getContent();
			this.m_array = new float[this.count * this.stride];
			for( String s : content.split( " " ) )
				this.m_array[count++] = Float.parseFloat( s );
		} else if( type.equals( "name" ) ) {
			content = t.getChild( "name_array" ).getContent();
			this.n_array = new String[this.count * this.stride];
			for( String s : content.split( " " ) )
				this.n_array[count++] = s;
		}
	}
}
