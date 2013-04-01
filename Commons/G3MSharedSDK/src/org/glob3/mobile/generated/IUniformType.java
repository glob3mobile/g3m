package org.glob3.mobile.generated; 
     public static void launchException()
     {
       throw new UniformException();
     }
   }
}

//
//  Uniforms.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 27/03/13.
//
//



//C++ TO JAVA CONVERTER TODO TASK: The original C++ template specifier was replaced with a Java generic specifier, which may not produce the same behavior:
public interface IUniformType<T>
{
  public void dispose()
  boolean isEqualsTo(T x);
  void set(GL gl, IGLUniformID id);
}