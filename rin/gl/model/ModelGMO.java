package rin.gl.model;

import rin.engine.resource.Resource;
import rin.engine.resource.formats.gmo.GMODecoder;
import static rin.engine.resource.formats.gmo.GMOSpec.*;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.SkinnedMesh;

public class ModelGMO implements Model {

	@Override
	public Actor fromResource( Resource resource ) {
		GMODecoder gmo = new GMODecoder( resource );
		SkinnedMesh mesh = new SkinnedMesh();
		
		for( SubFile file : gmo.getData().files ) {
			for( Surface s : file.surfaces ) {
				for( Mesh m : s.meshes ) {
					mesh.addPoly( m.v, new float[0], m.t, new float[0] );
					//System.out.println( m.name );
				}
				break;
			}
		}
		return mesh;
	}

}
