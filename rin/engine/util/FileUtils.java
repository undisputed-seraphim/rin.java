package rin.engine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import rin.util.IO;

public class FileUtils {
	
	public static final String FS = System.getProperty( "file.separator" );
	
	public static FileInputStream getInputStream( File file ) {
		FileInputStream res = null;
		try {
			res = new FileInputStream( file );
		} catch( FileNotFoundException ex ) {
			System.out.println( "FileNotFoundException raised. file = " + file.getPath() );
		}
		
		return res;
	}
	
	public static byte[] toByteArray( File file ) {
		InputStream in = getInputStream( file );
		byte[] res = null;
		try {
			res = new byte[in.available()];
			in.read( res );
			return res;
		} catch( IOException ex ) {
			System.out.println( "IOException raised in asByteArray. [" + file + "]" );
		}
		
		return res;
	}
	
	/*public static File createFile( File path, String name ) {
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
	}*/
	
	public static String toString( File file ) {
		StringBuilder source = new StringBuilder();
		
		/* create input stream for the file contents */
		FileInputStream in;
		try {
			in = new FileInputStream( file );
		} catch( FileNotFoundException e1 ) {
			System.out.println( "FileNotFoundException raised. file = " + file );
			return "";
		}
		
		/* create a buffer to read the file contents from the input stream */
		BufferedReader reader;
		try {
			reader = new BufferedReader( new InputStreamReader( in, "UTF-8" ) );
		} catch( UnsupportedEncodingException e1 ) {
			System.out.println( "UnsupportedEncodingException rased. encoding = utf-8, file = " + file );
			return "";
		}
		
		/* write the lines to a string */
		String line;
		try {
			while( ( line = reader.readLine() ) != null )
				source.append(line).append( IO.LS );
		} catch( IOException e ) {
			System.out.println( "IOException raisedin asString. [" + file + "]" );
			return "";
		}
		
		return source.toString();
	}
	
	public static boolean writeBytes( File file, byte[] bytes ) {
		FileOutputStream fos = getOutputStream( file );
		try {
			writeBytes( fos, bytes );
			fos.flush();
			fos.close();
			return true;
		} catch( IOException ex ) {
			System.out.println( "IOEXCEPTION" );
		}
		
		return false;
	}
	
	public static boolean writeBytes( FileOutputStream fos, byte[] ... bytes ) {
		try {
			for( int i = 0; i < bytes.length; i++ )
				fos.write( bytes[i] );
			fos.flush();
			return true;
		} catch( IOException ex ) {
			System.out.println( "IOEXCEPTION" );
		}
		
		return false;
	}
	
	public static FileOutputStream getOutputStream( File file ) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream( file );
			return fos;
		} catch( FileNotFoundException ex ) {
			System.out.println( "FILENOTFOUND" );
		}
		
		return null;
	}
	
}
