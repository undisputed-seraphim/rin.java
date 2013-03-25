package rin.util;

import java.lang.reflect.Array;

import rin.util.bio.BIOBuffer;

public class RinUtils {
	public static final class Converter<T> {
		private T data;
		public Converter( T data ) {
			this.data = data;
		}
		
		public <R> R to( R res ) {
			if( this.data.getClass().isArray() ) {
				for( int i = 0; i < Array.getLength( res ); i++ )
					Array.set( res, i, Array.get( this.data, i ) );
				return res;
			} else {
				return (R)this.data;
			}
		}
	}
	
	public static <T> Converter<?> convert( T[] arr ) {
		return new Converter<T[]>( arr );
	}
	
	public static <T> Converter<?> convert( T obj ) {
		return new Converter<T>( obj );
	}
	
	
	public static String asString( Object arr ) { return BIOBuffer.asString( arr, false ); }
	public static String asString( Object arr, boolean trim ) {
		String res = "";
		String split = trim ? "" : " ";
		
		if( arr != null )
			if( arr.getClass().isArray() )
				for( int i = 0; i < Array.getLength( arr ); i++ )
					res += Array.get( arr, i ) + (i+1<Array.getLength( arr )?split:"");
		
		return res;
	}
	
	public static String asString( Object[] arr ) { return BIOBuffer.asString( arr, false ); }
	public static String asString( Object[] arr, boolean trim ) {
		String res = "";
		String split = trim ? "" : " ";
		
		if( arr != null )
			for( int i = 0; i < arr.length; i++ )
				res += arr[i] + (i+1<Array.getLength(arr)?split:"");
		
		return res;
	}
	
	public static <T> T splice( T obj, int index, int length, T res ) {
		for( int i = index; i < length; i++ )
			Array.set( res, i, Array.get( obj, i ) );
		return res;
	}
}
