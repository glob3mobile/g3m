package org.glob3.mobile.generated; 
//
//  GPUProgramFactory.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

//
//  GPUProgramFactory.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//



public class GPUProgramSources
{
  public String _name;
  public String _vertexSource;
  public String _fragmentSource;

  public GPUProgramSources()
  {
  }

  public GPUProgramSources(String n, String v, String f)
  {
     _name = n;
     _vertexSource = v;
     _fragmentSource = f;
  }

  public GPUProgramSources(GPUProgramSources g)
  {
     _name = g._name;
     _vertexSource = g._vertexSource;
     _fragmentSource = g._fragmentSource;
  }
}