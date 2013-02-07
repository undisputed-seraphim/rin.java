uniform sampler2D sampler;
uniform sampler2DArray samplerA;
uniform bool useTexture;
uniform bool use3D;
uniform bool useColor;

uniform vec4 color;

varying vec3 vTexture;

void main( void ) {
	vec4 texel = vec4( 1.0, 0.0, 0.0, 1.0 );
	if( useColor ) {
		texel = color;
	} else if( useTexture ) {
		if( use3D ) {
			texel = vec4( texture2DArray( samplerA, vec3( vTexture.x, vTexture.y, 0 ) ).rgb, 1.0 );
		} else {
			texel = texture2D( sampler, vec2( vTexture.x, vTexture.y ) );
		}
	}
	
	gl_FragColor = texel;
}