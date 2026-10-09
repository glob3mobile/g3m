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

// RibbonMesh
      {
         GPUProgramSources srcRibbonMesh(
            "RibbonMesh",
            emptyString +
            "attribute vec4 aPosition;   // ribbon center line\n" +
            "attribute vec3 aRibbonSide; // unit side vector, +1/-1 per ribbon edge\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "uniform vec2 uViewPortExtent;\n" +
            "uniform vec2 uRibbonWidth; // x = width in meters, y = minimum width in pixels\n" +
            "void main() {\n" +
            "  vec4 center = uModelview * aPosition;\n" +
            "  vec4 side   = uModelview * (aPosition + vec4(aRibbonSide * (uRibbonWidth.x * 0.5), 0.0));\n" +
            "  if ((center.w <= 0.0) || (side.w <= 0.0)) {\n" +
            "    // behind the camera: perspective division is meaningless, keep the plain meters offset\n" +
            "    gl_Position = side;\n" +
            "  }\n" +
            "  else {\n" +
            "    vec2 halfViewport = uViewPortExtent * 0.5;\n" +
            "    vec2 dirPx = (side.xy / side.w - center.xy / center.w) * halfViewport;\n" +
            "    float metersPx = length(dirPx);\n" +
            "    gl_Position = center;\n" +
            "    if (metersPx > 0.0) {\n" +
            "      float halfPx = max(metersPx, uRibbonWidth.y * 0.5);\n" +
            "      vec2 offsetNDC = (dirPx / metersPx) * halfPx / halfViewport;\n" +
            "      gl_Position.xy += offsetNDC * center.w;\n" +
            "    }\n" +
            "  }\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "uniform vec4 uFlatColor;\n" +
            "void main() {\n" +
            "  gl_FragColor = uFlatColor;\n" +
            "}\n");
         this->add(srcRibbonMesh);
      }

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
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4 VertexColor;\n" +
            "void main() {\n" +
            "  gl_FragColor = VertexColor;\n" +
            "}\n");
         this->add(srcColorMesh);
      }

// TransformedTexCoorTexturedMesh_ColorGrade
      {
         GPUProgramSources srcTransformedTexCoorTexturedMesh_ColorGrade(
            "TransformedTexCoorTexturedMesh_ColorGrade",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec2 aTextureCoord;\n" +
            "uniform vec2 uTranslationTexCoord;\n" +
            "uniform vec2 uScaleTexCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform mat4 uColorMatrix;\n" +
            "void main() {\n" +
            "  vec4 color = texture2D(Sampler, TextureCoordOut);\n" +
            "  vec3 gradedColor = (uColorMatrix * vec4(color.rgb, 1.0)).rgb;\n" +
            "  gl_FragColor = vec4(clamp(gradedColor, 0.0, 1.0), color.a);\n" +
            "}\n");
         this->add(srcTransformedTexCoorTexturedMesh_ColorGrade);
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
            "uniform vec2 uTranslationTexCoord;\n" +
            "uniform vec2 uScaleTexCoord;\n" +
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
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "varying vec2 TextureCoordOut2;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform sampler2D Sampler2;\n" +
            "void main() {\n" +
            "  vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
            "  vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
            "  \n" +
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
            "uniform vec2 uTranslationTexCoord;\n" +
            "uniform vec2 uScaleTexCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
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
            "  float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "  //Computing Total Light in Vertex\n" +
            "  lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
            "  lightColor = clamp(lightColor, 0.0, 1.0);\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "uniform vec4 uFlatColor;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  gl_FragColor.rgb = uFlatColor.rgb * lightColor.rgb;\n" +
            "  gl_FragColor.a   = uFlatColor.a;\n" +
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
            "uniform vec2 uTranslationTexCoord;\n" +
            "uniform vec2 uScaleTexCoord;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  //Transforming TextureCoordOut\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  TextureCoordOut2 = aTextureCoord2;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "varying vec2 TextureCoordOut2;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform sampler2D Sampler2;\n" +
            "void main() {\n" +
            "  vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
            "  vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
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
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
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
            "uniform vec2 uBillboardAnchor; // Anchor in UV (texture-like) coordinates\n" +
            "uniform vec2 uTextureExtent;\n" +
            "uniform vec2 uViewPortExtent;\n" +
            "uniform vec2 uTranslationTexCoord;\n" +
            "uniform vec2 uScaleTexCoord;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * uBillboardPosition;\n" +
            "  float fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;\n" +
            "  float fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;\n" +
            "  gl_Position.x += (aTextureCoord.x - uBillboardAnchor.x) * fx;\n" +
            "  gl_Position.y -= (aTextureCoord.y - uBillboardAnchor.y) * fy;\n" +
            "  //Transformed Tex Coords applied to Billboard\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform vec4 uBillboardColorFactor;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut) * uBillboardColorFactor;\n" +
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
            "  vec2 pixel = aPosition2D;\n" +
            "  pixel.x -= uViewPortExtent.x / 2.0;\n" +
            "  pixel.y += uViewPortExtent.y / 2.0;\n" +
            "  gl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),\n" +
            "                     (pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),\n" +
            "                     0, 1);\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "uniform vec4 uFlatColor;\n" +
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
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "varying vec2 TextureCoordOut2;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform sampler2D Sampler2;\n" +
            "void main() {\n" +
            "  vec4 tex1 = texture2D(Sampler, TextureCoordOut);\n" +
            "  vec4 tex2 = texture2D(Sampler2, TextureCoordOut2);\n" +
            "  \n" +
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
            "uniform vec2 uTranslationTexCoord;\n" +
            "uniform vec2 uScaleTexCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "uniform float uRotationAngleTexCoord;\n" +
            "uniform vec2 uRotationCenterTexCoord;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  float s = sin( uRotationAngleTexCoord );\n" +
            "  float c = cos( uRotationAngleTexCoord );\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  TextureCoordOut = TextureCoordOut - uRotationCenterTexCoord;\n" +
            "  TextureCoordOut = vec2((TextureCoordOut.x * c) + (TextureCoordOut.y * s),\n" +
            "                         (-TextureCoordOut.x * s) + (TextureCoordOut.y * c));\n" +
            "  TextureCoordOut += uRotationCenterTexCoord;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
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
            "uniform vec2 uTranslationTexCoord;\n" +
            "uniform vec2 uScaleTexCoord;\n" +
            "uniform vec3 uDiffuseLightDirection; //MUST BE NORMALIZED\n" +
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
            "  lightColor = clamp(lightColor, 0.0, 1.0);\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  vec4 texColor = texture2D(Sampler, TextureCoordOut);\n" +
            "  gl_FragColor.rgb = texColor.rgb * lightColor.rgb;\n" +
            "  gl_FragColor.a   = texColor.a;\n" +
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
            "uniform vec3 uAmbientLightColor;\n" +
            "uniform vec3 uDiffuseLightColor;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  vec3 normalInModel = normalize( vec3(uModel * vec4(aNormal, 0.0) ));\n" +
            "  vec3 lightDirNormalized = normalize( uDiffuseLightDirection );\n" +
            "  float diffuseLightIntensity = max(dot(normalInModel, lightDirNormalized), 0.0);\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  TextureCoordOut = aTextureCoord;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "  //Computing Total Light in Vertex\n" +
            "  lightColor = uAmbientLightColor + uDiffuseLightColor * diffuseLightIntensity;\n" +
            "  lightColor = clamp(lightColor, 0.0, 1.0);\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "varying vec3 lightColor;\n" +
            "void main() {\n" +
            "  vec4 texColor = texture2D(Sampler, TextureCoordOut);\n" +
            "  gl_FragColor.rgb = texColor.rgb * lightColor.rgb;\n" +
            "  gl_FragColor.a   = texColor.a;\n" +
            "}\n");
         this->add(srcTexturedMesh_DirectionLight);
      }

// TexturedMesh_ColorGrade
      {
         GPUProgramSources srcTexturedMesh_ColorGrade(
            "TexturedMesh_ColorGrade",
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
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform mat4 uColorMatrix;\n" +
            "void main() {\n" +
            "  vec4 color = texture2D(Sampler, TextureCoordOut);\n" +
            "  vec3 gradedColor = (uColorMatrix * vec4(color.rgb, 1.0)).rgb;\n" +
            "  gl_FragColor = vec4(clamp(gradedColor, 0.0, 1.0), color.a);\n" +
            "}\n");
         this->add(srcTexturedMesh_ColorGrade);
      }

// Shader
      {
         GPUProgramSources srcShader(
            "Shader",
            emptyString +
            "attribute vec4 Position;\n" +
            "attribute vec2 TextureCoord;\n" +
            "attribute vec4 Color;\n" +
            "uniform vec2 TranslationTexCoord;\n" +
            "uniform vec2 ScaleTexCoord;\n" +
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
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "varying vec4 VertexColor;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform bool EnableTexture;\n" +
            "uniform vec4 FlatColor;\n" +
            "uniform bool EnableColorPerVertex;\n" +
            "uniform bool EnableFlatColor;\n" +
            "uniform float FlatColorIntensity;\n" +
            "uniform float ColorPerVertexIntensity;\n" +
            "void main() {\n" +
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
            "      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\n" +
            "      gl_FragColor = mix(gl_FragColor,\n" +
            "                         VertexColor,\n" +
            "                         intensity);\n" +
            "    }\n" +
            "  }\n" +
            "  else {\n" +
            "    if (EnableColorPerVertex) {\n" +
            "      gl_FragColor = VertexColor;\n" +
            "      if (EnableFlatColor) {\n" +
            "        gl_FragColor = gl_FragColor * FlatColor;\n" +
            "      }\n" +
            "    }\n" +
            "    else {\n" +
            "      gl_FragColor = FlatColor;\n" +
            "    }\n" +
            "  }\n" +
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
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
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
            "  vec2 pixel = aPosition2D;\n" +
            "  pixel.x -= uViewPortExtent.x / 2.0;\n" +
            "  pixel.y += uViewPortExtent.y / 2.0;\n" +
            "  \n" +
            "  gl_Position = vec4((pixel.x + uTranslation2D.x) / (uViewPortExtent.x / 2.0),\n" +
            "                     (pixel.y - uTranslation2D.y) / (uViewPortExtent.y / 2.0),\n" +
            "                     0, 1);\n" +
            "  \n" +
            "  TextureCoordOut = aTextureCoord;\n" +
            "  \n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
            "}\n");
         this->add(srcTextured2DMesh);
      }

// SphericalAtmosphere
      {
         GPUProgramSources srcSphericalAtmosphere(
            "SphericalAtmosphere",
            emptyString +
            "attribute vec4 aPosition; //Position of ZNear Frame corners relative to the camera\n" +
            "uniform mat4 uModelview; //Model + Projection\n" +
            "uniform float uPointSize;\n" +
            "uniform vec3 uCameraPosition;\n" +
            "varying vec3 rayDirection;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  gl_Position.z = 0.0;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "  //Ray [O + tD = X]\n" +
            "  rayDirection = aPosition.xyz;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "uniform vec3 uCameraPosition;\n" +
            "uniform float uGroundHazePass;\n" +
            "uniform vec3 uSpaceColor;\n" +
            "varying vec3 rayDirection;\n" +
            "const float earthRadius = 6.36744e6;\n" +
            "const vec3 earthRadii = vec3(6378137.0, 6378137.0, 6356752.314245);\n" +
            "const float realScaleHeight = 8.0;\n" +
            "const float atmosphereScale = 2.0;\n" +
            "const float skyScaleHeight = realScaleHeight * atmosphereScale;\n" +
            "const float stratoHeight = 12.0 * skyScaleHeight * 1000.0;\n" +
            "const float atmUndergroundOffset = 100e3;\n" +
            "const vec3 rayleighScattering = vec3(5.802, 13.558, 33.1) * 1e-3;\n" +
            "const float skyRayleighScatteringScale = 1.78;\n" +
            "const vec3 horizonColor = vec3(201.0, 227.0, 242.0) / 255.0;\n" +
            "const int opticalDepthSamples = 16;\n" +
            "const vec4 noAir = vec4(0.0, 0.0, 0.0, 0.0);\n" +
            "bool rayIntersectsSphere(vec3 o, vec3 d, float radius,\n" +
            "                         out float tNear,\n" +
            "                         out float tFar) {\n" +
            "  // http://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection\n" +
            "  float a = dot(d,d);\n" +
            "  float b = 2.0 * dot(o,d);\n" +
            "  float c = dot(o,o) - (radius*radius);\n" +
            "  float q = (b*b) - 4.0 * a * c;\n" +
            "  if (q <= 0.0) {\n" +
            "    return false;\n" +
            "  }\n" +
            "  float sq = sqrt(q);\n" +
            "  tNear = (-b - sq) / (2.0*a);\n" +
            "  tFar  = (-b + sq) / (2.0*a);\n" +
            "  return true;\n" +
            "}\n" +
            "bool rayHitsGround(vec3 o, vec3 d, out float tGround) {\n" +
            "  // the ellipsoid is the unit sphere once the space is divided by its radii\n" +
            "  float tFar;\n" +
            "  return rayIntersectsSphere(o / earthRadii, d / earthRadii, 1.0, tGround, tFar) && (tGround > 0.0);\n" +
            "}\n" +
            "float groundDistanceAlongRay(vec3 o, vec3 d) {\n" +
            "  float tGround;\n" +
            "  if (rayHitsGround(o, d, tGround)) {\n" +
            "    return tGround;\n" +
            "  }\n" +
            "  vec3 oInUnitSphere = o / earthRadii;\n" +
            "  vec3 dInUnitSphere = d / earthRadii;\n" +
            "  return -dot(oInUnitSphere, dInUnitSphere) / dot(dInUnitSphere, dInUnitSphere);\n" +
            "}\n" +
            "float airDensity(vec3 point, float scaleHeight) {\n" +
            "  float heightInKm = (length(point) - earthRadius) / 1000.0;\n" +
            "  return exp(-heightInKm / scaleHeight);\n" +
            "}\n" +
            "float opticalDepthInAtmosphere(vec3 p1, vec3 p2, float scaleHeight) {\n" +
            "  vec3 sampleStep = (p2 - p1) / float(opticalDepthSamples);\n" +
            "  float densitySum = 0.0;\n" +
            "  for (int i = 0; i < opticalDepthSamples; i++) {\n" +
            "    densitySum += airDensity(p1 + sampleStep * (float(i) + 0.5), scaleHeight);\n" +
            "  }\n" +
            "  return densitySum * length(sampleStep) / 1000.0;\n" +
            "}\n" +
            "float opticalDepthAbove(vec3 point, float scaleHeight) {\n" +
            "  return scaleHeight * airDensity(point, scaleHeight);\n" +
            "}\n" +
            "vec3 skyExtinction(float opticalDepth) {\n" +
            "  return rayleighScattering * skyRayleighScatteringScale * opticalDepth;\n" +
            "}\n" +
            "vec3 hazeExtinction(float opticalDepth) {\n" +
            "  return rayleighScattering * opticalDepth;\n" +
            "}\n" +
            "vec3 transmittance(vec3 airExtinction) {\n" +
            "  return exp(-airExtinction);\n" +
            "}\n" +
            "vec3 scatteredLight(vec3 airExtinction) {\n" +
            "  return horizonColor * (vec3(1.0) - exp(-airExtinction / horizonColor));\n" +
            "}\n" +
            "float screenNoise() {\n" +
            "  return fract(52.9829189 * fract(dot(gl_FragCoord.xy, vec2(0.06711056, 0.00583715))));\n" +
            "}\n" +
            "vec3 dithered(vec3 light) {\n" +
            "  return light + (screenNoise() - 0.5) / 255.0;\n" +
            "}\n" +
            "vec4 sky(vec3 o, vec3 d) {\n" +
            "  float tAtmosphereIn, tAtmosphereOut;\n" +
            "  if (!rayIntersectsSphere(o, d, earthRadius + stratoHeight, tAtmosphereIn, tAtmosphereOut) || (tAtmosphereOut <= 0.0)) {\n" +
            "    return noAir;\n" +
            "  }\n" +
            "  float tUnderground, tUndergroundFar;\n" +
            "  if (rayIntersectsSphere(o, d, earthRadius - atmUndergroundOffset, tUnderground, tUndergroundFar) && (tUnderground > 0.0)) {\n" +
            "    return vec4(uSpaceColor, 1.0);\n" +
            "  }\n" +
            "  float opticalDepth = opticalDepthInAtmosphere(o + d * max(tAtmosphereIn, 0.0), o + d * tAtmosphereOut, skyScaleHeight);\n" +
            "  vec3 airExtinction = skyExtinction(opticalDepth);\n" +
            "  vec3 light = dithered(scatteredLight(airExtinction));\n" +
            "  // opaque where the planet is behind, so no star shows where the tiles fall short of the ellipsoid\n" +
            "  float tGround;\n" +
            "  if (rayHitsGround(o, d, tGround)) {\n" +
            "    return vec4(light, 1.0);\n" +
            "  }\n" +
            "  // blended with one / oneMinusSrcAlpha: light + background * transmittance (one alpha, the transmittance of green)\n" +
            "  return vec4(light, 1.0 - transmittance(airExtinction).g);\n" +
            "}\n" +
            "vec4 groundHaze(vec3 o, vec3 d) {\n" +
            "  float tAtmosphereIn, tAtmosphereOut;\n" +
            "  if (!rayIntersectsSphere(o, d, earthRadius + stratoHeight, tAtmosphereIn, tAtmosphereOut)) {\n" +
            "    return noAir;\n" +
            "  }\n" +
            "  float tGround = groundDistanceAlongRay(o, d);\n" +
            "  float tStart = max(tAtmosphereIn, 0.0);\n" +
            "  if (tGround <= tStart) {\n" +
            "    return noAir;\n" +
            "  }\n" +
            "  // only the air beyond the vertical column above the ground point hazes it, so looking down is not veiled\n" +
            "  vec3 groundPoint = o + d * tGround;\n" +
            "  float opticalDepth = max(opticalDepthInAtmosphere(o + d * tStart, groundPoint, realScaleHeight) - opticalDepthAbove(groundPoint, realScaleHeight), 0.0);\n" +
            "  float opacity = 1.0 - transmittance(hazeExtinction(opticalDepth)).g;\n" +
            "  if (opacity <= 0.0) {\n" +
            "    return noAir;\n" +
            "  }\n" +
            "  return vec4(horizonColor, opacity);\n" +
            "}\n" +
            "void main() {\n" +
            "  //Ray [O + tD = X]\n" +
            "  vec3 o = uCameraPosition;\n" +
            "  vec3 d = normalize(rayDirection);\n" +
            "  if (uGroundHazePass > 0.5) {\n" +
            "    gl_FragColor = groundHaze(o, d);\n" +
            "  }\n" +
            "  else {\n" +
            "    gl_FragColor = sky(o, d);\n" +
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
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "uniform vec4 uFlatColor;\n" +
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
            "uniform vec2 uTranslationTexCoord;\n" +
            "uniform vec2 uScaleTexCoord;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "varying vec4 VertexColor;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  TextureCoordOut = (aTextureCoord * uScaleTexCoord) + uTranslationTexCoord;\n" +
            "  VertexColor = aColor;\n" +
            "  gl_PointSize = uPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "varying vec4 VertexColor;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform bool EnableTexture;\n" +
            "uniform lowp vec4 uFlatColor;\n" +
            "uniform bool EnableColorPerVertex;\n" +
            "uniform bool EnableFlatColor;\n" +
            "uniform float FlatColorIntensity;\n" +
            "uniform float ColorPerVertexIntensity;\n" +
            "void main() {\n" +
            "  if (EnableTexture) {\n" +
            "    gl_FragColor = texture2D(Sampler, TextureCoordOut);\n" +
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
            "      lowp float intensity = (FlatColorIntensity + ColorPerVertexIntensity) / 2.0;\n" +
            "      gl_FragColor = mix(gl_FragColor,\n" +
            "                         VertexColor,\n" +
            "                         intensity);\n" +
            "    }\n" +
            "  }\n" +
            "  else {\n" +
            "    if (EnableColorPerVertex) {\n" +
            "      gl_FragColor = VertexColor;\n" +
            "      if (EnableFlatColor) {\n" +
            "        gl_FragColor = gl_FragColor * uFlatColor;\n" +
            "      }\n" +
            "    }\n" +
            "    else {\n" +
            "      gl_FragColor = uFlatColor;\n" +
            "    }\n" +
            "  }\n" +
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
            "uniform vec2 uBillboardAnchor; // Anchor in UV (texture-like) coordinates\n" +
            "uniform vec2 uTextureExtent;\n" +
            "uniform vec2 uViewPortExtent;\n" +
            "varying vec2 TextureCoordOut;\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * uBillboardPosition;\n" +
            "  float fx = 2.0 * uTextureExtent.x / uViewPortExtent.x * gl_Position.w;\n" +
            "  float fy = 2.0 * uTextureExtent.y / uViewPortExtent.y * gl_Position.w;\n" +
            "  gl_Position.x += (aTextureCoord.x - uBillboardAnchor.x) * fx;\n" +
            "  gl_Position.y -= (aTextureCoord.y - uBillboardAnchor.y) * fy;\n" +
            "  TextureCoordOut = aTextureCoord;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec2 TextureCoordOut;\n" +
            "uniform sampler2D Sampler;\n" +
            "uniform vec4 uBillboardColorFactor;\n" +
            "void main() {\n" +
            "  gl_FragColor = texture2D(Sampler, TextureCoordOut) * uBillboardColorFactor;\n" +
            "}\n");
         this->add(srcBillboard);
      }

// Stars
      {
         GPUProgramSources srcStars(
            "Stars",
            emptyString +
            "attribute vec4 aPosition;\n" +
            "attribute vec4 aColor;\n" +
            "uniform mat4 uModelview;\n" +
            "uniform float uPointSize;\n" +
            "uniform float uFullStarMagnitude;\n" +
            "uniform float uStarSizeExponent;\n" +
            "varying vec4  StarColor;\n" +
            "varying float StarPointSize;\n" +
            "const float colourVisionMagnitude = 1.0;\n" +
            "float lightOfMagnitude(float magnitude, float referenceMagnitude) {\n" +
            "  return pow(10.0, 0.4 * (referenceMagnitude - magnitude));\n" +
            "}\n" +
            "void main() {\n" +
            "  gl_Position = uModelview * aPosition;\n" +
            "  float magnitude = aColor.a;\n" +
            "  float light = lightOfMagnitude(magnitude, uFullStarMagnitude);\n" +
            "  float colourSaturation = min(lightOfMagnitude(magnitude, colourVisionMagnitude), 1.0);\n" +
            "  vec3 colour = mix(vec3(1.0), aColor.rgb, colourSaturation);\n" +
            "  StarColor = vec4(colour, min(light, 1.0));\n" +
            "  StarPointSize = uPointSize * pow(max(light, 1.0), uStarSizeExponent);\n" +
            "  gl_PointSize = StarPointSize;\n" +
            "}\n",
            emptyString +
            "#ifdef GL_FRAGMENT_PRECISION_HIGH\n" +
            "precision highp float;\n" +
            "#else\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "varying vec4  StarColor;\n" +
            "varying float StarPointSize;\n" +
            "void main() {\n" +
            "  // a round disc with a soft edge one pixel wide\n" +
            "  float distanceToCentreInPixels = length(gl_PointCoord - vec2(0.5)) * StarPointSize;\n" +
            "  float coverage = clamp(StarPointSize / 2.0 - distanceToCentreInPixels + 0.5, 0.0, 1.0);\n" +
            "  gl_FragColor = vec4(StarColor.rgb, StarColor.a * coverage);\n" +
            "}\n");
         this->add(srcStars);
      }

  }

};

#endif
