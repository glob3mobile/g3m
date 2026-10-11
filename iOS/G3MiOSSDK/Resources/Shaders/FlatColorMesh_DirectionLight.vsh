attribute vec4 aPosition;
attribute vec3 aNormal;

uniform mat4 uModelview;
uniform mat4 uModel;

uniform float uPointSize;

uniform vec3 uAmbientLightColor;
uniform vec3 uDiffuseLightColor;

uniform vec3 uDiffuseLightDirection; // normalized by DirectionLightGLFeature
varying vec3 lightColor;

void main() {
  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));
  float diffuseLightIntensity = max(dot(normalInModel, uDiffuseLightDirection), 0.0);

  gl_Position = uModelview * aPosition;

  gl_PointSize = uPointSize;

  //Computing Total Light in Vertex
  lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;
  lightColor = clamp(lightColor, 0.0, 1.0);
}
