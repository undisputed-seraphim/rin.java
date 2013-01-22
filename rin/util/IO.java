package rin.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class IO {
	public static class file {
		public static InputStream asInputStream( String file ) {
			InputStream in = null;
			try {
				in = new FileInputStream( file );
			} catch( FileNotFoundException e ) {
				System.out.println( "FileNotFoundException raised. file = " + file );
				return null;
			}
			return in;
		}
		
		public static String asString( String file ) {
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
					source.append(line).append( System.getProperty( "line.separator" ) );
			} catch( IOException e ) {
				System.out.println( "IOException raised. file = " + file );
				return "";
			}
			
			return source.toString();
		}
	}
}
