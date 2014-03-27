attribute vec4 aPosition;
attribute vec2 aTextureCoord;

uniform mediump vec2 uTranslationTexCoord;
uniform mediump vec2 uScaleTexCoord;
uniform mat4 uModelview;

uniform float uPointSize;

uniform float uRotationAngleTexCoord;
uniform vec2 uRotationCenterTexCoord;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

void main() {
  gl_Position = uModelview * aPosition;

  float s = sin( uRotationAngleTexCoord );
  float c = cos( uRotationAngleTexCoord );

  //  vec2 textureCoord = aTextureCoord - uRotationCenterTexCoord;
  //
  //  vec2 newTextureCoord = vec2((textureCoord.x * c) + (textureCoord.y * s),
  //                              (-textureCoord.x * s) + (textureCoord.y * c));
  //
  //  newTextureCoord += uRotationCenterTexCoord;
  //
  //  TextureCoordOut = (newTextureCoord + uTranslationTexCoord) * uScaleTexCoord;

  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;

  TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;

  TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),
                         (-TextureCoordOut.x * s) + (TextureCoordOut.y * c));

  TextureCoordOut += uRotationCenterTexCoord;



  gl_PointSize = uPointSize;
}
