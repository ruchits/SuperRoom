precision mediump float;

uniform vec3 uSpotLightVec;
uniform vec3 uSpotLightPos;

uniform sampler2D uTexId;

varying vec4 vPosition;
varying vec3 vNormal;
varying vec2 vTexCoords;

const float cos_outer_cone_angle = 0.87; // 30 degrees
const float cos_inner_cone_angle = 0.97; // 25 degrees
const float max_light_distance_squared = 5625.0;

void main (void)
{
	vec4 final_color = vec4(0.0,0.0,0.0,0.0);

	vec3 spotDir = normalize(uSpotLightVec);
	vec3 lightDir = normalize(vPosition.xyz - uSpotLightPos);

	//cos 0  = 1
	//cos 90 = 0

	float cos_inner_minus_outer_angle =
	      cos_inner_cone_angle - cos_outer_cone_angle;

	float lightAngle = dot(lightDir, spotDir);

	float spotWeight = clamp(
		(lightAngle - cos_outer_cone_angle) / cos_inner_minus_outer_angle,
		0.0, 1.0);

	vec3 normal = normalize(vNormal);
	float diffuseWeight = max(dot(normal,-lightDir), 0.0);

	float shadowValue = diffuseWeight * 0.05;


	float distanceToPointSquared = (vPosition.x - uSpotLightPos.x) * (vPosition.x - uSpotLightPos.x)
								+(vPosition.y - uSpotLightPos.y) * (vPosition.y - uSpotLightPos.y)
								+(vPosition.z - uSpotLightPos.z) * (vPosition.z - uSpotLightPos.z);

	//diffuse falloff
	float diffuseFalloff = 1.0-clamp((distanceToPointSquared/max_light_distance_squared),0.0,1.0);



	if (lightAngle > cos_outer_cone_angle)
	{
		vec4 texColor = texture2D(uTexId, vTexCoords);
		final_color = vec4(texColor.rgb * diffuseWeight * spotWeight * diffuseFalloff, texColor.a);

		//temp yellowing-effect hack:
		final_color.xyz *= vec3(1.0,0.9,0.7);

		float finalShadowValue = shadowValue * (1.0-(spotWeight*diffuseFalloff));
		final_color += vec4(finalShadowValue,finalShadowValue,finalShadowValue,0.0);
	}
	else
	{
		final_color = vec4(shadowValue,shadowValue,shadowValue,1.0);
	}

	gl_FragColor = final_color;
}


