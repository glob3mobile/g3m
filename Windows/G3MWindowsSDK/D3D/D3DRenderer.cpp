#include "D3DRenderer.hpp"

#include "G3MWidget.hpp"
#include "GL.hpp"
#include "NativeGL_win8.hpp"

D3DRenderer::D3DRenderer()
{
	initialize(Windows::UI::Core::CoreWindow::GetForCurrentThread());
	createDeviceAndContext();
	createSwapChain();
	createDeviceDependentResources();

	this->_nativeGL = new NativeGL_win8(deviceContext, d3dRenderTargetView, d3dDepthStencilView);
	this->_gl = new GL(_nativeGL, true);

	_isInitialized = true;
}

void D3DRenderer::setWidget(G3MWidget* widget){
	_widget = widget;
}

void D3DRenderer::initialize(Windows::UI::Core::CoreWindow^ coreWindow){
	this->_coreWindow = coreWindow;
}

void D3DRenderer::createDeviceAndContext(){

	// Define temporary pointers to a device and a device context
	Microsoft::WRL::ComPtr<ID3D11Device> dev11;
	Microsoft::WRL::ComPtr<ID3D11DeviceContext> devcon11;
	// Create the device and device context objects
	D3D11CreateDevice(
		nullptr,
		D3D_DRIVER_TYPE_HARDWARE,
		nullptr,
		D3D11_CREATE_DEVICE_BGRA_SUPPORT,
		nullptr,
		0,
		D3D11_SDK_VERSION,
		&dev11,
		nullptr,
		&devcon11);
	// Cast the pointers from the DirectX 11 versions to the DirectX 11.1 versions
	dev11.As(&device);
	devcon11.As(&deviceContext);
}

void D3DRenderer::createSwapChain(){
	//swapchain description object
	DXGI_SWAP_CHAIN_DESC1 scDesc = { 0 };

	//Start filling the description object with values
	//1: width, height
	Windows::Foundation::Rect windowBounds = this->_coreWindow->Bounds;
	scDesc.Width = static_cast<UINT>(windowBounds.Width);
	scDesc.Height = static_cast<UINT>(windowBounds.Height);

	//2. Pixel format
	scDesc.Format = DXGI_FORMAT_B8G8R8A8_UNORM;

	//3. Stereo?
	scDesc.Stereo = false;

	//4. Anti-Aliasing
	scDesc.SampleDesc.Count = 1;
	scDesc.SampleDesc.Quality = 0;

	//5. What to use buffers for (drawing, shaders...) and how many to create
	scDesc.BufferUsage = DXGI_USAGE_RENDER_TARGET_OUTPUT;
	scDesc.BufferCount = 2;

	//6. Scaling (Buffer size != window size)
	scDesc.Scaling = DXGI_SCALING_NONE;

	//7. How to step through the buffers
	scDesc.SwapEffect = DXGI_SWAP_EFFECT_FLIP_SEQUENTIAL;

	//8. Additional Flags
	scDesc.Flags = 0;

	//Get the dxgiFactory. Weird. Whatever...
	Microsoft::WRL::ComPtr<IDXGIDevice1> dxgiDevice;
	device.As(&dxgiDevice);
	Microsoft::WRL::ComPtr<IDXGIAdapter> dxgiAdapter;
	dxgiDevice->GetAdapter(&dxgiAdapter);
	Microsoft::WRL::ComPtr<IDXGIFactory2> dxgiFactory;
	dxgiAdapter->GetParent(__uuidof(IDXGIFactory2), &dxgiFactory);

	//Finally, create the darn swapchain
	dxgiFactory->CreateSwapChainForCoreWindow(
		device.Get(),
		reinterpret_cast<IUnknown*>(_coreWindow),
		&scDesc,
		nullptr, //Not needed for now.
		&swapChain);
}

void D3DRenderer::createDeviceDependentResources(){

	//RenderTarget (it is basically a 2D-Texture)
	Microsoft::WRL::ComPtr<ID3D11Texture2D> d3dBackBuffer;
	swapChain->GetBuffer(0, IID_PPV_ARGS(&d3dBackBuffer));
	device->CreateRenderTargetView(d3dBackBuffer.Get(), nullptr, &d3dRenderTargetView);


	//StencilBuffer (also used as z-buffer) (it is basically another 2D-Texture)
	Windows::Foundation::Rect windowBounds = this->_coreWindow->Bounds;
	CD3D11_TEXTURE2D_DESC d3dDepthStencilDesc(
		DXGI_FORMAT_D24_UNORM_S8_UINT,
		static_cast<UINT>(windowBounds.Width),
		static_cast<UINT>(windowBounds.Height),
		1,
		1,
		D3D11_BIND_DEPTH_STENCIL
		);
	Microsoft::WRL::ComPtr<ID3D11Texture2D> d3dStencilBuffer;
	device->CreateTexture2D(&d3dDepthStencilDesc, nullptr, &d3dStencilBuffer);

	CD3D11_DEPTH_STENCIL_VIEW_DESC depthStencilViewDesc(
		D3D11_DSV_DIMENSION_TEXTURE2D
		);
	device->CreateDepthStencilView(d3dStencilBuffer.Get(), &depthStencilViewDesc, &d3dDepthStencilView);

	// set the viewport
	D3D11_VIEWPORT viewport = { 0 };
	viewport.TopLeftX = 0;
	viewport.TopLeftY = 0;
	viewport.Width = _coreWindow->Bounds.Width;
	viewport.Height = _coreWindow->Bounds.Height;
	deviceContext->RSSetViewports(1, &viewport);
}


void D3DRenderer::render(){
	// set our new render target object as the active render target
	deviceContext->OMSetRenderTargets(1, d3dRenderTargetView.GetAddressOf(), nullptr);

	// clear the back buffer to a deep blue
	float color[4] = { 0.0f, 0.0f, 0.0f, 1.0f };
	deviceContext->ClearRenderTargetView(d3dRenderTargetView.Get(), color);

	if (_widget != NULL){
		_widget->render(100, 100);
	}

	// switch the back buffer and the front buffer
	swapChain->Present(1, 0);
}

bool D3DRenderer::isInitialized() const{
	return _isInitialized ;
}

