

package org.glob3.mobile.generated;

//
//  GPUProgramManager.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//

//
//  GPUProgramManager.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 02/04/13.
//
//


public class GPUProgramManager {

   private final java.util.HashMap<String, GPUProgram> _programs = new java.util.HashMap<String, GPUProgram>();

   private final GPUProgramFactory                     _factory;
   private final GL                                    _gl;


   public GPUProgramManager(final GL gl,
                            final GPUProgramFactory factory) {
      _gl = gl;
      _factory = factory;
   }


   public void dispose() {

   }


   public final GPUProgram getCompiledProgram(final String name) {
      return _programs.get(name);
   }


   public final GPUProgram getProgram(final String name) {

      GPUProgram prog = getCompiledProgram(name);
      if (prog == null) {
         final GPUProgramSources ps = _factory.get(name);

         //Compile new Program
         if (ps != null) {
            prog = GPUProgram.createProgram(_gl, ps._name, ps._vertexSource, ps._fragmentSource);
            if (prog == null) {
               ILogger.instance().logError("Problem at creating program named %s.", name);
               return null;
            }

            _programs.put(name, prog);

            //_programs.insert ( std::pair<std::string, GPUProgram*>(prog->getName(),prog) );
         }

      }
      return prog;
   }


   public final GPUProgram getProgram(final GPUProgramState state) {

      final GPUProgram[] progs = (GPUProgram[]) _programs.values().toArray();
      for (final GPUProgram p : progs) {
         if (state.isLinkableToProgram(p)) {
            return p;
         }
      }

      final Iterator it = _programs.entrySet().iterator();
      while (it.hasNext()) {
         final Map.Entry pairs = (Map.Entry) it.next();
         final GPUProgram p = (GPUProgram) pairs.getValue();
         if (state.isLinkableToProgram(p)) {
            return p;
         }
      }

      final int WORKING_JM;

      final java.util.ArrayList<String> us = state.getUniformsNames();
      final int size = us.size();
      for (int i = 0; i < size; i++) {
         if (us.get(i).compareTo("ViewPortExtent") == 0) {
            return getProgram("Billboard");
         }
      }

      return getProgram("Default");
   }


}
