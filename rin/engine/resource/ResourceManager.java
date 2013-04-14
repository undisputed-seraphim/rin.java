package rin.engine.resource;

import java.net.URL;
import java.util.HashMap;

import rin.engine.Engine;
import rin.engine.resource.formats.pssg.PSSGDecoder;

public class ResourceManager {

	//TODO: add plugin support for model formats, etc
	
	protected static final String FS = Engine.LS;
	
	private static final HashMap<String, ResourceDecoder> decoders = new HashMap<String, ResourceDecoder>();
	static {
		addDecoder( "PSSG", PSSGDecoder.class );
	}
	
	public static <R extends ResourceDecoder> void addDecoder( String id, Class<R> cls ) {
		try {
			ResourceManager.decoders.put( id.toUpperCase(), cls.newInstance() );
		} catch (InstantiationException e) {
			System.err.println( "[ResourceManager] Could not add format " + id + "("+cls.getName()+")" );
		} catch (IllegalAccessException e) {
			System.err.println( "[ResourceManager] Could not add format " + id + "("+cls.getName()+")" );
		}
	}
	
	public static ResourceDecoder getDecoder( String format ) {
		ResourceDecoder decoder = ResourceManager.decoders.get( format.toUpperCase() );
		if( decoder == null )
			System.err.println( "[ResourceManager] No decoder found for format " + format );
		
		return decoder;
	}
	public static URL getResourceURL( String path ) {
		URL url = ResourceManager.class.getResource( path );
		if( url == null )
			System.err.println( "[ResourceManager] Resource '" + path + "' not found." );
		
		return url;
	}
	
	public static ResourceIdentifier getResource( String path ) {
		URL url = getResourceURL( path );
		if( url == null )
			return null;
		
		return new ResourceIdentifier( url );
	}
	
	private static String constructPath( String folder, String ... resource ) {
		String path = FS + folder;
		for( String s : resource )
			path += FS + s;
		
		return path;
	}
	
	public static URL getCustomResourceURL( String folder, String ... resource ) {
		String path = constructPath( folder, resource );
		return getResourceURL( path );
	}
	
	public static ResourceIdentifier getCustomResource( String folder, String ... resource ) {
		URL url = getCustomResourceURL( folder, resource );
		if( url == null )
			return null;
		
		return new ResourceIdentifier( url );
	}
	
	public static URL getPackResourceURL( String pack, String ... resource ) {
		return getCustomResourceURL( "packs" + FS + pack, resource );
	}
	
	public static ResourceIdentifier getPackResource( String pack, String ... resource ) {
		return getCustomResource( "packs" + FS + pack, resource );
	}
	
}
