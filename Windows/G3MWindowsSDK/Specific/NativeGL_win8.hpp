//
//  NativeGL_win8.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 04/06/14.
// 
//

#ifndef __G3MWindowsSDK_NativeGL_win8__
#define __G3MWindowsSDK_NativeGL_win8__

#include <wrl/client.h>
#include <d3d11_2.h>

#include "INativeGL.hpp"




#define DEPTH_BUFFER_BIT 1
#define COLOR_BUFFER_BIT 2

#define FEATURE_POLYGONOFFSETFILL 1
#define FEATURE_DEPTHTEST 2
#define FEATURE_BLEND 3
#define FEATURE_CULLFACE 4

#define TYPE_FLOAT 1
#define TYPE_UBYTE 2
#define TYPE_UINT 4
#define TYPE_INT 8
#define TYPE_VEC2FLOAT 16
#define TYPE_VEC3FLOAT 32
#define TYPE_VEC4FLOAT 64
#define TYPE_MAT4FLOAT 128
#define TYPE_BOOL 256

#define TEXTURETYPE2D 1




class NativeGL_win8 :public INativeGL {
private:
	Microsoft::WRL::ComPtr<ID3D11Device1> _device;
	Microsoft::WRL::ComPtr<ID3D11DeviceContext1> _deviceContext;
	Microsoft::WRL::ComPtr<ID3D11RenderTargetView> _renderTargetView;
	Microsoft::WRL::ComPtr<ID3D11DepthStencilView> _depthStencilView;

	mutable ID3D11Buffer* _indexBuffer = NULL;
	mutable int _indexBufferSize = 0;


	mutable ID3D11RasterizerState1* _rasterizerState;
	mutable D3D11_RASTERIZER_DESC1 _rasterizerDesc;
	mutable bool _rasterizerStateChanged = true;

	mutable D3D11_DEPTH_STENCIL_DESC _depthStencilDesc;
	mutable bool _depthStencilStateChanged = true;
	
	mutable ID3D11BlendState* _blendState;
	mutable D3D11_BLEND_DESC _blendDesc;
	mutable D3D11_RENDER_TARGET_BLEND_DESC _rtblendDesc;
	mutable bool _blendDescChanged = true;

	mutable D3D11_SAMPLER_DESC _samplerDesc;
	mutable bool _samplerDescChanged = true;

	mutable float _clearColor[4];

	mutable int _sourceBlendColor = 1;
	mutable int _destBlendColor = 1;

//	GPUUniform_D3D* _currentUniforms;

public:

	NativeGL_win8(Microsoft::WRL::ComPtr<ID3D11Device1>& device,
		Microsoft::WRL::ComPtr<ID3D11DeviceContext1>& dc,
		Microsoft::WRL::ComPtr<ID3D11RenderTargetView>& rtv,
		Microsoft::WRL::ComPtr<ID3D11DepthStencilView>& dsv) :
		_device(device),
		_deviceContext(dc),
		_renderTargetView(rtv),
		_depthStencilView(dsv)
	{
		initializeRenderStates();
	}

	Microsoft::WRL::ComPtr<ID3D11Device1> getDevice(){
		return _device;
	}

	Microsoft::WRL::ComPtr<ID3D11DeviceContext1> getDeviceContext(){
		return _deviceContext;
	}

	//d3d: colors as const FLOAT ColorRGBA[4], clear color is passed as an argument of buffer-specific API-call
	void clearColor(float red, float green, float blue, float alpha) const;
	void useProgram(IGPUProgram* program) const;

	//d3d: Uniforms dont exist, use constant buffers
	//TODO: ID?
	void uniform2f(const IGLUniformID* loc, float x, float y) const;
	void uniform1f(const IGLUniformID* loc, float x) const;
	void uniform1i(const IGLUniformID* loc, int v) const;
	void uniformMatrix4fv(const IGLUniformID* location, bool transpose, const Matrix44D* matrix) const;
	void uniform4f(const IGLUniformID* location, float v0, float v1, float v2, float v3) const;
	void uniform3f(const IGLUniformID* location, float v0, float v1, float v2) const;

	//d3d: specific API calls for each buffer:
	//see:http://msdn.microsoft.com/en-us/library/windows/desktop/ff476387(v=vs.85).aspx (Depth-/Stencil buffer), 
	//http://msdn.microsoft.com/en-us/library/windows/desktop/ff476388(v=vs.85).aspx (Color buffer)
	void clear(int buffers) const;

	void enable(int feature) const;
	void disable(int feature) const;
	void polygonOffset(float factor, float units) const;
	void vertexAttribPointer(int index, int size, bool normalized, int stride, const IFloatBuffer* buffer) const;
	void drawElements(int mode, int count, IShortBuffer* indices) const;
	void lineWidth(float width) const;
	int getError() const;
	void blendFunc(int sfactor, int dfactor) const;
	void bindTexture(int target, const IGLTextureId* texture) const;
	bool deleteTexture(const IGLTextureId* texture) const;
	void enableVertexAttribArray(int location) const;
	void disableVertexAttribArray(int location) const;
	void pixelStorei(int pname, int param) const;
	std::vector<IGLTextureId*> genTextures(int	n) const;
	void texParameteri(int target, int par, int v) const;
	void texImage2D(const IImage* image, int format) const;
	void generateMipmap(int target) const;
	void drawArrays(int mode, int first, int count) const;
	void cullFace(int c) const;
	void getIntegerv(int v, int i[]) const;

	int CullFace_Front() const;
	int CullFace_Back() const;
	int CullFace_FrontAndBack() const;

	int BufferType_ColorBuffer() const;
	int BufferType_DepthBuffer() const;

	int Feature_PolygonOffsetFill() const;
	int Feature_DepthTest() const;
	int Feature_Blend() const;
	int Feature_CullFace() const;

	int Type_Float() const;
	int Type_UnsignedByte() const;
	int Type_UnsignedInt() const;
	int Type_Int() const;
	int Type_Vec2Float() const;
	int Type_Vec3Float() const;
	int Type_Vec4Float() const;
	int Type_Bool() const;
	int Type_Matrix4Float() const;

	int Primitive_Triangles() const;
	int Primitive_TriangleStrip() const;
	int Primitive_TriangleFan() const;
	int Primitive_Lines() const;
	int Primitive_LineStrip() const;
	int Primitive_LineLoop() const;
	int Primitive_Points() const;

	int BlendFactor_One() const;
	int BlendFactor_Zero() const;
	int BlendFactor_SrcAlpha() const;
	int BlendFactor_OneMinusSrcAlpha() const;

	int TextureType_Texture2D() const;

	int TextureParameter_MinFilter() const;
	int TextureParameter_MagFilter() const;
	int TextureParameter_WrapS() const;
	int TextureParameter_WrapT() const;

	int TextureParameterValue_Nearest() const;
	int TextureParameterValue_Linear() const;
	int TextureParameterValue_NearestMipmapNearest() const;
	int TextureParameterValue_NearestMipmapLinear() const;
	int TextureParameterValue_LinearMipmapNearest() const;
	int TextureParameterValue_LinearMipmapLinear() const;

	int TextureParameterValue_ClampToEdge() const;

	int Alignment_Pack() const;
	int Alignment_Unpack() const;

	int Format_RGBA() const;

	int Variable_Viewport() const;
	int Variable_ActiveAttributes() const;
	int Variable_ActiveUniforms() const;

	int Error_NoError() const;

	int createProgram() const;
	bool deleteProgram(int program) const;
	void attachShader(int program, int shader) const;
	int createShader(ShaderType type) const;
	bool compileShader(int shader, const std::string& source) const;
	bool deleteShader(int shader) const;
	void printShaderInfoLog(int shader) const;
	bool linkProgram(int program) const;
	void printProgramInfoLog(int program) const;

	void bindAttribLocation(const IGPUProgram* program, int loc, const std::string& name) const;

	int getProgramiv(const IGPUProgram* program, int param) const;

	GPUUniform* getActiveUniform(const IGPUProgram* program, int i) const;
	GPUAttribute* getActiveAttribute(const IGPUProgram* program, int i) const;

	void depthMask(bool v) const;

	void setActiveTexture(int i) const;

private:
		void setRenderState() const;
		void initializeRenderStates() const;
};

#endif