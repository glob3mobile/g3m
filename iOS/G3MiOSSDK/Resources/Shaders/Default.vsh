//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 Position;
attribute vec2 TextureCoord;
attribute vec4 Color;

uniform mediump vec2 TranslationTexCoord;
uniform mediump vec2 ScaleTexCoord;

uniform mat4 uModelview;

uniform float PointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;


void main() {
  gl_Position = uModelview * Position;
  
  TextureCoordOut = (TextureCoord * ScaleTexCoord) + TranslationTexCoord;
  
  VertexColor = Color;
  
  gl_PointSize = PointSize;
}
