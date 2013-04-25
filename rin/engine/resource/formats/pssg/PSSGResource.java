package rin.engine.resource.formats.pssg;

import java.util.HashMap;

import rin.engine.resource.Resource;
import rin.engine.resource.ResourceIdentifier;
import rin.engine.resource.formats.pssg.PSSGDecoder.ActualAnimation;

public class PSSGResource {

	private ResourceIdentifier id;
	private HashMap<String, ActualAnimation> animMap = new HashMap<String, ActualAnimation>();
	
	public PSSGResource( ResourceIdentifier id ) {
		this.id = id;
	}
	
	public HashMap<String, ActualAnimation> getAnimationMap() {
		return animMap;
	}
	
	public ActualAnimation getAnimation( String name ) {
		return animMap.get( name );
	}
	
	//@Override
	public ResourceIdentifier getIdentifier() { return this.id; }
	
}
