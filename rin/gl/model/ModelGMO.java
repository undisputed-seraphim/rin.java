package rin.gl.model;

import java.util.ArrayList;
import java.util.Arrays;

import rin.engine.resource.Directory;
import rin.engine.resource.Resource;
import rin.engine.resource.formats.gmo.GMODecoder;
import rin.engine.util.ArrayUtils;
import rin.engine.util.ImageUtils;
import static rin.engine.resource.formats.gmo.GMOSpec.*;
import rin.gl.TextureManager;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.util.Buffer;

public class ModelGMO implements Model {

	@Override
	public Actor fromResource( Resource resource ) {
		GMODecoder gmo = new GMODecoder( resource );
		
		Mesh mesh = new Mesh();
		int count = 0;
		ArrayList<Float> v = new ArrayList<Float>();
		for( SubFile file : gmo.getData().files ) {
			String[] tex = new String[ file.textures.size() ];
			for( int i = 0; i < tex.length; i++ ) {
				Texture t = file.textures.get( i );
				if( TextureManager.load( t.file, t.width, t.height, t.rawData ) != -1 )
					tex[i] = t.file;
				//ImageUtils.test( t.width, t.height, 4, t.rawData );
			}
			
			for( Surface s : file.surfaces ) {
				for( MeshGroup mg : s.groups ) {
					count++;
					if( mg.texture != -1 )
						mesh.addPoly( Buffer.toArrayf( mg.v ), new float[0], Buffer.toArrayf( mg.t ), tex[mg.texture] );
					else mesh.addPoly( Buffer.toArrayf( mg.v ), new float[0], Buffer.toArrayf( mg.t ), new float[0] );
				}
			}
		}
		System.out.println( "COUNT: " + count );
		//mesh.addPoly( Buffer.toArrayf( v ), new float[0], new float[0], new float[0] );
		
		//gmo.destroy();
		return mesh;
	}

}
