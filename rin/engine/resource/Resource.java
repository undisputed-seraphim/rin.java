package rin.engine.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rin.engine.util.ArrayUtils;
import rin.engine.util.FileUtils;

public class Resource extends ResourcePointer {

	private static final String LS = System.getProperty( "line.separator" );
	
	private Directory parent;
	private FileOutputStream fos;
	
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
	
	public boolean isOpen() {
		return fos != null;
	}
	
	public void openStream() {
		if( fos == null )
			fos = FileUtils.getOutputStream( target );
	}
	
	public void closeStream() {
		if( fos != null ) {
			try {
				fos.flush();
				fos.close();
			} catch( IOException ex ) {
				System.out.println( "IOEXCEPTION" );
			}
		}
	}
	
	public byte[] toByteArray() {
		return FileUtils.toByteArray( target );
	}
	
	public Resource write( Object ... o ) {
		for( int i = 0 ; i < o.length; i++ )
			FileUtils.writeBytes( fos, o[i].toString().getBytes() );
		return this;
	}
	
	public Resource writeLine( Object ... o ) {
		for( int i = 0 ; i < o.length; i++ )
			FileUtils.writeBytes( fos, o[i].toString().getBytes() );
		FileUtils.writeBytes( fos, LS.getBytes() );
		return this;
	}
	
	public void writeBytes( byte[] bytes ) {
		FileUtils.writeBytes( target, bytes );
	}
	
}
