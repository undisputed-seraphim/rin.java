package rin.engine.resource;

import java.io.File;
import java.net.URL;

import rin.engine.util.FileUtils;

public class ResourceIdentifier {
	
	private URL url;
	private String id;
	private String name;
	private String ext;
	
	public ResourceIdentifier( URL url ) {
		this.url = url;
		this.id = url.getPath().toLowerCase();
		this.name = url.getPath().substring( url.getPath().lastIndexOf( ResourceManager.FS ) + 1 );
		if( this.name.indexOf( "." ) != -1 )
			this.ext = this.name.substring( this.name.lastIndexOf( "." ) + 1 );
	}
	
	public URL getURL() { return this.url; }
	public String getId() { return this.id; }
	public String getName() { return this.name; }
	public String getExt() { return this.ext; }
	
	public File asFile() {
		return new File( this.url.getPath() );
	}
	
	public byte[] asByteArray() {
		return FileUtils.asByteArray( this.url.getPath() );
	}
	
	public void decodeAs( String format ) {
		ResourceManager.getDecoder( format ).decode( this );
	}
	
}
