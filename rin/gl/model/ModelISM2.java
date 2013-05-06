package rin.gl.model;

import rin.engine.resource.Resource;
import rin.engine.resource.image.ImageContainer;
import rin.engine.resource.model.Material;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.Surface;
import rin.gl.TextureManager;
import rin.gl.lib3d.Actor;
import rin.gl.lib3d.Mesh;
import rin.gl.lib3d.XModel;

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
		XModel x = new XModel();
		x.build( container.getVertices(), container.getNormals(), container.getTexcoords() );
		for( Surface s : container.getSurfaces() ) {
			if( s.getMaterial() != null ) {
				ImageContainer ic = s.getMaterial().getTexture();
				int tex = -1;
				if( ic != null )
					tex = TextureManager.load( s.getName(), ic.getWidth(), ic.getHeight(), ic.getFormat().getStride(), ic.getData() );
				x.addPoly( s.getIndices(), tex );
			} else x.addPoly( s.getIndices(), -1 );
		}
		return x;
		/*Mesh mesh = new Mesh();
		//ImageContainer tmp = container.getSurfaces().get( 0 ).getMaterial().getTexture();
		for( Surface s : container.getSurfaces() ) {
			Material mat = s.getMaterial();
			if( mat != null ) {
				ImageContainer ic = mat.getTexture();
				if( ic != null ) {
					//TextureManager.load( ic.getName() );
					//mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), ic.getName() );
					if( TextureManager.load( s.getName(), ic.getWidth(), ic.getHeight(), ic.getFormat().getStride(), ic.getData() ) != -1 ) {
						mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), s.getName() );
					} else mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), new float[0] );
				} else mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), new float[0] );
			} else mesh.addPoly( s.getVertices(), s.getNormals(), s.getTexcoords(), new float[0] );
		}
		return mesh;*/
	}

}
