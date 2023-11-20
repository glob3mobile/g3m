//
//  BasicShadersGL2.hpp
//  G3M
//
//

#ifndef G3M_BasicShadersGL2_h
#define G3M_BasicShadersGL2_h

#include "GPUProgramFactory.hpp"

class BasicShadersGL2: public GPUProgramFactory {

public:
   BasicShadersGL2() {
#ifdef C_CODE
      const std::string emptyString = "";
#endif
#ifdef JAVA_CODE
      final String emptyString = "";
#endif

// ColorMesh
      {
         GPUProgramSources srcColorMesh(
            "ColorMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec4 aColor;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec4 VertexColor;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  VertexColor = aColor;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec4 VertexColor;\n" +
            "void main() {\n" +
            "  gl_FragColor = VertexColor;\n" +
            "}\n");
         this->add(srcColorMesh);
      }

// FullTransformedTexCoorMultiTexturedMesh
      {
         GPUProgramSources srcFullTransformedTexCoorMultiTexturedMesh(
            "FullTransformedTexCoorMultiTexturedMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "attribute vec2 aTextureCoord2;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "varying vec2 TextureCoordOut2;\n" +
            "uniform mediump vec2 uTranslationTexCoord;\n" +
            "uniform mediump vec2 uScaleTexCoord;\n" +
            "uniform float uRotationAngleTexCoord;\n" +
            "uniform vec2 uRotationCenterTexCoord;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  //Transforming TextureCoordOut\n" +
            "  float s = sin( uRotationAngleTexCoord );\n" +
            "  float c = cos( uRotationAngleTexCoord );\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;\n" +
            "  TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),\n" +
            "                         (-TextureCoordOut.x * s) + (TextureCoordOut.y * c));\n" +
            "  TextureCoordOut += uRotationCenterTexCoord;\n" +
            "  TextureCoordOut2 = aTextureCoord2;\n" +
            "  \n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "varying mediump vec2 TextureCoordOut2;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform sampler2D Sampler2;\n" +
            "void main() {\n" +
            "  mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
            "  mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
            "  gl_FragColor = tex1 * tex2;\n" +
            "}\n");
         this->add(srcFullTransformedTexCoorMultiTexturedMesh);
      }

// TransformedTexCoorTexturedMesh
      {
         GPUProgramSources srcTransformedTexCoorTexturedMesh(
            "TransformedTexCoorTexturedMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "uniform mediump vec2 uTranslationTexCoord;\n" +
            "uniform mediump vec2 uScaleTexCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec4 VertexColor;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  \n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "varying mediump vec4 VertexColor;\n" +
            "uniform sampler2D Sampler;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "}\n");
         this->add(srcTransformedTexCoorTexturedMesh);
      }

// FlatColorMesh_DirectionLight
      {
         GPUProgramSources srcFlatColorMesh_DirectionLight(
            "FlatColorMesh_DirectionLight",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec3 aNormal;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform mat4 uModel;\n" +
            "uniform float uPointSize;\n" +
            "uniform vec3 uAmbientLightColor;\n" +
            "uniform vec3 uDiffuseLightColor;\n" +
            "uniform vec3 uDiffuseLightDirection; //We must normalize\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
            "  vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
            "  \n" +
            "  float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "  //Computing Total Light in Vertex\n" +
            "  lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
            "  lightColor.x = min(lightColor.x, 1.0);\n" +
            "  lightColor.y = min(lightColor.y, 1.0);\n" +
            "  lightColor.z = min(lightColor.z, 1.0);\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "uniform lowp vec4 uFlatColor;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  gl_FragColor.r = uFlatColor.r * lightColor.r;\n" +
            "  gl_FragColor.g = uFlatColor.g * lightColor.r;\n" +
            "  gl_FragColor.b = uFlatColor.b * lightColor.r;\n" +
            "  gl_FragColor.a = uFlatColor.a;\n" +
            "}\n");
         this->add(srcFlatColorMesh_DirectionLight);
      }

// TransformedTexCoorMultiTexturedMesh
      {
         GPUProgramSources srcTransformedTexCoorMultiTexturedMesh(
            "TransformedTexCoorMultiTexturedMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "attribute vec2 aTextureCoord2;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "varying vec2 TextureCoordOut2;\n" +
            "uniform mediump vec2 uTranslationTexCoord;\n" +
            "uniform mediump vec2 uScaleTexCoord;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  //Transforming TextureCoordOut\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  TextureCoordOut2 = aTextureCoord2;\n" +
            "  \n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "varying mediump vec2 TextureCoordOut2;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform sampler2D Sampler2;\n" +
            "void main() {\n" +
            "  mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
            "  mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
            "  gl_FragColor = tex1 * tex2;\n" +
            "}\n");
         this->add(srcTransformedTexCoorMultiTexturedMesh);
      }

// TexturedMesh
      {
         GPUProgramSources srcTexturedMesh(
            "TexturedMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  TextureCoordOut = aTextureCoord;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "}\n");
         this->add(srcTexturedMesh);
      }

// Billboard_TransformedTexCoor
      {
         GPUProgramSources srcBillboard_TransformedTexCoor(
            "Billboard_TransformedTexCoor",
            emptyString +
            "attribute vec2 aTextureCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform vec4 uBillboardPosition;\n" +
            "uniform vec2 uBillboardAnchor; //Anchor in UV (texture-like) coordinates\n" +
            "uniform vec2 uTextureExtent;\n" +
            "uniform vec2 uViewPortExtent;\n" +
            "uniform mediump vec2 uTranslationTexCoord;\n" +
            "uniform mediump vec2 uScaleTexCoord;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * uBillboardPosition;\n" +
            "  \n" +
            "  float fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;\n" +
            "  float fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;\n" +
            "  \n" +
            "  gl_Position.x += ((aTextureCoord.x - 0.5) - (uBillboardAnchor.x - 0.5)) * fx;\n" +
            "  gl_Position.y -= ((aTextureCoord.y - 0.5) - (uBillboardAnchor.y - 0.5)) * fy;\n" +
            "  \n" +
            "  //Transformed Tex Coords applied to Billboard\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "}\n");
         this->add(srcBillboard_TransformedTexCoor);
      }

// FlatColor2DMesh
      {
         GPUProgramSources srcFlatColor2DMesh(
            "FlatColor2DMesh",
            emptyString +
            "attribute vec2 aPosition2D;\n" +
            "uniform float uPointSize;\n" +
            "uniform vec2 uTranslation2D;\n" +
            "uniform vec2 uViewPortExtent;\n" +
            "void main() {\n" +
            "  \n" +
            "  vec2 pixel = aPosition2D;\n" +
            "  pixel.x -= uViewPortExtent.x / 2.0;\n" +
            "  pixel.y += uViewPortExtent.y / 2.0;\n" +
            "  \n" +
            "  gl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),\n" +
            "                     (pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),\n" +
            "                     0, 1);\n" +
            "  \n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "uniform lowp vec4 uFlatColor;\n" +
            "void main() {\n" +
            "  gl_FragColor = uFlatColor;\n" +
            "}\n");
         this->add(srcFlatColor2DMesh);
      }

// MultiTexturedMesh
      {
         GPUProgramSources srcMultiTexturedMesh(
            "MultiTexturedMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "attribute vec2 aTextureCoord2;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "varying vec2 TextureCoordOut2;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  TextureCoordOut = aTextureCoord;\n" +
            "  TextureCoordOut2 = aTextureCoord2;\n" +
            "  \n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "varying mediump vec2 TextureCoordOut2;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform sampler2D Sampler2;\n" +
            "void main() {\n" +
            "  mediump vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
            "  mediump vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
            "  gl_FragColor = tex1 * tex2;\n" +
            "}\n");
         this->add(srcMultiTexturedMesh);
      }

// FullTransformedTexCoorTexturedMesh
      {
         GPUProgramSources srcFullTransformedTexCoorTexturedMesh(
            "FullTransformedTexCoorTexturedMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "uniform mediump vec2 uTranslationTexCoord;\n" +
            "uniform mediump vec2 uScaleTexCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "uniform float uRotationAngleTexCoord;\n" +
            "uniform vec2 uRotationCenterTexCoord;\n" +
            "varying vec4 VertexColor;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  float s = sin( uRotationAngleTexCoord );\n" +
            "  float c = cos( uRotationAngleTexCoord );\n" +
            "  //  vec2 textureCoord = aTextureCoord - uRotationCenterTexCoord;\n" +
            "  //\n" +
            "  //  vec2 newTextureCoord = vec2((textureCoord.x * c) + (textureCoord.y * s),\n" +
            "  //                              (-textureCoord.x * s) + (textureCoord.y * c));\n" +
            "  //\n" +
            "  //  newTextureCoord += uRotationCenterTexCoord;\n" +
            "  //\n" +
            "  //  TextureCoordOut = (newTextureCoord + uTranslationTexCoord) * uScaleTexCoord;\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;\n" +
            "  TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),\n" +
            "                         (-TextureCoordOut.x * s) + (TextureCoordOut.y * c));\n" +
            "  TextureCoordOut += uRotationCenterTexCoord;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "varying mediump vec4 VertexColor;\n" +
            "uniform sampler2D Sampler;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "}\n");
         this->add(srcFullTransformedTexCoorTexturedMesh);
      }

// TransformedTexCoorTexturedMesh_DirectionLight
      {
         GPUProgramSources srcTransformedTexCoorTexturedMesh_DirectionLight(
            "TransformedTexCoorTexturedMesh_DirectionLight",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "attribute vec3 aNormal;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform mat4 uModel;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform mediump vec2 uTranslationTexCoord;\n" +
            "uniform mediump vec2 uScaleTexCoord;\n" +
            "uniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED\n" +
            "varying float diffuseLightIntensity;\n" +
            "uniform vec3 uAmbientLightColor;\n" +
            "uniform vec3 uDiffuseLightColor;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
            "  vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
            "  float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "  //Computing Total Light in Vertex\n" +
            "  lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
            "  lightColor.x = min(lightColor.x, 1.0);\n" +
            "  lightColor.y = min(lightColor.y, 1.0);\n" +
            "  lightColor.z = min(lightColor.z, 1.0);\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying mediump vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  vec4 texColor = texture2D(Sampler, TextureCoordOut);\n" +
            "  gl_FragColor.r = texColor.r * lightColor.r;\n" +
            "  gl_FragColor.g = texColor.g * lightColor.r;\n" +
            "  gl_FragColor.b = texColor.b * lightColor.r;\n" +
            "  gl_FragColor.a = texColor.a;\n" +
            "}\n");
         this->add(srcTransformedTexCoorTexturedMesh_DirectionLight);
      }

// TexturedMesh_DirectionLight
      {
         GPUProgramSources srcTexturedMesh_DirectionLight(
            "TexturedMesh_DirectionLight",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "attribute vec3 aNormal;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform mat4 uModel;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED IN SHADER\n" +
            "varying float diffuseLightIntensity;\n" +
            "uniform vec3 uAmbientLightColor;\n" +
            "uniform vec3 uDiffuseLightColor;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
            "  vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
            "  \n" +
            "  float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  \n" +
            "  TextureCoordOut = aTextureCoord;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "  //Computing Total Light in Vertex\n" +
            "  lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
            "  lightColor.x = min(lightColor.x, 1.0);\n" +
            "  lightColor.y = min(lightColor.y, 1.0);\n" +
            "  lightColor.z = min(lightColor.z, 1.0);\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying mediump vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  vec4 texColor = texture2D(Sampler, TextureCoordOut);\n" +
            "  gl_FragColor.r = texColor.r * lightColor.r;\n" +
            "  gl_FragColor.g = texColor.g * lightColor.r;\n" +
            "  gl_FragColor.b = texColor.b * lightColor.r;\n" +
            "  gl_FragColor.a = texColor.a;\n" +
            "}\n");
         this->add(srcTexturedMesh_DirectionLight);
      }

// Shader
      {
         GPUProgramSources srcShader(
            "Shader",
            emptyString +
            "attribute vec4 Position;\n" +
            "attribute vec2 TextureCoord;\n" +
            "attribute vec4 Color;\n" +
            "uniform mediump vec2 TranslationTexCoord;\n" +
            "uniform mediump vec2 ScaleTexCoord;\n" +
            "uniform mat4 Projection;\n" +
            "uniform mat4 Modelview;\n" +
            "uniform bool BillBoard;\n" +
            "uniform vec2 TextureExtent;\n" +
            "uniform vec2 ViewPortExtent;\n" +
            "uniform float PointSize;\n" +
            "varying vec4 VertexColor;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = Projection * Modelview * Position;\n" +
            "  if (BillBoard) {\n" +
            "    gl_Position.x += ((TextureCoord.x - 0.5) * 2.0 * TextureExtent.x / ViewPortExtent.x) * gl_Position.w;\n" +
            "    gl_Position.y -= ((TextureCoord.y - 0.5) * 2.0 * TextureExtent.y / ViewPortExtent.y) * gl_Position.w;\n" +
            "  }\n" +
            "  TextureCoordOut = (TextureCoord * ScaleTexCoord) + TranslationTexCoord;\n" +
            "  VertexColor = Color;\n" +
            "  gl_PointSize = PointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "varying mediump vec4 VertexColor;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform bool EnableTexture;\n" +
            "uniform lowp vec4 FlatColor;\n" +
            "uniform bool EnableColorPerVertex;\n" +
            "uniform bool EnableFlatColor;\n" +
            "uniform mediump float FlatColorIntensity;\n" +
            "uniform mediump float ColorPerVertexIntensity;\n" +
            "void main() {\n" +
            "  \n" +
            "  if (EnableTexture) {\n" +
            "    gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "    if (EnableFlatColor || EnableColorPerVertex) {\n" +
            "      lowp vec4 color;\n" +
            "      if (EnableFlatColor) {\n" +
            "        color = FlatColor;\n" +
            "        if (EnableColorPerVertex) {\n" +
            "          color = color * VertexColor;\n" +
            "        }\n" +
            "      }\n" +
            "      else {\n" +
            "        color = VertexColor;\n" +
            "      }\n" +
            "      \n" +
            "      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\n" +
            "      gl_FragColor = mix(gl_FragColor,\n" +
            "                         VertexColor,\n" +
            "                         intensity);\n" +
            "    }\n" +
            "  }\n" +
            "  else {\n" +
            "    \n" +
            "    if (EnableColorPerVertex) {\n" +
            "      gl_FragColor = VertexColor;\n" +
            "      if (EnableFlatColor) {\n" +
            "        gl_FragColor = gl_FragColor * FlatColor;\n" +
            "      }\n" +
            "    }\n" +
            "    else {\n" +
            "      gl_FragColor = FlatColor;\n" +
            "    }\n" +
            "    \n" +
            "  }\n" +
            "  \n" +
            "}\n");
         this->add(srcShader);
      }

// NoColorMesh
      {
         GPUProgramSources srcNoColorMesh(
            "NoColorMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "void main() {\n" +
            "  gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); //RED\n" +
            "}\n");
         this->add(srcNoColorMesh);
      }

// Textured2DMesh
      {
         GPUProgramSources srcTextured2DMesh(
            "Textured2DMesh",
            emptyString +
            "attribute vec2 aPosition2D;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform vec2 uTranslation2D;\n" +
            "uniform vec2 uViewPortExtent;\n" +
            "void main() {\n" +
            "  \n" +
            "  vec2 pixel = aPosition2D;\n" +
            "  pixel.x -= uViewPortExtent.x / 2.0;\n" +
            "  pixel.y += uViewPortExtent.y / 2.0;\n" +
            "  \n" +
            "  \n" +
            "  gl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),\n" +
            "                     (pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),\n" +
            "                     0, 1);\n" +
            "  TextureCoordOut = aTextureCoord;\n" +
            "  \n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "  //gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0); //RED\n" +
            "}\n");
         this->add(srcTextured2DMesh);
      }

// SphericalAtmosphere
      {
         GPUProgramSources srcSphericalAtmosphere(
            "SphericalAtmosphere",
            emptyString +
            "attribute vec4 aPosition; //Position of ZNear Frame corners in world-space\n" +
            "uniform mat4 uModelview; //Model + Projection\n" +
            "uniform float uPointSize;\n" +
            "uniform highp vec3 uCameraPosition;\n" +
            "varying highp vec3 rayDirection;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  gl_Position.z = 0.0;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "  highp vec3 planePos = aPosition.xyz;\n" +
            "  //Ray [O + tD = X]\n" +
            "  rayDirection = planePos - uCameraPosition;\n" +
            "}\n",
            emptyString +
            "uniform highp vec3 uCameraPosition;\n" +
            "varying highp vec3 rayDirection;\n" +
            "const highp float earthRadius = 6.36744e6;\n" +
            "const highp float atmosphereScale = 15.0;\n" +
            "const highp float stratoHeight = 50e3 * atmosphereScale;\n" +
            "const highp float atmUndergroundOffset = 100e3;\n" +
            "const highp float minHeight = 35000.0;\n" +
            "const highp vec4 whiteSky = vec4(1.0, 1.0, 1.0, 1.0);\n" +
            "const highp vec4 blueSky = vec4(135.0 / 255.0,\n" +
            "                                206.0 / 255.0,\n" +
            "                                235.0 / 255.0,\n" +
            "                                1.0);\n" +
            "const highp vec4 darkSpace = vec4(0.0, 0.0, 0.0, 1.0);\n" +
            "const highp vec4 groundSkyColor = mix(blueSky, whiteSky, smoothstep(0.0, 1.0, 0.5));\n" +
            "bool intersectionsWithAtmosphere(highp vec3 o, highp vec3 d,\n" +
            "                                 out highp vec3 p1,\n" +
            "                                 out highp vec3 p2) {\n" +
            "  // http://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection\n" +
            "  highp float a = dot(d,d);\n" +
            "  highp float b = 2.0 * dot(o,d);\n" +
            "  highp float r = earthRadius - atmUndergroundOffset; //Earth radius\n" +
            "  highp float c = dot(o,o) - (r*r);\n" +
            "  highp float q1 = (b*b) - 4.0 * a * c;\n" +
            "  r = earthRadius + stratoHeight; //Atm. radius\n" +
            "  c = dot(o,o) - (r*r);\n" +
            "  highp float q2 = (b*b) - 4.0 * a * c;\n" +
            "  bool valid = (q1 < 0.0) && (q2 > 0.0);\n" +
            "  if (valid) {\n" +
            "    highp float sq = sqrt(q2);\n" +
            "    highp float t1 = (-b - sq) / (2.0*a);\n" +
            "    highp float t2 = (-b + sq) / (2.0*a);\n" +
            "    if (t1 < 0.0 && t2 < 0.0) {\n" +
            "      return false;\n" +
            "    }\n" +
            "    p1 = o + d * max(min(t1,t2), 0.0);\n" +
            "    p2 = o + d * max(t1,t2);\n" +
            "  }\n" +
            "  return valid;\n" +
            "}\n" +
            "highp float getRayFactor(highp vec3 o, highp vec3 d) {\n" +
            "  // Ray density calculations explained in: https://github.com/amazingsmash/AtmosphericShaders\n" +
            "  // Scaling the scene down to improve floating point calculations\n" +
            "  d /= 1000.0;\n" +
            "  o /= 1000.0;\n" +
            "  highp float er = earthRadius / 1000.0;\n" +
            "  highp float sh = (stratoHeight + earthRadius) / 1000.0;\n" +
            "  highp float ld = dot(d,d);\n" +
            "  highp float pdo = dot(d,o);\n" +
            "  highp float dx = d.x;\n" +
            "  highp float dy = d.y;\n" +
            "  highp float dz = d.z;\n" +
            "  highp float ox = o.x;\n" +
            "  highp float oy = o.y;\n" +
            "  highp float oz = o.z;\n" +
            "  highp float dox2 = (dx + ox) * (dx + ox);\n" +
            "  highp float doy2 = (dy + oy) * (dy + oy);\n" +
            "  highp float doz2 = (dz + oz) * (dz + oz);\n" +
            "  highp float ox2 = ox * ox;\n" +
            "  highp float oy2 = oy * oy;\n" +
            "  highp float oz2 = oz * oz;\n" +
            "  highp float dx2 = dx * dx;\n" +
            "  highp float dy2 = dy * dy;\n" +
            "  highp float dz2 = dz * dz;\n" +
            "  return ((((dx*(dx + ox) + dy*(dy + oy) + dz*(dz + oz))*\n" +
            "            sqrt(dox2 + doy2 + doz2))/ld -\n" +
            "           (sqrt(ox2 + oy2 + oz2)*pdo)/ld - 2.*sh +\n" +
            "           ((dz2*(ox2 + oy2) - 2.0*dx*dz*ox*oz - 2.0*dy*oy*(dx*ox + dz*oz) +\n" +
            "             dy2*(ox2 + oz2) + dx2*(oy2 + oz2))*\n" +
            "            log(dx*(dx + ox) + dy*(dy + oy) + dz*(dz + oz) +\n" +
            "                sqrt(ld)*sqrt(dox2 + doy2 + doz2)))/pow(ld,1.5) -\n" +
            "           ((dz2*(ox2 + oy2) - 2.0*dx*dz*ox*oz - 2.0*dy*oy*(dx*ox + dz*oz) +\n" +
            "             dy2*(ox2 + oz2) + dx2*(oy2 + oz2))*\n" +
            "            log(sqrt(ld)*sqrt(ox2 + oy2 + oz2) + pdo))/pow(ld,1.5))/\n" +
            "          (2.*(er - 1.*sh)));\n" +
            "}\n" +
            "void main() {\n" +
            "  //Ray [O + tD = X]\n" +
            "  highp vec3 sp1, sp2;\n" +
            "  bool valid = intersectionsWithAtmosphere(uCameraPosition, rayDirection, sp1, sp2);\n" +
            "  if (valid) {\n" +
            "    //Calculating color\n" +
            "    highp float f = getRayFactor(sp1, sp2 - sp1) * 1.3;\n" +
            "    highp vec4 color = mix(darkSpace, blueSky, smoothstep(0.0, 1.0, f));\n" +
            "    color = mix(color, whiteSky, smoothstep(0.7, 1.0, f));\n" +
            "    //Calculating camera Height (for precision problems)\n" +
            "    //Below a certain threshold float precision is not enough for calculations\n" +
            "    highp float camHeight = length(uCameraPosition) - earthRadius;\n" +
            "    gl_FragColor = mix(color, groundSkyColor, smoothstep(minHeight, minHeight / 4.0, camHeight));\n" +
            "  }\n" +
            "  else {\n" +
            "    gl_FragColor = darkSpace;\n" +
            "  }\n" +
            "}\n");
         this->add(srcSphericalAtmosphere);
      }

// FlatColorMesh
      {
         GPUProgramSources srcFlatColorMesh(
            "FlatColorMesh",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "uniform lowp vec4 uFlatColor;\n" +
            "void main() {\n" +
            "  gl_FragColor = uFlatColor;\n" +
            "}\n");
         this->add(srcFlatColorMesh);
      }

// Default
      {
         GPUProgramSources srcDefault(
            "Default",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "attribute vec4 aColor;\n" +
            "uniform mediump vec2 uTranslationTexCoord;\n" +
            "uniform mediump vec2 uScaleTexCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec4 VertexColor;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  \n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  \n" +
            "  VertexColor = aColor;\n" +
            "  \n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "varying mediump vec2 TextureCoordOut;\n" +
            "varying mediump vec4 VertexColor;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform bool EnableTexture;\n" +
            "uniform lowp vec4 uFlatColor;\n" +
            "uniform bool EnableColorPerVertex;\n" +
            "uniform bool EnableFlatColor;\n" +
            "uniform mediump float FlatColorIntensity;\n" +
            "uniform mediump float ColorPerVertexIntensity;\n" +
            "void main() {\n" +
            "  \n" +
            "  if (EnableTexture) {\n" +
            "    gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "    \n" +
            "    if (EnableFlatColor || EnableColorPerVertex) {\n" +
            "      lowp vec4 color;\n" +
            "      if (EnableFlatColor) {\n" +
            "        color = uFlatColor;\n" +
            "        if (EnableColorPerVertex) {\n" +
            "          color = color * VertexColor;\n" +
            "        }\n" +
            "      }\n" +
            "      else {\n" +
            "        color = VertexColor;\n" +
            "      }\n" +
            "      \n" +
            "      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\n" +
            "      gl_FragColor = mix(gl_FragColor,\n" +
            "                         VertexColor,\n" +
            "                         intensity);\n" +
            "    }\n" +
            "  }\n" +
            "  else {\n" +
            "    \n" +
            "    if (EnableColorPerVertex) {\n" +
            "      gl_FragColor = VertexColor;\n" +
            "      if (EnableFlatColor) {\n" +
            "        gl_FragColor = gl_FragColor * uFlatColor;\n" +
            "      }\n" +
            "    }\n" +
            "    else {\n" +
            "      gl_FragColor = uFlatColor;\n" +
            "    }\n" +
            "    \n" +
            "  }\n" +
            "  \n" +
            "}\n");
         this->add(srcDefault);
      }

// Billboard
      {
         GPUProgramSources srcBillboard(
            "Billboard",
            emptyString +
            "attribute vec2 aTextureCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform vec4 uBillboardPosition;\n" +
            "uniform vec2 uBillboardAnchor; //Anchor in UV (texture-like) coordinates\n" +
            "uniform vec2 uTextureExtent;\n" +
            "uniform vec2 uViewPortExtent;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * uBillboardPosition;\n" +
            "  \n" +
            "  float fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;\n" +
            "  float fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;\n" +
            "  \n" +
            "  gl_Position.x += ((aTextureCoord.x - 0.5) - (uBillboardAnchor.x - 0.5)) * fx;\n" +
            "  gl_Position.y -= ((aTextureCoord.y - 0.5) - (uBillboardAnchor.y - 0.5)) * fy;\n" +
            "  \n" +
            "  TextureCoordOut = aTextureCoord;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying mediump vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "}\n");
         this->add(srcBillboard);
      }

  }

};

#endif
