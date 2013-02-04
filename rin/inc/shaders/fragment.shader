uniform sampler2D sampler[10];
uniform bool useTexture;
uniform bool useUnique;

varying vec2 vTexture;
varying vec4 vColor;
varying vec4 vUnique;

void main( void ) {
	vec4 texel = vec4( 1.0, 0.0, 0.0, 0.1 );
	if( useUnique ) {
		texel = vUnique;
	} else if( useTexture ) {
		texel = texture2D( sampler[0], vec2( vTexture.s, vTexture.t ) );
	} else {
		texel = vColor;
	}
	
	gl_FragColor = vec4( texel.xyz, 1.0 );
}