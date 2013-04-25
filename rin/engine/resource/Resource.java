package rin.engine.resource;

import java.io.File;

public class Resource extends ResourcePointer {

	private Directory parent;
	
	public Resource( File file ) {
		super( file );
		parent = new Directory( new File( file.getParent() ) );
	}
	
	public String getBaseName() {
		return target.getName().substring( 0, target.getName().lastIndexOf( "." ) );
	}
	
	public String getExtension() {
		return target.getName().substring( target.getName().lastIndexOf( "." ) + 1 );
	}

	public Directory getDirectory() {
		if( parent == null )
			parent = new Directory( new File( target.getPath() ) );
		
		return parent;
	}
}
