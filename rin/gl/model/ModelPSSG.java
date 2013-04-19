package rin.gl.model;

import java.util.ArrayList;

import rin.engine.Engine;
import rin.engine.util.ArrayUtils;
import rin.gl.TextureManager;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.gl.lib3d.Poly;
import rin.gl.texture.ImportDDS;
import static rin.gui.GUIFactory.*;
import static org.lwjgl.opengl.EXTTextureCompressionS3TC.*;
import rin.util.Buffer;
import rin.util.bio.BIOBuffer;
import rin.util.dcode.dds.DDSFile;
import rin.util.dcode.pssg.PSSGFile;
import rin.util.math.Quat4;

public class ModelPSSG implements Model {

	@Override public Actor fromFile( String file ) {
		System.out.println( file );
		String path = file.substring( 0, file.lastIndexOf( Engine.LS ) + 1 );
		String name = file.substring( file.lastIndexOf( Engine.LS ) + 1 );
		
		Mesh mesh = new Mesh();
		PSSGFile pssg = new PSSGFile( file );
		pssg.read();
		
		/*createWindow( name + "_window" )
				.setTitle( name )
				.add( createContainer( name + "_root" ) )
				.show();
		
		int id = 0;
		PSSGFile.Master master = pssg.getMaster();
		for( PSSGFile.Texture t : master.textures )
			if( t.id.indexOf( "body" ) != -1 && t.id.indexOf("specular") == -1 )
				id = master.textures.indexOf( t );
		
		int format = 0;
		if( pssg.getMaster().textures.get( id ).format.toUpperCase().equals("DXT3") )
			format = GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
		else if( pssg.getMaster().textures.get( id ).format.toUpperCase().equals("DXT1") )
			format = GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
		else
			System.out.println( "FUCK" );
		ImportDDS.loadDDS( pssg.getMaster().textures.get( id ).data, format, pssg.getMaster().textures.get( id ).width,
				pssg.getMaster().textures.get( id ).height, pssg.getMaster().textures.get( id ).id );
		
		System.out.println( id + " " + master.textures.get( id ).id );
		int count = 0;
		for( PSSGFile.Skin skin : master.skin ) {
			if( skin.iindices.length != 0 ) {
				//System.out.println( BIOBuffer.asString( skin.skindices ) );
				//System.out.println( skin.st.length );
				ArrayList<Float> v = new ArrayList<Float>();
				ArrayList<Float> t = new ArrayList<Float>();
				//System.out.println( pssg.getMaster().textures.get( count ).id );
				count++;
				for( int i = 0; i < skin.iindices.length; i++ ) {
					v.add( skin.sv[ skin.iindices[i] * 3 ] );
					v.add( skin.sv[ skin.iindices[i] * 3 + 1 ] );
					v.add( skin.sv[ skin.iindices[i] * 3 + 2 ] );
					
					t.add( skin.st[ skin.iindices[i] * 2 ] );
					t.add( skin.st[ skin.iindices[i] * 2 + 1 ] );
				}
				
				mesh.addPoly( Buffer.toArrayf( v ), new float[0], Buffer.toArrayf( t ), "" );
				final Poly poly = mesh.getPolys().get( mesh.getPolys().size() - 1 );
				
				poly.addTexture( pssg.getMaster().textures.get( id ).id );
				
				getContainer( name + "_root" )
						.add( createCheckBox()
								.onCheck( new CheckBoxEvent() {
									public void run() {
										poly.setVisible( true );
									}
								})
								.onUnCheck( new CheckBoxEvent() {
									public void run() {
										poly.setVisible( false );
									}
								})
								.check()
						);
			}
			if( skin.sindices.length != 0 ) {
				ArrayList<Float> v = new ArrayList<Float>();
				ArrayList<Float> t = new ArrayList<Float>();
				//System.out.println( skin.st.length );
				//System.out.println( skin.sindices.length + " " + skin.st.length + " " + skin.sv.length );
				count++;
				//System.out.println( BIOBuffer.asString( skin.sv ) );
				for( int i = 0; i < skin.sindices.length; i++ ) {
					v.add( skin.sv[ skin.sindices[i] * 3 ] );
					v.add( skin.sv[ skin.sindices[i] * 3 + 1 ] );
					v.add( skin.sv[ skin.sindices[i] * 3 + 2 ] );
					
					t.add( skin.st[ skin.sindices[i] * 2 ] );
					t.add( skin.st[ skin.sindices[i] * 2 + 1 ] );
				}
				
				
				mesh.addPoly( Buffer.toArrayf( v ), new float[0], Buffer.toArrayf( t ), "" );
				final Poly poly = mesh.getPolys().get( mesh.getPolys().size() - 1 );

				poly.addTexture( pssg.getMaster().textures.get( id ).id );
			}
			
		}
		
		getWindow( name+"_window" ).show();
		System.out.println( pssg.getMaster().textures.size() + " " + count );*/
		
		PSSGFile.PSSGData data = pssg.getData();
		for( PSSGFile.PSSGDataSource ds : data.sources ) {
			if( ds.skindices.length != 0 ) {
				System.out.println( ArrayUtils.asString( ds.weights ) );
				System.out.println( ArrayUtils.asString( ds.skindices ) );
				
				float[] v = new float[ds.indices.length*3];
				float[] t = new float[ds.indices.length*2];
				float[] b = new float[ds.indices.length*4];
				
				//System.out.println( ds.skindices.length + " " + ds.indices.length + " " + ds.weights.length );
				for( int i = 0; i < ds.indices.length; i++ ) {
					v[i*3] = ds.v[ ds.indices[i] * 3 ];
					v[i*3+1] = ds.v[ ds.indices[i] * 3 + 1 ];
					v[i*3+2] = ds.v[ ds.indices[i] * 3 + 2 ];

					//System.out.println( "bone: " + ds.skindices[ ds.indices[i] ] );
					
					t[i*2] = ds.t[ ds.indices[i] * 3 ];
					t[i*2+1] = ds.t[ ds.indices[i] * 3 + 1 ];
					
					/*for( int j = 0; j < 3; j++ ) {
						if( ds.weights[ ds.indices[ i*3+j ] ] != 0.0f ) {
							System.out.println( data.skel.skel.getBone( ds.joints.get( ds.skindices[ ds.indices[ i*3+j ] ] ) ) );
						}
					}*/
				}
				
				System.out.println( (v.length/3) + " " + (ds.skindices.length/4) );
				String tex = path + "textures" + Engine.LS + ds.texture.id.substring( 0, ds.texture.id.lastIndexOf( "." ) ) + ".png";
				mesh.addPoly( v, new float[0], t, tex );
			}
		}
		
		mesh.setPosition( 100.0f, 0.0f, 0.0f );
		mesh.setScale( 0.01f, 0.01f, 0.01f );
		mesh.setRotation( 90.0f, 0.0f, 0.0f );
		return mesh;
	}

}
