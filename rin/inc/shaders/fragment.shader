uniform sampler2D sampler;

uniform bool useUnique;
uniform bool useColor;
uniform bool useTexture;

varying vec4 vVertex;
varying vec4 vColor;
varying vec4 vNormal;
varying vec4 vTexture;

void main( void ) {
	vec4 texel = vec4( 1.0, 0.0, 0.0, 1.0 );
	if( useUnique ) {
		texel = vec4( vVertex.w, vNormal.w, vTexture.w, 1.0 );
		gl_FragColor = texel;
	} else {
		if( useColor ) {
			texel = vColor;
		} else if( useTexture ) {
			texel = texture2D( sampler, vec2( vTexture.x, vTexture.y ) );
		}
	
		gl_FragColor = texel;
	}
}