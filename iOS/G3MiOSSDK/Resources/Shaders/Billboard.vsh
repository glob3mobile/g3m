//
//  Billboard.vsh
//
//  Created by José Miguel Santana Núñez
//

attribute vec2 aTextureCoord;

uniform mat4 uModelview;

uniform vec4 uBillboardPosition;

uniform vec2 uTextureExtent;
uniform vec2 uViewPortExtent;

varying vec2 TextureCoordOut;

void main() {
  gl_Position = uModelview * uBillboardPosition;

  if (gl_Position.x < 0.0){
    gl_Position.x = 0.0;
  }

  if (gl_Position.x > uViewPortExtent.x){
    gl_Position.x = uViewPortExtent.x;
  }

  if (gl_Position.y < 0.0){
    gl_Position.y = 0.0;
  }

  if (gl_Position.y > uViewPortExtent.y){
    gl_Position.y = uViewPortExtent.y;
  }
  
  gl_Position.x += ((aTextureCoord.x - 0.5) * 2.0 * uTextureExtent.x / uViewPortExtent.x) * gl_Position.w;
  gl_Position.y -= ((aTextureCoord.y - 0.5) * 2.0 * uTextureExtent.y / uViewPortExtent.y) * gl_Position.w;
  
  TextureCoordOut = aTextureCoord;
}
