//
//  Shader.vsh
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

attribute vec4 Position;
attribute vec2 TextureCoord;
attribute vec4 Color;

uniform mat4 Projection;
uniform mat4 Modelview;

uniform bool BillBoard;
uniform float ViewPortRatio;
uniform float PointSize;

varying vec4 VertexColor;
varying vec2 TextureCoordOut;

/* //USEFUL VARIABLES FOR LIGHTING
varying float DiffuseLighting;
uniform vec3 LightDirection; // light direction in eye coords
*/


void main() {
  gl_Position = Projection * Modelview * Position;
  
  if (BillBoard) {
    gl_Position.x += (-0.05 + TextureCoord.x * 0.1) * gl_Position.w;
    gl_Position.y -= (-0.05 + TextureCoord.y * 0.1) * gl_Position.w * ViewPortRatio;
  }
  
  TextureCoordOut = TextureCoord;
  
  VertexColor = Color;
  
  gl_PointSize = PointSize;
}
