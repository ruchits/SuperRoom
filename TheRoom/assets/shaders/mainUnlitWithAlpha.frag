precision mediump float;

uniform sampler2D uTexId;
uniform sampler2D uTexAlphaId;

varying vec2 vTexCoords;

void main (void)
{
	vec4 texColor = texture2D(uTexId, vTexCoords);
	texColor.a = texture2D(uTexAlphaId, vTexCoords).r;
	gl_FragColor = texColor;
}


