//uPosition is the x,y center
uniform vec2 uPosition;
uniform vec2 uSize;

//aPosition is the vertex
attribute vec4 aPosition;
attribute vec2 aTexCoords;

varying vec2 vTexCoords;

void main()
{
	vTexCoords = aTexCoords;
	gl_Position = vec4(
		aPosition.x * uSize.x + uPosition.x,
		aPosition.y * uSize.y + uPosition.y,
		aPosition.z,
		aPosition.w);

	/*gl_Position = vec4(
		aPosition.x * uSize.x + uPosition.x,
		aPosition.y * uSize.y + uPosition.y,
		0.0,0.0);*/
}