//
//  Billboard.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec2 aTextureCoord;

uniform mat4 uModelview;

uniform vec2 uBillboardAnchor; //Anchor in UV (texture-like) coordinates

uniform vec2 uTextureExtent;
uniform vec2 uViewPortExtent;

varying vec2 TextureCoordOut;

void main() {
  //gl_Position = uModelview * vec4(.0,.0,.0,1.0);
  //gl_Position = vec4(uModelview[3][0], uModelview[3][1], uModelview[3][2], uModelview[3][3]);
  gl_Position = uModelview[3].xyzw; //Accesing 4th column (translation of modelview matrix)
  
  float fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;
  float fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;
  
  gl_Position.x += ((aTextureCoord.x - 0.5) - (uBillboardAnchor.x - 0.5)) * fx;
  gl_Position.y -= ((aTextureCoord.y - 0.5) - (uBillboardAnchor.y - 0.5)) * fy;
  
  TextureCoordOut = aTextureCoord;
}
