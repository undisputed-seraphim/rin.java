package rin.engine.resource.model.rin3d;

import rin.engine.resource.Resource;
import rin.engine.resource.model.ModelContainer;
import rin.engine.resource.model.ModelEncoder;
import rin.engine.resource.model.ModelOptions;
import rin.engine.util.binary.BinaryWriter;

public class Rin3dEncoder extends BinaryWriter implements ModelEncoder {

	@Override
	public String getExtensionName() { return "rin3d"; }

	@Override
	public boolean encode( ModelContainer from, Resource to, ModelOptions opts ) {
		load( to );
		
		return true;
	}

}
