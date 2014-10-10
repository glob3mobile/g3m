//
//  GPUProgram_D3D.hpp
//  G3MWindowsSDK
//
//  Created by Oliver Koehler on 21/07/14.
//
//

#ifndef __G3MWindowsSDK__GPUProgram_D3D__
#define __G3MWindowsSDK__GPUProgram_D3D__

#include <map>

#include "IGPUProgram.hpp"
#include "NativeGL_win8.hpp"

class GPUUniform_D3D;
class GPUAttribute_D3D;


enum AttributeType{
	UNKNOWN = 0,
	FLOAT1 = 1,
	FLOAT2 = 2,
	FLOAT3 = 3,
	FLOAT4 = 4
};



class GPUProgram_D3D : public IGPUProgram{
private:
	NativeGL_win8* _ngl;
	ID3D11VertexShader* _vshader;
	ID3D11PixelShader* _pshader;
	ID3D11InputLayout* _inputLayout;
	Platform::Array<byte>^ _vsData;
	Platform::Array<byte>^ _psData;

	//todo: move to NativeGL
	std::map<std::string, AttributeType> m;

	Platform::Array<byte>^ loadShaderFile(std::string File);
	void getShaderUniforms(GL* gl);
	void getShaderAttributes(GL* gl);
	GPUUniform_D3D* createUniform(std::string name, int size, int klasse, int type, int id);
	GPUAttribute_D3D* createAttribute(std::string name, int id, int semanticIndex);


public:
	GPUProgram_D3D(GL* gl,
		const std::string& name,
		NativeGL_win8* ngl);

	ID3D11VertexShader* getVertexShader(){
		return _vshader;
	}

	ID3D11PixelShader* getPixelShader(){
		return _pshader;
	}


	//void onUnused(GL* gl);
	void applyChanges(GL* gl);
	void getVariables(GL* gl);
	void deleteShader(GL* gl, int shader) const;
	void deleteProgram(GL* gl, const IGPUProgram* p);
	~GPUProgram_D3D();



};


#endif