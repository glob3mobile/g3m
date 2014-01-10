//
//  Default.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec4 aPosition;
attribute vec2 aTextureCoord;
attribute vec2 aTextureCoord2;

uniform mat4 uModelview;
uniform float uPointSize;

varying vec2 TextureCoordOut;
varying vec2 TextureCoordOut2;

uniform mediump vec2 uTranslationTexCoord;
uniform mediump vec2 uScaleTexCoord;
uniform float uRotationAngleTexCoord;
uniform vec2 uRotationCenterTexCoord;

void main() {
  gl_Position = uModelview * aPosition;

  //Transforming TextureCoordOut

  float s = sin( uRotationAngleTexCoord );
  float c = cos( uRotationAngleTexCoord );

  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;

  TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;

  TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),
                         (-TextureCoordOut.x * s) + (TextureCoordOut.y * c));

  TextureCoordOut += uRotationCenterTexCoord;

  //////////////////////////////

  TextureCoordOut2 = aTextureCoord2;
  
  gl_PointSize = uPointSize;
}
