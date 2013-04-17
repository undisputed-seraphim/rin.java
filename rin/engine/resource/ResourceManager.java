package rin.engine.resource;

import java.net.URL;
import java.util.HashMap;

import rin.engine.Engine;
import rin.engine.resource.formats.pssg.PSSGDecoder;

public class ResourceManager {

	//TODO: add plugin support for model formats, etc
	
	protected static final String FS = Engine.LS;
	
	private static final HashMap<Class<?>, ResourceDecoder> decoders = new HashMap<Class<?>, ResourceDecoder>();
	static {
		addDecoder( PSSGDecoder.class );
	}
	
	public static <T extends ResourceDecoder> void addDecoder(Class<T> cls ) {
		try {
			ResourceManager.decoders.put( cls, cls.newInstance() );
		} catch (InstantiationException e) {
			System.err.println( "[ResourceManager] Could not add format ("+cls.getName()+")" );
		} catch (IllegalAccessException e) {
			System.err.println( "[ResourceManager] Could not add format ("+cls.getName()+")" );
		}
	}
	
	public static <T extends ResourceDecoder> T getDecoder( Class<T> cls ) {
		T decoder = cls.cast( ResourceManager.decoders.get( cls ) );
		if( decoder == null )
			System.err.println( "[ResourceManager] No decoder found for format " + cls );
		
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
