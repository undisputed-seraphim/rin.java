attribute vec4 vertex;
attribute vec4 color;
attribute vec4 normal;
attribute vec4 texture;

uniform mat4 vMatrix;
uniform mat4 mMatrix;

varying vec4 vVertex;
varying vec4 vColor;
varying vec4 vNormal;
varying vec4 vTexture;

void main( void ) {
	gl_Position = vMatrix * mMatrix * vec4( vertex.xyz, 1.0 );
	vVertex = vertex;
	vColor = color;
	vNormal = normal;
	vTexture = texture;
}