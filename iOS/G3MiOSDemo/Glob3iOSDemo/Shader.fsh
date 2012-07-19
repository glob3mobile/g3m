//
//  Shader.fsh
//
//  Created by Agustín Trujillo Pino on 12/01/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//

varying mediump vec2 TextureCoordOut;

varying mediump vec4 VertexColor;

uniform sampler2D Sampler;
uniform bool EnableTexture;
uniform lowp vec4 FlatColor;

uniform bool EnableColorPerVertex;
uniform bool EnableFlatColor;
uniform mediump float FlatColorIntensity;
uniform mediump float ColorPerVertexIntensity;

uniform mediump vec2 TexCoordTranslation;
uniform mediump vec2 TexCoordScale;

void main() {
  
  if (EnableTexture) {
    
    mediump vec2 texCoord = TextureCoordOut * TexCoordScale;
    texCoord += TexCoordTranslation;
    
    gl_FragColor = texture2D(Sampler, texCoord);

    if (EnableFlatColor || EnableColorPerVertex){

      lowp vec4 color;
      if (EnableFlatColor){
        color = FlatColor;
        if (EnableColorPerVertex)
        {
          color = color * VertexColor;
        }
      } else{
        color = VertexColor;
      }
      
      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;
      gl_FragColor = mix( gl_FragColor,
                        VertexColor,
                        intensity);
    }
  }
  else {
  
    if (EnableColorPerVertex){
      gl_FragColor = VertexColor;
      if (EnableFlatColor)
      {
        gl_FragColor = gl_FragColor * FlatColor;
      }
    } else{
        gl_FragColor = FlatColor;
    }
  
  }
  
}
