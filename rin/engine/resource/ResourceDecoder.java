package rin.engine.resource;

public interface ResourceDecoder {

	public abstract ResourceContainer decode( Resource resource, ResourceOptions opts );
	
}
