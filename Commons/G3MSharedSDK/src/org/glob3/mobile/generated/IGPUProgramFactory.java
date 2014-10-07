package org.glob3.mobile.generated; 
//
//  IGPUProgramFactory.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/07/14.
//
//

//
//  IGPUProgramFactory.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/07/14.
//
//



//class IGPUProgram;
//class GL;

public abstract class IGPUProgramFactory
{
   protected static IGPUProgramFactory _instance = null;

   public static void setInstance(IGPUProgramFactory factory)
   {
      if (_instance != null)
      {
         ILogger.instance().logWarning("IGPUProgramFactory instance already set!");
         if (_instance != null)
            _instance.dispose();
      }
      _instance = factory;
   }

   public static IGPUProgramFactory instance()
   {
      return _instance;
   }

   public abstract IGPUProgram get(GL gl, String name);

   public void dispose()
   {
   }

}