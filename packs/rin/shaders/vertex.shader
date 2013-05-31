#version 120

attribute vec4 vertex;
attribute vec4 color;
attribute vec4 normal;
attribute vec4 texture;
attribute vec4 bone;
attribute vec4 weight;

uniform mat4 vMatrix;
uniform mat4 mMatrix;

varying vec4 vVertex;
varying vec4 vColor;
varying vec4 vNormal;
varying vec4 vTexture;

uniform vec4 quats[200];
uniform vec3 trans[200];

varying vec4 vTransformedNormal;

mat4 getTransform( int index ) {
	float yy, zz, xy, zw, xz, yw, xx, yz, xw;
	vec4 tmp = quats[index];
	
	yy = tmp.y*tmp.y;
	zz = tmp.z*tmp.z;
	xy = tmp.x*tmp.y;
	zw = tmp.z*tmp.w;
	xz = tmp.x*tmp.z;
	yw = tmp.y*tmp.w;
	xx = tmp.x*tmp.x;
	yz = tmp.y*tmp.z;
	xw = tmp.x*tmp.w;
	
	mat4 mtmp = mat4(
		vec4(1.0-2.0*yy-2.0*zz,2.0*xy+2.0*zw,2.0*xz-2.0*yw,0),
		vec4(2.0*xy-2.0*zw,1.0-2.0*xx-2.0*zz,2.0*yz+2.0*xw,0),
		vec4(2.0*xz+2.0*yw,2.0*yz-2.0*xw,1.0-2.0*xx-2.0*yy,0),
		vec4(trans[index].x,trans[index].y,trans[index].z,1));
	return mtmp;
}

void main( void ) {
	vec4 pos = vec4( vertex.xyz, 1 );
	
	if( weight.x != 0.0 ) {
		mat4 mtmp;
		
		mtmp = getTransform( int(bone.x) );
		pos += weight.x * mtmp * vec4( vertex.xyz, 1.0 );
		
		if( weight.y != 0.0 ) {
			mtmp = getTransform( int(bone.y) );
			pos += weight.y * mtmp * vec4( vertex.xyz, 1.0 );
			
			if( weight.z != 0.0 ) {
				mtmp = getTransform( int(bone.z) );
				pos += weight.z * mtmp * vec4( vertex.xyz, 1.0 );
				
				if( weight.w != 0.0 ) {
					mat4 mtmp = getTransform( int(bone.w) );
					pos += weight.w * mtmp * vec4( vertex.xyz, 1.0 );
					
					/* if more bones than 4 per vertex needed, added more attributes and repeat above */
				}
			}
		}
	}
	
	gl_Position = vMatrix * mMatrix * vec4( pos.xyz, 1.0 );
	vVertex = vertex;
	vColor = color;
	vNormal = normal;
	vTexture = texture;
	/*mat4 nMatrix = mMatrix;
	vTransformedNormal = nMatrix * vec4( normal.xyz, 1.0 );*/
}