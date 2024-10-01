attribute vec2 aTextureCoord;

uniform mat4 uModelview;

uniform vec4 uBillboardPosition;
uniform vec2 uBillboardAnchor; // Anchor in UV (texture-like) coordinates

uniform vec2 uTextureExtent;
uniform vec2 uViewPortExtent;

uniform mediump vec2 uTranslationTexCoord;
uniform mediump vec2 uScaleTexCoord;

varying vec2 TextureCoordOut;

void main() {
  gl_Position = uModelview * uBillboardPosition;

  float fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;
  float fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;

  gl_Position.x += (aTextureCoord.x - uBillboardAnchor.x) * fx;
  gl_Position.y -= (aTextureCoord.y - uBillboardAnchor.y) * fy;

  //Transformed Tex Coords applied to Billboard
  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;
}
