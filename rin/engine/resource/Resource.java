package rin.engine.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rin.engine.util.ArrayUtils;
import rin.engine.util.FileUtils;

public class Resource extends ResourcePointer {

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
	
	public void openDynamicStream() {
		if( fos == null )
			fos = FileUtils.getOutputStream( target );
	}
	
	public void closeDynamicStream() {
		if( fos != null ) {
			try {
				fos.flush();
				fos.close();
			} catch( IOException ex ) {
				System.out.println( "IOEXCEPTION" );
			}
		}
	}
	
	public byte[] asByteArray() {
		return FileUtils.asByteArray( target.getPath() );
	}
	
	public void writeDynamicBytes( byte[] bytes ) {
		FileUtils.writeBytes( fos, bytes );
	}
	
	public void writeDynamicString( String str ) {
		FileUtils.writeBytes( fos, str.getBytes(), System.getProperty( "line.separator" ).getBytes() );
	}
	
	public void writeBytes( byte[] bytes ) {
		FileUtils.writeBytes( target, bytes );
	}
	
}
