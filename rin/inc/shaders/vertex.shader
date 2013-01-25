#version 150

attribute vec3 vertex;
attribute vec3 normal;
attribute vec2 texture;

uniform mat4 pMatrix;
uniform mat4 vMatrix;
uniform mat4 inv_vMatrix;
uniform mat4 mMatrix;

varying vec2 vTexture;

void main( void ) {
	gl_Position = pMatrix * vMatrix * mMatrix * vec4( vertex, 1.0 );
	vTexture = texture;
}