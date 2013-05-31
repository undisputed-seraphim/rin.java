package rin.engine.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

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
	
	public boolean delete() {
		return target.delete();
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
	
	@Override
	public String toString() {
		return FileUtils.toString( target );
	}
	
	public Resource write( Object ... o ) {
		if( fos == null )
			openStream();
		
		for( int i = 0 ; i < o.length; i++ )
			FileUtils.writeBytes( fos, o[i].toString().getBytes() );
		return this;
	}
	
	public Resource writeLine( Object ... o ) {
		if( fos == null )
			openStream();
		
		for( int i = 0 ; i < o.length; i++ )
			FileUtils.writeBytes( fos, o[i].toString().getBytes() );
		FileUtils.writeBytes( fos, LS.getBytes() );
		return this;
	}
	
	public boolean writeBytes( byte[] bytes ) {
		FileUtils.writeBytes( target, bytes );
		return true;
	}
	
	public boolean writeBuffer( ByteBuffer buffer ) {
		FileOutputStream fos = FileUtils.getOutputStream( target );
		WritableByteChannel bc = Channels.newChannel( fos );
		
		try {
			bc.write( buffer );
			fos.flush();
			fos.close();
			return true;
		} catch( IOException ex ) {
			System.out.println( "Resource#writeBuffer(ByteBuffer): failed to write buffer or close file." );
		}
		
		return false;
	}
	
}
