//
//  Shader.vsh
//  Prueba Opengl iPad
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

attribute vec4 Position;
attribute vec2 TextureCoord;

varying vec2 TextureCoordOut;

uniform mat4 Projection;
uniform mat4 Modelview;

uniform bool BillBoard;
uniform float ViewPortRatio;

void main()
{
    if (!BillBoard){
        gl_Position = Projection * Modelview * Position;
        TextureCoordOut = TextureCoord;
    }else{
        gl_Position = Projection * Modelview * Position;
        gl_Position.x += (-0.05 + TextureCoord.x * 0.1)* gl_Position.w ;
        gl_Position.y -= (-0.05+ TextureCoord.y *0.1)* gl_Position.w * ViewPortRatio;
        
        TextureCoordOut = TextureCoord;
    
    }
}
