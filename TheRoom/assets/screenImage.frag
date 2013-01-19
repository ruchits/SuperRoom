precision mediump float;

uniform sampler2D uTexId;
varying vec2 vTexCoords;

void main()
{
	vec4 texColor = texture2D(uTexId, vTexCoords);
	gl_FragColor = vec4(texColor.rgb, texColor.a);
}