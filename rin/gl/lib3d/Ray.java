package rin.gl.lib3d;

import rin.util.math.*;

public class Ray {
	protected Vec3 position;
	protected Vec3 direction;
	protected Vec3 up;
	
	public Ray( Mat4 m ) {
		this.position = Mat4.getPos( m );
		this.direction = Mat4.getLookAt( m );
	}
	
	public boolean intersects( Mesh m ) {
		float	x = m.bbox.xMin + m.position.x, X = m.bbox.xMax + m.position.x,
				y = m.bbox.yMin + m.position.y, Y = m.bbox.yMax + m.position.y,
				z = m.bbox.zMin + m.position.z, Z = m.bbox.zMax + m.position.z;
		
		if( x <= this.position.x && X >= this.position.x )
			if( y <= this.position.y && Y >= this.position.y )
				if( z <= this.position.z && Z >= this.position.z )
					return true;
		
		return false;
	}
}
