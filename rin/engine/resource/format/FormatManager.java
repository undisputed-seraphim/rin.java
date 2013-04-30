package rin.engine.resource.format;

import java.util.HashMap;

import rin.engine.resource.ResourceDecoder;

public class FormatManager {

	HashMap<String, ResourceDecoder> imageDecoders = new HashMap<String, ResourceDecoder>();
	HashMap<String, ResourceDecoder> modelDecoders = new HashMap<String, ResourceDecoder>();
	
	public ResourceDecoder getImageDecoder( String extension ) {
		return null;
	}
	
	HashMap<String, ResourceDecoder> imageEncoders = new HashMap<String, ResourceDecoder>();
	HashMap<String, ResourceDecoder> modelEncoders = new HashMap<String, ResourceDecoder>();
}
