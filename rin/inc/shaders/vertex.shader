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

uniform vec4 quats[50];
uniform vec3 trans[50];

varying vec4 vTransformedNormal;

void main( void ) {
	vec4 pos = vec4( vertex.xyz, 1 );
	vec4 tmp;
	float yy, zz, xy, zw, xz, yw, xx, yz, xw;
					if( weight.x != 0.0 ) {
						tmp = quats[ int(bone.x) ];
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
							vec4(0,0,0,1) );
						pos += weight.x * mtmp * vec4( vertex.xyz, 1.0 );
						pos += weight.x * vec4(trans[int(bone.x)].x, trans[int(bone.x)].y, trans[int(bone.x)].z,0);
					}
					if( weight.y != 0.0 ) {
						tmp = quats[ int(bone.y) ];
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
							vec4(0,0,0,1) );
						pos += weight.y * mtmp * vec4( vertex.xyz, 1.0 );
						pos += weight.y * vec4(trans[int(bone.y)].x, trans[int(bone.y)].y, trans[int(bone.y)].z,0);
					}
					if( weight.z != 0.0 ) {
						tmp = quats[ int(bone.z) ];
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
							vec4(0,0,0,1) );
						pos += weight.z * mtmp * vec4( vertex.xyz, 1.0 );
						pos += weight.z * vec4(trans[int(bone.z)].x, trans[int(bone.z)].y, trans[int(bone.z)].z,0);
					}
					if( weight.w != 0.0 ) {
						tmp = quats[ int(bone.w) ];
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
							vec4(0,0,0,1) );
						pos += weight.w * mtmp * vec4( vertex.xyz, 1.0 );
						pos += weight.w * vec4(trans[int(bone.w)].x, trans[int(bone.w)].y, trans[int(bone.w)].z,0);
					}
	gl_Position = vMatrix * mMatrix * vec4( pos.xyz, 1.0 );
	vVertex = vertex;
	vColor = color;
	vNormal = normal;
	vTexture = texture;
	/*mat4 nMatrix = mMatrix;
	vTransformedNormal = nMatrix * vec4( normal.xyz, 1.0 );*/
}