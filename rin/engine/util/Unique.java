package rin.engine.util;

import java.util.HashMap;

public class Unique {
	
	private static HashMap<Integer, UniquePointer> oInt = new HashMap<Integer, UniquePointer>();
	
	private static int uInt = -1;
	
	public static int getNextId() {
		if( uInt + 1 == Integer.MAX_VALUE )
			uInt = Integer.MIN_VALUE;
		return uInt++;
	}
	
	public static void cache( UniquePointer up ) {
		oInt.put( up.getUniqueId(), up );
	}
	
	public static UniquePointer get( int id ) { return oInt.get( id ); }
	public static <R> R get( int id, Class<R> cls ) { return cls.cast( get( id ) ); }
	
}
