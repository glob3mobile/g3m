//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;
attribute vec3 aNormal;

uniform mat4 uModelview;
//uniform mat4 uProjection;
//uniform mat4 uCameraModel;
uniform mat4 uModel;

uniform float uPointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

varying vec3 vertex_normal;

void main() {

  vertex_normal = normalize( vec3(uModel * vec4(aNormal, 0.0) ));
  
//  diffuseLightIntensity = max(dot(normal, lightDir), 0.0);

//  if (length(normal) > 1.01 || length(normal) < 0.99 || dot(normal, lightDir) > 1.01){
//    diffuseLightIntensity = 0.0;
//  }

  gl_Position = uModelview * aPosition;
  
  TextureCoordOut = aTextureCoord;

  gl_PointSize = uPointSize;
}
