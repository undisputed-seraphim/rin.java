#version 120

attribute vec2 vertex;
attribute vec2 texture;

uniform mat4 pMatrix;
uniform mat4 vMatrix;
uniform mat4 mMatrix;

uniform float zindex = 0.0;

varying vec2 vTexture;


void main( void ) {
	vec3 pos = vec3( vertex.x, vertex.y, zindex );
	gl_Position = pMatrix * vMatrix * mMatrix * vec4( pos, 1.0 );
	vTexture = texture;
}