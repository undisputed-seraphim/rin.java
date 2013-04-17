package rin.engine.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class ArrayUtils {

	public static <T> String asString( T arr ) {
		String res = "[ ";
		for( int i = 0; i < Array.getLength( arr ); i++ ) {
			if( !res.equals( "[ " ) )
				res += ", ";
			res += Array.get( arr, i );
		}
		
		return res + " ]";
	}
	
	public static <T> String asString( T[] arr ) {
		String res = "[ ";
		for( T t : arr ) {
			if( !res.equals( "[ " ) )
				res += ", ";
			res += t;
		}
		
		return res + " ]";
	}
	
	public static <T> String asString( Collection<T> collection ) {
		String res = "[ ";
		for( T t : collection ) {
			if( !res.equals( "[ " ) )
				res += ", ";
			res += t;
		}
		
		return res + " ]";
	}
	
	public static <T1, T2> String asString( Map<T1, T2> map ) {
		String res = "{ ";
		for( T1 t1 : map.keySet() ) {
			if( !res.equals( "{ " ) )
				res += ", ";
			res += "[" + t1 + " : " + map.get( t1 ) + "]";
		}
		
		return res + " }";
	}
}
