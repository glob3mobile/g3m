// Base D3D Renderer

#ifndef __G3MWindowSDK_D3DRenderer__
#define __G3MWindowSDK_D3DRenderer__


//#include <wrl.h>
#include <wrl/client.h>
#include <d3d11_2.h>
//#include <d2d1_2.h>
//#include <d2d1effects_1.h>
//#include <dwrite_2.h>
//#include <wincodec.h>
//#include <DirectXColors.h>
//#include <DirectXMath.h>
//#include <memory>
//#include <agile.h>
//#include <concrt.h>
//#include <collection.h>
//#include <ppltasks.h>



class NativeGL_win8;
class GL;
class G3MWidget;

//ref class D3DRenderer sealed{	
class D3DRenderer {


private:

	NativeGL_win8* _nativeGL;
	GL* _gl;
	G3MWidget* _widget;

	bool _isInitialized = false;

	//Represents a virtual adapter for our D3D device, it is an abstraction layer for working with graphics devices.
	Microsoft::WRL::ComPtr<ID3D11Device1> device;
	//Represents a device context which is used to render commands.
	Microsoft::WRL::ComPtr<ID3D11DeviceContext1> deviceContext;
	//Our swap chain structure, DXGI stands for DirectX Graphics Infrastructure.
	Microsoft::WRL::ComPtr<IDXGISwapChain1> swapChain;
	// Handles the render-target subresources that we can access during rendering.
	Microsoft::WRL::ComPtr<ID3D11RenderTargetView> d3dRenderTargetView;
	//Used in depth stencil testing.
	Microsoft::WRL::ComPtr<ID3D11DepthStencilView> d3dDepthStencilView;
	//Vertex Shader Com-Pointer
	Microsoft::WRL::ComPtr<ID3D11VertexShader> vertexshader;
	//Pixel Shader Com-Pointer
	Microsoft::WRL::ComPtr<ID3D11PixelShader> pixelshader;

	Windows::UI::Core::CoreWindow^ _coreWindow;

	float* bgcol;


public:
	D3DRenderer();
	void initialize(Windows::UI::Core::CoreWindow^ coreWindow);
	void createDeviceAndContext();
	void createSwapChain();
	void createDeviceDependentResources();

	void render();

	void setWidget(G3MWidget* widget);

	GL* getGL(){
		return _gl;
	}

	//void cleanUp();
	bool isInitialized() const;
};



#endif
