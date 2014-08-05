//
//  GPUProgramFactory_D3D.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 06/07/14.
//
//

//#include <d3d11_2.h>
//#include <fstream>
//#include <iostream>
#include "GPUProgramFactory_D3D.hpp"
#include "ILogger.hpp"
#include "GPUProgram_D3D.hpp"

#include "GL.hpp"



IGPUProgram* GPUProgramFactory_D3D::get(GL* gl, const std::string& name){

	if (_ngl == NULL){
		_ngl = (NativeGL_win8*)gl->getNative();
	}
	
	/*//Load pre-compiled vertex-Shader file
	std::string vertexShaderName = name + "V.cso";
	//std::wstring wvertexShaderName;
	//wvertexShaderName.assign(vertexShaderName.begin(), vertexShaderName.end());
	Platform::Array<byte>^ VSData = loadShaderFile(vertexShaderName);
	//create the vertex Shader object
	ID3D11VertexShader* vertexShader;
	_ngl->getDevice()->CreateVertexShader(VSData->Data, VSData->Length, nullptr, &vertexShader);

	//Load pre-compiled pixel shader file
	std::string pixelShaderName = name + "P.cso";
	Platform::Array<byte>^ PSData = loadShaderFile(pixelShaderName);
	//create the pixel Shader object
	ID3D11PixelShader* pixelShader;
	_ngl->getDevice()->CreatePixelShader(PSData->Data, PSData->Length, nullptr, &pixelShader);*/

	return new GPUProgram_D3D(gl, name, _ngl);

}





