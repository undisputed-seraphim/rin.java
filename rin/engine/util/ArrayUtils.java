package rin.engine.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class ArrayUtils {

	public static <T> String asString( T arr ) {
		String res = "[ ";
		if( arr == null )
			return res + " ]";
		
		for( int i = 0; i < Array.getLength( arr ); i++ ) {
			if( !res.equals( "[ " ) )
				res += ", ";
			res += Array.get( arr, i );
		}
		
		return res + " ]";
	}
	
	public static <T> String asString( T[] arr ) {
		String res = "[ ";
		if( arr == null )
			return res + " ]";
		
		for( T t : arr ) {
			if( !res.equals( "[ " ) )
				res += ", ";
			res += t;
		}
		
		return res + " ]";
	}
	
	public static <T> String asString( Collection<T> collection ) {
		String res = "[ ";
		if( collection == null )
			return res + " ]";
		
		for( T t : collection ) {
			if( !res.equals( "[ " ) )
				res += ", ";
			res += t;
		}
		
		return res + " ]";
	}
	
	public static <T1, T2> String asString( Map<T1, T2> map ) {
		String res = "{ ";
		if( map == null )
			return res + " ]";
		
		for( T1 t1 : map.keySet() ) {
			if( !res.equals( "{ " ) )
				res += ", ";
			res += "[" + t1 + " : " + map.get( t1 ) + "]";
		}
		
		return res + " }";
	}
	
	public static <T> T[] newInstance( T ... args ) { return args; }
	
	public static String[] toStringArray( String arr, String delimiter ) {
		return arr.split( delimiter );
	}
	
	public static float[] toFloatArray( String arr, String delimiter ) {
		String[] tmp = toStringArray( arr, delimiter );
		float[] res = new float[tmp.length];
		for( int i = 0; i < res.length; i++ )
			res[i] = Float.parseFloat( tmp[i] );
		return res;
	}
	
	public static <T> float[] toFloatArray( T[] arr ) {
		float[] res = new float[arr.length];
		for( int i = 0; i < res.length; i++ )
			res[i] = Float.parseFloat( arr[i].toString() );
		return res;
	}
	
	@SuppressWarnings( "unchecked" )
	public static <T> T flip( T arr ) {
		int length = Array.getLength( arr );
		T res = (T)Array.newInstance( arr.getClass().getComponentType(), length );
		for( int i = 0; i < length; i++ )
			Array.set( res, length-1-i, Array.get( arr, i ) );
		arr = res;
		return res;
	}
	
	public static <T> T flatten( T[] arr, T res ) {
		int k = 0;
		for( int i = 0; i < arr.length; i++ )
			for( int j = 0; j < Array.getLength( arr[i] ); j++ ) {
				Array.set( res, k++, Array.get( arr[i], j ) );
			}
		return res;
	}
	
	public static float min( float[] arr ) {
		float min = arr[0];
		for( float f : arr )
			if( f < min )
				min = f;
		return min;
	}
	
	public static float max( float[] arr ) {
		float max = arr[0];
		for( float f : arr )
			if( f > max )
				max = f;
		return max;
	}
	
	public static <T> T merge( T res, T ... arr ) {
		int k = 0;
		for( T t : arr )
			for( int i = 0; i < Array.getLength( t ); i++ )
				Array.set( res, k++, Array.get( t, i ) );
		
		return res;
	}
	
}
