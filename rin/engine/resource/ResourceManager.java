package rin.engine.resource;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import rin.engine.Engine;
import rin.engine.resource.formats.pssg.PSSGDecoder;
import rin.engine.util.FileUtils;

public final class ResourceManager {

	private static final File ROOT_FILE = new File( ResourceManager.class.getResource( "/" ).getPath() );
	private static final Directory ROOT_DIR = new Directory( ROOT_FILE );
	private static final Directory PACK_DIR = ROOT_DIR.getDirectory( "packs" );
	
	public static class ResourceNotFoundException extends Error {
		private static final long serialVersionUID = 7L;
		
		public ResourceNotFoundException() {}
	}
	
	public static class ResourceExistsException extends Error {
		private static final long serialVersionUID = 7L;
		
		public ResourceExistsException() {}
	}
	
	public static class DirectoryNotFoundException extends Error {
		private static final long serialVersionUID = 7L;
	}
	
	public static class DecoderNotFoundException extends Error {
		private static final long serialVersionUID = 7L;
		
		public DecoderNotFoundException() {}
	}
	
	//TODO: add plugin support for model formats, etc
	
	protected static final String FS = File.separator;
	
	/* ---------- new and improved logic -------- */
	
	public static Resource getPackResource( String pack, String ... resource ) {
		if( resource.length == 0 )
			throw new IllegalArgumentException( "ResourceManager#getPackResource() requires a resource string." );
		
		return PACK_DIR.getDirectory( pack ).findResource( resource );
	}
	
	public static Directory getPackDirectory( String pack, String ... directory ) {
		Directory res = PACK_DIR.getDirectory( pack );
		for( int i = 0; i < directory.length; i++ )
			res = res.getDirectory( directory[i] );
		
		return res;
	}
}
