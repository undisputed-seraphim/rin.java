package rin.engine.resource.model;

import rin.engine.resource.image.ImageContainer;

public class Material {

	private ImageContainer texture;
	
	public Material( ImageContainer image ) {
		texture = image;
	}
	
	public ImageContainer getTexture() {
		return texture;
	}
	
}
