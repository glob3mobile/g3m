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
	return new GPUProgram_D3D(gl, name, _ngl);

}





