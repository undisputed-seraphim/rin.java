package rin.engine.resource;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;

public class Directory {

	public static final FileFilter ONLY_FILES = new FileFilter() {
		@Override public boolean accept( File file ) {
			return !file.isDirectory();
		}
	};
	
	private URL path;
	private ResourceIdentifier[] files;
	
	public Directory( URL url ) {
		path = url;
		File[] fs = new File( url.getPath() ).listFiles( ONLY_FILES );
		files = new ResourceIdentifier[fs.length];
		
		for( int i = 0; i < fs.length; i++ )
			try {
				files[i] = new ResourceIdentifier( fs[i].toURI().toURL() );
			} catch( MalformedURLException e ) {
			}
	}
	
	public ResourceIdentifier[] getFiles() {
		return files;
	}
	
	public void print() {
		System.out.println( "Directory: " + path.getPath() );
		for( int i = 0; i < files.length; i++ )
			System.out.println( "Resource: " + files[i].getName() );
	}
	
}
