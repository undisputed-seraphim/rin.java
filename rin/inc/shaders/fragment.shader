uniform sampler2D sampler;
uniform bool useTexture;
uniform bool useColor;

uniform vec4 color;

varying vec2 vTexture;

void main( void ) {
	vec4 texel = vec4( 1.0, 0.0, 0.0, 1.0 );
	if( useColor ) {
		texel = color;
	} else if( useTexture ) {
		texel = texture2D( sampler, vec2( vTexture.s, vTexture.t ) );
	}
	
	gl_FragColor = texel;
}