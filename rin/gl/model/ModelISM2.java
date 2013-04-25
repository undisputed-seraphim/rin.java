package rin.gl.model;

import java.util.ArrayList;

import rin.engine.Engine;
import rin.engine.resource.Resource;
import rin.engine.resource.formats.ism2.ISM2Decoder;
import rin.engine.resource.formats.ism2.ISM2Spec.ISM2Mesh;
import rin.engine.resource.formats.ism2.ISM2Spec.ISM2SubMesh;
import rin.engine.util.ArrayUtils;
import rin.engine.util.PSSGExtractor;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.gl.lib3d.Poly;
import rin.util.Buffer;

public class ModelISM2 implements Model {

	@Override
	public Actor fromResource( Resource resource ) {
		Mesh mesh = new Mesh();
		ISM2Decoder decoder = new ISM2Decoder( resource );
		/*for( ISM2Mesh m : decoder.getData().meshList )
			for( ISM2SubMesh s : m.children )
				mesh.addPoly( s.v, s.n, s.t, new float[0] );*/
		
		return mesh;		
	}

}
