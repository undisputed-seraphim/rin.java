package rin.util;

import java.util.ArrayList;

public class RinUtils {
	public static <T> T[] toArray( ArrayList<T> t, T[] res ) {
		int length = t.size();

		for( int i = 0; i < length; i++ ) {
			res[i] = t.get( i );
		}
		
		return res;
	}
}
