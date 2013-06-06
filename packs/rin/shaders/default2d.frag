uniform sampler2D sampler;

uniform bool useTexture;

varying vec4 vTexture;


void main( void ) {
	vec4 texel = vec4( 1.0, 0.0, 0.0, 1.0 );
	texel = texture2D( sampler, vec2( vTexture.x, vTexture.y ) );
	
	if( texel.w == 0.0 ) discard;
	gl_FragColor = texel;
}