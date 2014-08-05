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


enum AttributeType{
	UNKNOWN = 0,
	FLOAT1 = 1,
	FLOAT2 = 2,
	FLOAT3 = 3,
	FLOAT4 = 4
};

/*struct mapStruct{
	static std::map<std::string, AttributeType> create_map()
	{
		std::map<std::string, AttributeType> m;
		m["BINORMAL"] = FLOAT4;
		m["BLENDWEIGHT"] = FLOAT1;
		m["COLOR"] = FLOAT4;
		m["NORMAL"] = FLOAT4;
		m["POSITION"] = FLOAT4;
		m["POSITIONT"] = FLOAT4;
		m["PSIZE"] = FLOAT1;
		m["TANGENT"] = FLOAT4;
		m["TEXCOORD"] = FLOAT4;
		return m;
	}
	static const std::map<std::string, AttributeType> myMap;
};*/

class GPUProgram_D3D : public IGPUProgram{
private:
	NativeGL_win8* _ngl;
	ID3D11VertexShader* _vshader;
	ID3D11PixelShader* _pshader;
	Platform::Array<byte>^ _vsData;
	Platform::Array<byte>^ _psData;

	
	//const std::map<std::string, AttributeType> mapStruct::myMap = mapStruct::create_map();
	std::map<std::string, AttributeType> m;

	/*std::map<std::string, int> semantics_list = {
		std::make_pair("BINORMAL", FLOAT4),
		std::make_pair("BLENDWEIGHT", FLOAT1),
		std::make_pair("COLOR", FLOAT4),
		std::make_pair("NORMAL", FLOAT4),
		std::make_pair("POSITION", FLOAT4),
		std::make_pair("POSITIONT", FLOAT4),
		std::make_pair("PSIZE", FLOAT1),
		std::make_pair("TANGENT", FLOAT4),
		std::make_pair("TEXCOORD", FLOAT4)
	};*/

	Platform::Array<byte>^ loadShaderFile(std::string File);
	void getShaderUniforms(GL* gl);
	void getShaderAttributes(GL* gl);
	GPUUniform* createUniform(std::string name, int size, int klasse, int type, int id);
	GPUAttribute* createAttribute(std::string name);

public:
	GPUProgram_D3D(GL* gl,
		const std::string& name,
		NativeGL_win8* ngl);

	void onUnused(GL* gl);
	void applyChanges(GL* gl);
	void getVariables(GL* gl);
	void deleteShader(GL* gl, int shader) const;
	void deleteProgram(GL* gl, const IGPUProgram* p);
	~GPUProgram_D3D();



};


#endif