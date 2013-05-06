package rin.engine.resource;

public interface ResourceExtractor {

	public String getExtensionName();
	public ExtractOptions getDefaultOptions();
	public boolean extract( Resource resource, ExtractOptions opts );
	
}
