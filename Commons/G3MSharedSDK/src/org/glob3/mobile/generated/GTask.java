package org.glob3.mobile.generated; 
//
//  GTask.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

//
//  GTask.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//


//class G3MContext;


public abstract class GTask
{
///#ifdef C_CODE
//  virtual ~GTask() { }
///#endif
///#ifdef JAVA_CODE
//  void dispose();
///#endif

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning vtp ask Dgd: GTask no debería ser una interfaz pura??
  public void dispose()
  {
  }


  public abstract void run(G3MContext context);
}