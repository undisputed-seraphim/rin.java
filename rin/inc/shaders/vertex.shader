attribute vec3 vertex;
attribute vec3 normal;
attribute vec2 texture;
attribute vec4 unique;

uniform mat4 pMatrix;
uniform mat4 vMatrix;
uniform mat4 mMatrix;

uniform vec4 color;

varying vec2 vTexture;
varying vec3 vNormal;
varying vec4 vColor;
varying vec4 vUnique;

void main( void ) {
	gl_Position = vMatrix * mMatrix * vec4( vertex, 1.0 );
	vNormal = normal;
	vTexture = texture;
	vColor = color;
	vUnique = unique;
}