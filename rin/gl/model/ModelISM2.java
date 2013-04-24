package rin.gl.model;

import java.util.ArrayList;

import rin.engine.Engine;
import rin.engine.resource.formats.ism2.ISM2Decoder;
import rin.engine.util.ArrayUtils;
import rin.engine.util.PSSGExtractor;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.gl.lib3d.Poly;
import rin.util.Buffer;

public class ModelISM2 implements Model {

	@Override
	public Actor fromFile( String file ) {
		Mesh mesh = new Mesh();
		ISM2Decoder decoder = new ISM2Decoder( file );
		System.out.println( "**** " + decoder.getData().meshList.length );
		mesh.addPoly( decoder.getData().v, new float[0], new float[0], new float[0] );
		return mesh;		
	}

}
