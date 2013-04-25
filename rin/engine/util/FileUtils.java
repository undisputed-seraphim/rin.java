package rin.engine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	
	public static final String FS = System.getProperty( "file.separator" );
	
	public static InputStream asInputStream( String file ) {
		InputStream in = null;
		try {
			in = new FileInputStream( file );
		} catch( FileNotFoundException ex ) {
			System.out.println( "FileNotFoundException raised. file = " + file );
			return null;
		}
		
		return in;
	}
	
	public static File asFile( String file ) {
		return new File( file );
	}
	
	public static byte[] asByteArray( String file ) {
		InputStream in = asInputStream( file );
		byte[] res = null;
		try {
			res = new byte[in.available()];
			in.read( res );
			return res;
		} catch( IOException e1 ) {
			System.out.println( "IOException raised in asByteArray. [" + file + "]" );
		}
		
		return res;
	}
	
	public static File createFile( File path, String name ) {
		return createFile( path.getPath() + FS + name );
	}
	
	public static File createFile( String path ) {
		File res = new File( path );
		try {
			res.createNewFile();
		} catch( IOException ex ) {
			//TODO: add exception
		}
		
		return res;
	}
	
	public static boolean writeBytes( File file, byte[] bytes ) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream( file );
			try {
				fos.write( bytes );
				fos.close();
			} catch( IOException ex ) {
				System.out.println( "IOEXCEPTION" );
				// TODO Auto-generated catch block
			}
			return true;
		} catch( FileNotFoundException ex ) {
			// TODO add exception
		}
		
		return false;
	}
}
