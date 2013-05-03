package rin.gl.model;

import rin.engine.resource.Resource;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.model.Material;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.Surface;
import rin.gl.TextureManager;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;

public class ModelISM2 implements Model {

	@Override
	public Actor fromResource( Resource resource ) {
		Mesh mesh = new Mesh();
		/*for( ISM2Mesh m : decoder.getData().meshList )
			for( ISM2SubMesh s : m.children )
				mesh.addPoly( s.v, s.n, s.t, new float[0] );*/
		
		return mesh;		
	}

	@Override
	public Actor fromContainer( ModelContainer container ) {
		Mesh mesh = new Mesh();
		ImageContainer tmp = container.getSurfaces().get( 0 ).getMaterial().getTexture();
		for( Surface s : container.getSurfaces() ) {
			Material mat = s.getMaterial();
			if( mat != null ) {
				ImageContainer ic = mat.getTexture();
				if( ic != null ) {
					System.out.println( ic.getData().length );
					if( TextureManager.load( s.getName(), tmp.getWidth(), tmp.getHeight(), tmp.getFormat().getStride(), tmp.getData() ) != -1 ) {
						mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), s.getName() );
					} else mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), new float[0] );
				} else mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), new float[0] );
			} else mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), new float[0] );
		}
		return mesh;
	}

}
