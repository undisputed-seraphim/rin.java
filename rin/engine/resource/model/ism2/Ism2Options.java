package rin.engine.resource.model.ism2;

import rin.engine.resource.model.ModelOptions;

public class Ism2Options extends ModelOptions {

	private boolean hasAnimations = false;
	
	public Ism2Options setAnimated( boolean animated ) {
		hasAnimations = animated;
		return this;
	}
	
	public boolean isAnimated() {
		return hasAnimations;
	}
	
	private String animationDir = "";
	
	public String getAnimationDirectory() {
		return animationDir;
	}
	
	public Ism2Options setAnimationDirectory( String dir ) {
		animationDir = dir;
		return this;
	}
	
}
