package rin.gl.lib3d;

import java.util.Comparator;

import rin.gl.lib3d.interfaces.Positionable;

public class DistanceComparator implements Comparator<Positionable> {

	@Override public int compare( Positionable o1, Positionable o2 ) {
		return o1.compareTo( o2 );
	}

}
