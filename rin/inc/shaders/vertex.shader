attribute vec3 vertex;
attribute vec3 normal;
attribute vec2 texture;

uniform mat4 pMatrix;
uniform mat4 mvMatrix;
uniform mat4 vMatrix;
uniform bool picking;

varying vec2 vTexture;

void main( void ) {
	gl_Position = vMatrix * mvMatrix * vec4( vertex, 1.0 );
	vTexture = texture;
}