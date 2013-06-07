#version 120

attribute vec2 vertex;
attribute vec2 texture;

uniform mat4 pMatrix;
uniform mat4 vMatrix;
uniform mat4 mMatrix;

uniform float zindex = 0.0;
uniform int mapw = 1;
uniform int maph = 1;

uniform int sw = 0;
uniform int sh = 0;

uniform int mapx = 1;
uniform int mapy = 1;

varying vec2 vTexture;

float getX( float inx ) {
	if( inx == 0 ) return mapx * sw / float(mapw);
	return (mapx*sw+sw) / float( mapw );
}

float getY( float iny ) {
	if( iny == 0 ) return mapy*sh / float(maph);
	return (mapy*sh+sh) / float( maph );
}

vec2 getCoords( float inx, float iny ) {
	vec2 c = vec2( getX( inx ), getY( iny ) );
	return c;
}

void main( void ) {
	vec3 pos = vec3( vertex.x, vertex.y, zindex );
	gl_Position = pMatrix * vMatrix * mMatrix * vec4( pos, 1.0 );
	vTexture = getCoords( texture.x, texture.y );
}