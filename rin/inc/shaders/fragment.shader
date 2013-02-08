uniform sampler2D sampler;
uniform sampler2DArray samplerA;

uniform bool useUnique;
uniform bool useColor;
uniform bool useTexture;
uniform bool use3D;

varying vec4 vVertex;
varying vec4 vColor;
varying vec4 vNormal;
varying vec4 vTexture;

void main( void ) {
	vec4 texel = vec4( 1.0, 0.0, 0.0, 1.0 );
	if( useUnique ) {
		texel = vec4( vVertex.w, vNormal.w, vTexture.w, 1.0 );
	} else if( useColor ) {
		texel = vColor;
	} else if( useTexture ) {
		if( use3D ) {
			texel = vec4( texture2DArray( samplerA, vec3( vTexture.x, vTexture.y, 0 ) ).rgb, 1.0 );
		} else {
			texel = texture2D( sampler, vec2( vTexture.x, vTexture.y ) );
		}
	}
	
	gl_FragColor = texel;
}