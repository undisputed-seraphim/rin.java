package rin.engine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	
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
	
}
