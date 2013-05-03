package rin.engine.resource.model.pssg;

import java.nio.ByteBuffer;

import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelDecoder;
import rin.engine.resource.model.ModelOptions;
import rin.util.bio.BaseBinaryReader;
import rin.util.bio.BinaryReader;

public class PssgDecoder extends BaseBinaryReader implements ModelDecoder {
	
	@Override
	public String getExtensionName() { return "pssg"; }
	
	@Override
	public ModelContainer decode( Resource resource, ModelOptions opts ) {
		load( resource );
		
		ModelContainer mc = new ModelContainer();
		return mc;
	}

	@Override
	public void clear() {
		//TODO: clear all data used by this decoder
	}
}