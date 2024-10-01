#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

varying vec2 TextureCoordOut;

varying vec4 VertexColor;

uniform sampler2D Sampler;
uniform bool EnableTexture;
uniform vec4 FlatColor;

uniform bool EnableColorPerVertex;
uniform bool EnableFlatColor;
uniform float FlatColorIntensity;
uniform float ColorPerVertexIntensity;

void main() {
  if (EnableTexture) {
    gl_FragColor = texture2D(Sampler, TextureCoordOut);

    if (EnableFlatColor || EnableColorPerVertex) {
      lowp vec4 color;
      if (EnableFlatColor) {
        color = FlatColor;
        if (EnableColorPerVertex) {
          color = color * VertexColor;
        }
      }
      else {
        color = VertexColor;
      }

      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;
      gl_FragColor = mix(gl_FragColor,
                         VertexColor,
                         intensity);
    }
  }
  else {
    if (EnableColorPerVertex) {
      gl_FragColor = VertexColor;
      if (EnableFlatColor) {
        gl_FragColor = gl_FragColor * FlatColor;
      }
    }
    else {
      gl_FragColor = FlatColor;
    }
  }
}
