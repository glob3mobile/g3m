//
//  BasicShadersGL2.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 15/11/13.
//
//

#ifndef G3MiOSSDK_BasicShadersGL2_h
#define G3MiOSSDK_BasicShadersGL2_h

#include <GPUProgramFactory.hpp>

class BasicShadersGL2: public GPUProgramFactory{

public:
  BasicShadersGL2(){

    GPUProgramSources sourcesBillboard("Billboard",
                                       std::string("attribute vec2 aTextureCoord; \n ") +
                                       "uniform mat4 uModelview; \n " +
                                       "uniform vec4 uBillboardPosition; \n " +
                                       "uniform vec2 uTextureExtent; \n " +
                                       "uniform vec2 uViewPortExtent; \n " +
                                       "varying vec2 TextureCoordOut; \n " +
                                       "void main() { \n " +
                                       "gl_Position = uModelview * uBillboardPosition; \n " +
                                       "gl_Position.x += ((aTextureCoord.x - 0.5) * 2.0 * uTextureExtent.x / uViewPortExtent.x) * gl_Position.w; \n " +
                                       "gl_Position.y -= ((aTextureCoord.y - 0.5) * 2.0 * uTextureExtent.y / uViewPortExtent.y) * gl_Position.w; \n " +
                                       "TextureCoordOut = aTextureCoord; \n " +
                                       "} \n ",
                                       std::string("varying mediump vec2 TextureCoordOut; \n ") +
                                       "uniform sampler2D Sampler; \n " +
                                       "void main() { \n " +
                                       "gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " +
                                       "} \n ");
    this->add(sourcesBillboard);

    GPUProgramSources sourcesColorMesh("ColorMesh",
                                       std::string("attribute vec4 aPosition; \n ") +
                                       "attribute vec4 aColor; \n " +
                                       "uniform mat4 uModelview; \n " +
                                       "uniform float uPointSize; \n " +
                                       "varying vec4 VertexColor; \n " +
                                       "void main() { \n " +
                                       "gl_Position = uModelview * aPosition; \n " +
                                       "VertexColor = aColor; \n " +
                                       "gl_PointSize = uPointSize; \n " +
                                       "} \n ",
                                       std::string("varying mediump vec4 VertexColor; \n ") +
                                       "void main() { \n " +
                                       "gl_FragColor = VertexColor; \n " +
                                       "} \n ");
    this->add(sourcesColorMesh);

    GPUProgramSources sourcesDefault("Default",
                                     std::string("attribute vec4 aPosition; \n ") +
                                     "attribute vec2 aTextureCoord; \n " +
                                     "attribute vec4 aColor; \n " +
                                     "uniform mediump vec2 uTranslationTexCoord; \n " +
                                     "uniform mediump vec2 uScaleTexCoord; \n " +
                                     "uniform mat4 uModelview; \n " +
                                     "uniform float uPointSize; \n " +
                                     "varying vec4 VertexColor; \n " +
                                     "varying vec2 TextureCoordOut; \n " +
                                     "void main() { \n " +
                                     "gl_Position = uModelview * aPosition; \n " +
                                     "TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord; \n " +
                                     "VertexColor = aColor; \n " +
                                     "gl_PointSize = uPointSize; \n " +
                                     "} \n ",
                                     std::string("varying mediump vec2 TextureCoordOut; \n ") +
                                     "varying mediump vec4 VertexColor; \n " +
                                     "uniform sampler2D Sampler; \n " +
                                     "uniform bool EnableTexture; \n " +
                                     "uniform lowp vec4 uFlatColor; \n " +
                                     "uniform bool EnableColorPerVertex; \n " +
                                     "uniform bool EnableFlatColor; \n " +
                                     "uniform mediump float FlatColorIntensity; \n " +
                                     "uniform mediump float ColorPerVertexIntensity; \n " +
                                     "void main() { \n " +
                                     "if (EnableTexture) { \n " +
                                     "gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " +
                                     "if (EnableFlatColor || EnableColorPerVertex) { \n " +
                                     "lowp vec4 color; \n " +
                                     "if (EnableFlatColor) { \n " +
                                     "color = uFlatColor; \n " +
                                     "if (EnableColorPerVertex) { \n " +
                                     "color = color * VertexColor; \n " +
                                     "} \n " +
                                     "} \n " +
                                     "else { \n " +
                                     "color = VertexColor; \n " +
                                     "} \n " +
                                     "lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0; \n " +
                                     "gl_FragColor = mix(gl_FragColor, \n " +
                                     "VertexColor, \n " +
                                     "intensity); \n " +
                                     "} \n " +
                                     "} \n " +
                                     "else { \n " +
                                     "if (EnableColorPerVertex) { \n " +
                                     "gl_FragColor = VertexColor; \n " +
                                     "if (EnableFlatColor) { \n " +
                                     "gl_FragColor = gl_FragColor * uFlatColor; \n " +
                                     "} \n " +
                                     "} \n " +
                                     "else { \n " +
                                     "gl_FragColor = uFlatColor; \n " +
                                     "} \n " +
                                     "} \n " +
                                     "} \n ");
    this->add(sourcesDefault);

    GPUProgramSources sourcesFlatColorMesh("FlatColorMesh",
                                           std::string("attribute vec4 aPosition; \n ") +
                                           "uniform mat4 uModelview; \n " +
                                           "uniform float uPointSize; \n " +
                                           "void main() { \n " +
                                           "gl_Position = uModelview * aPosition; \n " +
                                           "gl_PointSize = uPointSize; \n " +
                                           "} \n ",
                                           std::string("uniform lowp vec4 uFlatColor; \n ") +
                                           "void main() { \n " +
                                           "gl_FragColor = uFlatColor; \n " +
                                           "} \n ");
    this->add(sourcesFlatColorMesh);

    GPUProgramSources sourcesFlatColorMesh_DirectionLight("FlatColorMesh_DirectionLight",
                                                          std::string("attribute vec4 aPosition; \n ") +
                                                          "attribute vec3 aNormal; \n " +
                                                          "uniform mat4 uModelview; \n " +
                                                          "uniform mat4 uModel; \n " +
                                                          "uniform float uPointSize; \n " +
                                                          "uniform vec3 uLightDirection; //MUST BE NORMALIZED \n " +
                                                          "varying float diffuseLightIntensity; \n " +
                                                          "void main() { \n " +
                                                          "vec3 normal = normalize( vec3(uModel * vec4(aNormal, 0.0) )); \n " +
                                                          "vec3 lightDir = normalize( uLightDirection ); \n " +
                                                          "diffuseLightIntensity = max(dot(normal, lightDir), 0.0); \n " +
                                                          "gl_Position = uModelview * aPosition; \n " +
                                                          "gl_PointSize = uPointSize; \n " +
                                                          "} \n ",
                                                          std::string("precision highp float; \n ") +
                                                          "uniform sampler2D Sampler; \n " +
                                                          "uniform float uAmbientLight; \n " +
                                                          "uniform vec4 uLightColor; \n " +
                                                          "varying float diffuseLightIntensity; \n " +
                                                          "uniform lowp vec4 uFlatColor; \n " +
                                                          "void main() { \n " +
                                                          "gl_FragColor = uFlatColor; \n " +
                                                          "vec4 lightColor = vec4(uAmbientLight, uAmbientLight, uAmbientLight, 1.0) + uLightColor * diffuseLightIntensity; \n " +
                                                          "gl_FragColor *= lightColor; \n " +
                                                          "} \n ");
    this->add(sourcesFlatColorMesh_DirectionLight);

    GPUProgramSources sourcesNoColorMesh("NoColorMesh",
                                         std::string("attribute vec4 aPosition; \n ") +
                                         "uniform mat4 uModelview; \n " +
                                         "uniform float uPointSize; \n " +
                                         "void main() { \n " +
                                         "gl_Position = uModelview * aPosition; \n " +
                                         "gl_PointSize = uPointSize; \n " +
                                         "} \n ",
                                         std::string("void main() { \n ") +
                                         "gl_FragColor = vec4(0.5, 0.5, 0.5, 1.0); //GRAY \n " +
                                         "} \n ");
    this->add(sourcesNoColorMesh);

    GPUProgramSources sourcesShader("Shader",
                                    std::string("attribute vec4 Position; \n ") +
                                    "attribute vec2 TextureCoord; \n " +
                                    "attribute vec4 Color; \n " +
                                    "uniform mediump vec2 TranslationTexCoord; \n " +
                                    "uniform mediump vec2 ScaleTexCoord; \n " +
                                    "uniform mat4 Projection; \n " +
                                    "uniform mat4 Modelview; \n " + 
                                    "uniform bool BillBoard; \n " + 
                                    "uniform vec2 TextureExtent; \n " + 
                                    "uniform vec2 ViewPortExtent; \n " + 
                                    "uniform float PointSize; \n " + 
                                    "varying vec4 VertexColor; \n " + 
                                    "varying vec2 TextureCoordOut; \n " + 
                                    "void main() { \n " + 
                                    "gl_Position = Projection * Modelview * Position; \n " + 
                                    "if (BillBoard) { \n " + 
                                    "gl_Position.x += ((TextureCoord.x - 0.5) * 2.0 * TextureExtent.x / ViewPortExtent.x) * gl_Position.w; \n " + 
                                    "gl_Position.y -= ((TextureCoord.y - 0.5) * 2.0 * TextureExtent.y / ViewPortExtent.y) * gl_Position.w; \n " + 
                                    "} \n " + 
                                    "TextureCoordOut = (TextureCoord * ScaleTexCoord) + TranslationTexCoord; \n " + 
                                    "VertexColor = Color; \n " + 
                                    "gl_PointSize = PointSize; \n " + 
                                    "} \n ",
                                    std::string("varying mediump vec2 TextureCoordOut; \n ") + 
                                    "varying mediump vec4 VertexColor; \n " + 
                                    "uniform sampler2D Sampler; \n " + 
                                    "uniform bool EnableTexture; \n " + 
                                    "uniform lowp vec4 FlatColor; \n " + 
                                    "uniform bool EnableColorPerVertex; \n " + 
                                    "uniform bool EnableFlatColor; \n " + 
                                    "uniform mediump float FlatColorIntensity; \n " + 
                                    "uniform mediump float ColorPerVertexIntensity; \n " + 
                                    "void main() { \n " + 
                                    "if (EnableTexture) { \n " + 
                                    "gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
                                    "if (EnableFlatColor || EnableColorPerVertex) { \n " + 
                                    "lowp vec4 color; \n " + 
                                    "if (EnableFlatColor) { \n " + 
                                    "color = FlatColor; \n " + 
                                    "if (EnableColorPerVertex) { \n " + 
                                    "color = color * VertexColor; \n " + 
                                    "} \n " + 
                                    "} \n " + 
                                    "else { \n " + 
                                    "color = VertexColor; \n " + 
                                    "} \n " + 
                                    "lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0; \n " + 
                                    "gl_FragColor = mix(gl_FragColor, \n " + 
                                    "VertexColor, \n " + 
                                    "intensity); \n " + 
                                    "} \n " + 
                                    "} \n " + 
                                    "else { \n " + 
                                    "if (EnableColorPerVertex) { \n " + 
                                    "gl_FragColor = VertexColor; \n " + 
                                    "if (EnableFlatColor) { \n " + 
                                    "gl_FragColor = gl_FragColor * FlatColor; \n " + 
                                    "} \n " + 
                                    "} \n " + 
                                    "else { \n " + 
                                    "gl_FragColor = FlatColor; \n " + 
                                    "} \n " + 
                                    "} \n " + 
                                    "} \n ");
    this->add(sourcesShader);
    
    GPUProgramSources sourcesTexturedMesh("TexturedMesh",
                                          std::string("attribute vec4 aPosition; \n ") + 
                                          "attribute vec2 aTextureCoord; \n " + 
                                          "uniform mat4 uModelview; \n " + 
                                          "uniform float uPointSize; \n " + 
                                          "varying vec4 VertexColor; \n " + 
                                          "varying vec2 TextureCoordOut; \n " + 
                                          "void main() { \n " + 
                                          "gl_Position = uModelview * aPosition; \n " + 
                                          "TextureCoordOut = aTextureCoord; \n " + 
                                          "gl_PointSize = uPointSize; \n " + 
                                          "} \n ",
                                          std::string("varying mediump vec2 TextureCoordOut; \n ") + 
                                          "varying mediump vec4 VertexColor; \n " + 
                                          "uniform sampler2D Sampler; \n " + 
                                          "void main() { \n " + 
                                          "gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
                                          "} \n ");
    this->add(sourcesTexturedMesh);
    
    GPUProgramSources sourcesTexturedMesh_DirectionLight("TexturedMesh_DirectionLight",
                                                         std::string("attribute vec4 aPosition; \n ") + 
                                                         "attribute vec2 aTextureCoord; \n " + 
                                                         "attribute vec3 aNormal; \n " + 
                                                         "uniform mat4 uModelview; \n " + 
                                                         "uniform mat4 uModel; \n " + 
                                                         "uniform float uPointSize; \n " + 
                                                         "varying vec4 VertexColor; \n " + 
                                                         "varying vec2 TextureCoordOut; \n " + 
                                                         "uniform vec3 uLightDirection; //MUST BE NORMALIZED \n " + 
                                                         "varying float diffuseLightIntensity; \n " + 
                                                         "void main() { \n " + 
                                                         "vec3 normal = normalize( vec3(uModel * vec4(aNormal, 0.0) )); \n " + 
                                                         "vec3 lightDir = normalize( uLightDirection ); \n " + 
                                                         "diffuseLightIntensity = max(dot(normal, lightDir), 0.0); \n " + 
                                                         "gl_Position = uModelview * aPosition; \n " + 
                                                         "TextureCoordOut = aTextureCoord; \n " + 
                                                         "gl_PointSize = uPointSize; \n " + 
                                                         "} \n ",
                                                         std::string("precision highp float; \n ") + 
                                                         "varying mediump vec2 TextureCoordOut; \n " + 
                                                         "varying mediump vec4 VertexColor; \n " + 
                                                         "uniform sampler2D Sampler; \n " + 
                                                         "uniform float uAmbientLight; \n " + 
                                                         "uniform vec4 uLightColor; \n " + 
                                                         "varying float diffuseLightIntensity; \n " + 
                                                         "void main() { \n " + 
                                                         "gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
                                                         "vec4 lightColor = vec4(uAmbientLight, uAmbientLight, uAmbientLight, 1.0) + uLightColor * diffuseLightIntensity; \n " + 
                                                         "gl_FragColor *= lightColor; \n " + 
                                                         "} \n ");
    this->add(sourcesTexturedMesh_DirectionLight);
    
    GPUProgramSources sourcesTransformedTexCoorTexturedMesh("TransformedTexCoorTexturedMesh",
                                                            std::string("attribute vec4 aPosition; \n ") + 
                                                            "attribute vec2 aTextureCoord; \n " + 
                                                            "uniform mediump vec2 uTranslationTexCoord; \n " + 
                                                            "uniform mediump vec2 uScaleTexCoord; \n " + 
                                                            "uniform mat4 uModelview; \n " + 
                                                            "uniform float uPointSize; \n " + 
                                                            "varying vec4 VertexColor; \n " + 
                                                            "varying vec2 TextureCoordOut; \n " + 
                                                            "void main() { \n " + 
                                                            "gl_Position = uModelview * aPosition; \n " + 
                                                            "TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord; \n " + 
                                                            "gl_PointSize = uPointSize; \n " + 
                                                            "} \n ",
                                                            std::string("varying mediump vec2 TextureCoordOut; \n ") + 
                                                            "varying mediump vec4 VertexColor; \n " + 
                                                            "uniform sampler2D Sampler; \n " + 
                                                            "void main() { \n " + 
                                                            "gl_FragColor = texture2D(Sampler, TextureCoordOut); \n " + 
                                                            "} \n ");
    this->add(sourcesTransformedTexCoorTexturedMesh);
    
    GPUProgramSources sourcesZRender("ZRender",
                                     std::string("attribute vec4 aPosition; \n ") + 
                                     "uniform mat4 uModelview; \n " + 
                                     "uniform float uPointSize; \n " + 
                                     "void main() { \n " + 
                                     "gl_Position = uModelview * aPosition; \n " + 
                                     "gl_PointSize = uPointSize; \n " + 
                                     "} \n ",
                                     std::string("void main() { \n ") + 
                                     "highp float zFar = 16777215.0; // 2^24-1 \n " + 
                                     "highp float z = gl_FragCoord.z * zFar; \n " + 
                                     "highp float Z = floor(z+0.5); \n " + 
                                     "highp float R = floor(Z/65536.0); \n " + 
                                     "Z -= R * 65536.0; \n " + 
                                     "highp float G = floor(Z/256.0); \n " + 
                                     "highp float B = Z - G * 256.0; \n " + 
                                     "gl_FragColor = vec4(R/255.0, G/255.0, B/255.0, 0.0); \n " + 
                                     "} \n ");
    this->add(sourcesZRender);
    
  }
  
};
#endif