package org.glob3.mobile.generated; 
//
//  GPUProgramFactory_OGL.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 06/07/14.
//
//

//
//  GPUProgramFactory_OGL.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 06/07/14.
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

   public GPUProgramSources(String name, String vertexSource, String fragmentSource)
   {
      _name = name;
      _vertexSource = vertexSource;
      _fragmentSource = fragmentSource;
   }

   public GPUProgramSources(GPUProgramSources that)
   {
      _name = that._name;
      _vertexSource = that._vertexSource;
      _fragmentSource = that._fragmentSource;
   }
}