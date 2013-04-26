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
	public static <T> T merge( T ... arr ) {
		int len = 0;
		for( T t : arr )
			len += Array.getLength( t );
		
		Class<?> componentType = arr.getClass().getComponentType();
		T res = (T)Array.newInstance( componentType, len );
		
		len = 0;
		for( T t : arr )
			for( int i = 0; i < Array.getLength( t ); i++ )
				Array.set( res, len++, componentType.cast( Array.get( t, i ) ) );
		
		return res;
	}
	
}
