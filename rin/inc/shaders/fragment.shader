uniform sampler2D sampler;
uniform bool useTexture;

varying vec2 vTexture;

void main( void ) {
	vec4 texel = vec4( 1.0, 0.0, 0.0, 0.1 );
	if( useTexture ) {
		texel = texture2D( sampler, vec2( vTexture.s, vTexture.t ) );
	}
	gl_FragColor = vec4( texel.xyz, 1.0 );
}