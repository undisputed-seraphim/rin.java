package rin.engine.resource;

import java.io.File;

public class ResourcePointer {

	protected File target;
	
	public ResourcePointer( File file ) {
		target = file;
	}
	
	public File getTarget() {
		return target;
	}
	
	public String getName() {
		return target.getName();
	}
	
	public String getPath() {
		return target.getPath();
	}
	
}
