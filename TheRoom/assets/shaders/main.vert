uniform mat4 uProjViewMatrix;

attribute vec4 aPosition;
attribute vec3 aNormal;
attribute vec2 aTexCoords;

varying vec4 vPosition;
varying vec3 vNormal;
varying vec2 vTexCoords;

void main()
{
	vTexCoords = aTexCoords;
	vNormal = aNormal;
	vPosition = aPosition;
	gl_Position = uProjViewMatrix * aPosition;
}