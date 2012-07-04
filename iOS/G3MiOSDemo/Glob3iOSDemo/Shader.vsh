//
//  Shader.vsh
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

attribute vec4 Position;
attribute vec2 TextureCoord;
attribute vec4 Color;

varying vec2 TextureCoordOut;

uniform mat4 Projection;
uniform mat4 Modelview;

uniform bool BillBoard;
uniform float ViewPortRatio;

varying vec4 VertexColor;

void main() {
  gl_Position = Projection * Modelview * Position;

  if (BillBoard) {
    gl_Position.x += (-0.05 + TextureCoord.x * 0.1) * gl_Position.w ;
    gl_Position.y -= (-0.05 + TextureCoord.y * 0.1) * gl_Position.w * ViewPortRatio;
  }

  TextureCoordOut = TextureCoord;
  
  VertexColor = Color;
  //VertexColor = vec4(1.0, 1.0, 1.0, 1.0);
  
}
