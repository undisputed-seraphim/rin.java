package rin.engine.resource;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import rin.engine.Engine;
import rin.engine.resource.formats.pssg.PSSGDecoder;
import rin.engine.util.FileUtils;

public class ResourceManager {

	public static class ResourceNotFoundException extends Error {
		private static final long serialVersionUID = 7L;
		
		public ResourceNotFoundException() {}
	}
	
	public static class DecoderNotFoundException extends Error {
		private static final long serialVersionUID = 7L;
		
		public DecoderNotFoundException() {}
	}
	
	//TODO: add plugin support for model formats, etc
	
	protected static final String FS = Engine.LS;
	
	private static final HashMap<Class<?>, ResourceDecoder> decoders = new HashMap<Class<?>, ResourceDecoder>();
	static {
		//addDecoder( PSSGDecoder.class );
	}
	
	public static <T extends ResourceDecoder> void addDecoder(Class<T> cls ) {
		try {
			ResourceManager.decoders.put( cls, cls.newInstance() );
		} catch( InstantiationException ex ) {
			System.err.println( "[ResourceManager] Could not add format ("+cls.getName()+")" );
		} catch( IllegalAccessException ex ) {
			System.err.println( "[ResourceManager] Could not add format ("+cls.getName()+")" );
		}
	}
	
	public static <T extends ResourceDecoder> T getDecoder( Class<T> cls ) {
		T decoder = cls.cast( ResourceManager.decoders.get( cls ) );
		if( decoder == null )
			throw new DecoderNotFoundException();
		
		return decoder;
	}
	
	public static URL getResourceURL( String path ) {
		URL url = ResourceManager.class.getResource( path );
		if( url == null )
			throw new ResourceNotFoundException();
		
		return url;
	}
	
	public static ResourceIdentifier getResource( String path ) {
		URL url = getResourceURL( path );
		if( url == null )
			throw new ResourceNotFoundException();
		
		return new ResourceIdentifier( url );
	}
	
	private static String constructPath( String folder, String ... resource ) {
		String path = "/" + folder;
		for( String s : resource )
			path = path + "/" + s;
		
		return path;
	}
	
	public static URL getCustomResourceURL( String folder, String ... resource ) {
		String path = constructPath( folder, resource );
		return getResourceURL( path );
	}
	
	public static ResourceIdentifier getCustomResource( String folder, String ... resource ) {
		URL url = getCustomResourceURL( folder, resource );
		if( url == null )
			throw new ResourceNotFoundException();
		
		return new ResourceIdentifier( url );
	}
	
	public static URL getPackResourceURL( String pack, String ... resource ) {
		return getCustomResourceURL( "packs" + "/" + pack, resource );
	}
	
	public static ResourceIdentifier getPackResource( String pack, String ... resource ) {
		return getCustomResource( "packs" + "/" + pack, resource );
	}
	
	public static Directory getDirectory( URL path, String directory ) {
		return new Directory( getResourceURL( path.getPath() + directory ) );
	}
	
	public static Directory getPackDirectory( String pack, String ... directory ) {
		return new Directory( getCustomResourceURL( "packs" + "/" + pack, directory ) );
	}
	
	public static File createPackResource( String pack, String ... resource ) {
		return FileUtils.createFile( getCustomResourceURL( "packs" + FS + pack, resource ).getPath() );
	}
	
	public static Directory createPackDirectory( String pack, String ... directory ) {
		return null;
	}
}
