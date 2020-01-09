

#include "NativeGL_Emscripten.hpp"

#include "FloatBuffer_Emscripten.hpp"

using namespace emscripten;


NativeGL_Emscripten::NativeGL_Emscripten(const emscripten::val& gl) :
_gl(gl),
GL_FLOAT( gl["FLOAT"].as<unsigned long>() )
{

}

void NativeGL_Emscripten::useProgram(GPUProgram* program) const {
#error TODO
}

void NativeGL_Emscripten::uniform2f(const IGLUniformID* loc,
                                    float x,
                                    float y) const {
#error TODO
}

void NativeGL_Emscripten::uniform1f(const IGLUniformID* loc,
                                    float x) const {
#error TODO
}

void NativeGL_Emscripten::uniform1i(const IGLUniformID* loc,
                                    int v) const {
#error TODO
}

void NativeGL_Emscripten::uniformMatrix4fv(const IGLUniformID* location,
                                           bool transpose,
                                           const Matrix44D* matrix) const {
#error TODO
}

void NativeGL_Emscripten::clearColor(float red,
                                     float green,
                                     float blue,
                                     float alpha) const {
#error TODO
}

void NativeGL_Emscripten::clear(int buffers) const {
#error TODO
}

void NativeGL_Emscripten::uniform4f(const IGLUniformID* location,
                                    float v0,
                                    float v1,
                                    float v2,
                                    float v3) const {
#error TODO
}

void NativeGL_Emscripten::uniform3f(const IGLUniformID* location,
                                    float v0,
                                    float v1,
                                    float v2) const {
#error TODO
}

void NativeGL_Emscripten::enable(int feature) const {
#error TODO
}

void NativeGL_Emscripten::disable(int feature) const {
#error TODO
}

void NativeGL_Emscripten::polygonOffset(float factor,
                                        float units) const {
#error TODO
}

void NativeGL_Emscripten::vertexAttribPointer(int index,
                                              int size,
                                              bool normalized,
                                              int stride,
                                              const IFloatBuffer* buffer) const {
  val webGLBuffer = ((FloatBuffer_Emscripten*) buffer)->bindVBO(_gl);

  _gl.call<void>("vertexAttribPointer", index, size, GL_FLOAT, normalized, stride, 0);
}

void NativeGL_Emscripten::drawElements(int mode,
                                       int count,
                                       IShortBuffer* indices) const {
#error TODO
}

void NativeGL_Emscripten::lineWidth(float width) const {
#error TODO
}

int NativeGL_Emscripten::getError() const {
#error TODO
}

void NativeGL_Emscripten::blendFunc(int sfactor,
                                    int dfactor) const {
#error TODO
}

void NativeGL_Emscripten::bindTexture(int target,
                                      const IGLTextureID* texture) const {
#error TODO
}

/* Delete Texture from GPU, and answer if the TextureID can be reused */
bool NativeGL_Emscripten::deleteTexture(const IGLTextureID* texture) const {
#error TODO
}

void NativeGL_Emscripten::enableVertexAttribArray(int location) const {
#error TODO
}

void NativeGL_Emscripten::disableVertexAttribArray(int location) const {
#error TODO
}

void NativeGL_Emscripten::pixelStorei(int pname,
                                      int param) const {
#error TODO
}

std::vector<IGLTextureID*> NativeGL_Emscripten::genTextures(int  n) const {
#error TODO
}

void NativeGL_Emscripten::texParameteri(int target,
                                        int par,
                                        int v) const {
#error TODO
}

void NativeGL_Emscripten::texImage2D(const IImage* image, int format) const {
#error TODO
}

void gNativeGL_Emscripten::enerateMipmap(int target) const {
#error TODO
}

void NativeGL_Emscripten::drawArrays(int mode,
                                     int first,
                                     int count) const {
#error TODO
}

void NativeGL_Emscripten::cullFace(int c) const {
#error TODO
}

void NativeGL_Emscripten::getIntegerv(int v, int i[]) const {
#error TODO
}


int CullFace_Front() const {
#error TODO
}
int CullFace_Back() const {
#error TODO
}
int CullFace_FrontAndBack() const {
#error TODO
}

int BufferType_ColorBuffer() const {
#error TODO
}
int BufferType_DepthBuffer() const {
#error TODO
}

int Feature_PolygonOffsetFill() const {
#error TODO
}
int Feature_DepthTest() const {
#error TODO
}
int Feature_Blend() const {
#error TODO
}
int Feature_CullFace() const {
#error TODO
}

int Type_Float() const {
#error TODO
}
int Type_UnsignedByte() const {
#error TODO
}
int Type_UnsignedInt() const {
#error TODO
}
int Type_Int() const {
#error TODO
}
int Type_Vec2Float() const {
#error TODO
}
int Type_Vec3Float() const {
#error TODO
}
int Type_Vec4Float() const {
#error TODO
}
int Type_Bool() const {
#error TODO
}
int Type_Matrix4Float() const {
#error TODO
}

int Primitive_Triangles() const {
#error TODO
}
int Primitive_TriangleStrip() const {
#error TODO
}
int Primitive_TriangleFan() const {
#error TODO
}
int Primitive_Lines() const {
#error TODO
}
int Primitive_LineStrip() const {
#error TODO
}
int Primitive_LineLoop() const {
#error TODO
}
int Primitive_Points() const {
#error TODO
}

int BlendFactor_One() const {
#error TODO
}
int BlendFactor_Zero() const {
#error TODO
}
int BlendFactor_SrcAlpha() const {
#error TODO
}
int BlendFactor_OneMinusSrcAlpha() const {
#error TODO
}

int TextureType_Texture2D() const {
#error TODO
}

int TextureParameter_MinFilter() const {
#error TODO
}
int TextureParameter_MagFilter() const {
#error TODO
}
int TextureParameter_WrapS() const {
#error TODO
}
int TextureParameter_WrapT() const {
#error TODO
}

int TextureParameterValue_Nearest() const {
#error TODO
}
int TextureParameterValue_Linear() const {
#error TODO
}
int TextureParameterValue_NearestMipmapNearest() const {
#error TODO
}
int TextureParameterValue_NearestMipmapLinear() const {
#error TODO
}
int TextureParameterValue_LinearMipmapNearest() const {
#error TODO
}
int TextureParameterValue_LinearMipmapLinear() const {
#error TODO
}

/* TextureWrapMode */
int TextureParameterValue_Repeat() const {
#error TODO
}
int TextureParameterValue_ClampToEdge() const {
#error TODO
}
int TextureParameterValue_MirroredRepeat() const {
#error TODO
}

int Alignment_Pack() const {
#error TODO
}
int Alignment_Unpack() const {
#error TODO
}

int Format_RGBA() const {
#error TODO
}

int Variable_Viewport() const {
#error TODO
}
int Variable_ActiveAttributes() const {
#error TODO
}
int Variable_ActiveUniforms() const {
#error TODO
}

int Error_NoError() const {
#error TODO
}

int createProgram() const {
#error TODO
}
bool deleteProgram(int program) const {
#error TODO
}
void attachShader(int program, int shader) const {
#error TODO
}
int createShader(ShaderType type) const {
#error TODO
}
bool compileShader (int shader, const std::string& source) const {
#error TODO
}
bool deleteShader(int shader) const {
#error TODO
}
void printShaderInfoLog(int shader) const {
#error TODO
}
bool linkProgram(int program) const {
#error TODO
}
void printProgramInfoLog(int program) const {
#error TODO
}

void bindAttribLocation(const GPUProgram* program, int loc, const std::string& name) const {
#error TODO
}

int getProgramiv(const GPUProgram* program, int param) const {
#error TODO
}

GPUUniform* getActiveUniform(const GPUProgram* program, int i) const {
#error TODO
}
GPUAttribute* getActiveAttribute(const GPUProgram* program, int i) const {
#error TODO
}

void depthMask(bool v) const {
#error TODO
}

void setActiveTexture(int i) const {
#error TODO
}

void viewport(int x, int y, int width, int height) const {
#error TODO
}
