attribute vec3 vertex;
attribute vec3 normal;
attribute vec2 texture;

uniform mat4 pMatrix;
uniform mat4 vMatrix;
uniform mat4 mMatrix;

varying vec2 vTexture;
varying vec3 vNormal;

void main( void ) {
	gl_Position = vMatrix * mMatrix * vec4( vertex, 1.0 );
	vNormal = normal;
	vTexture = texture;
}