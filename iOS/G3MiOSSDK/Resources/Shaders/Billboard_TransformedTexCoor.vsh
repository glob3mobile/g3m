//
//  Billboard.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec2 aTextureCoord;

uniform mat4 uModelview;

uniform vec4 uBillboardPosition;
uniform vec2 uBillboardAnchor; //Anchor in UV (texture-like) coordinates

uniform vec2 uTextureExtent;
uniform vec2 uViewPortExtent;

uniform mediump vec2 uTranslationTexCoord;
uniform mediump vec2 uScaleTexCoord;

varying vec2 TextureCoordOut;

void main() {
  gl_Position = uModelview * uBillboardPosition;
  
  gl_Position.x += ((aTextureCoord.x - 0.5) * 2.0 * uTextureExtent.x / uViewPortExtent.x) * gl_Position.w;
  gl_Position.y -= ((aTextureCoord.y - 0.5) * 2.0 * uTextureExtent.y / uViewPortExtent.y) * gl_Position.w;
  
  gl_Position.x += (uBillboardAnchor.x * (uTextureExtent.x / uViewPortExtent.x)) * gl_Position.w;
  gl_Position.y += (uBillboardAnchor.y * (uTextureExtent.y / uViewPortExtent.y)) * gl_Position.w;
  
  //Transformed Tex Coords applied to Billboard
  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;
}
