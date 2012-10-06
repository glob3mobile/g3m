

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IGLProgramId;

import com.google.gwt.core.client.JavaScriptObject;


public final class GLProgramId_WebGL
         implements
            IGLProgramId {

   final private JavaScriptObject _program; //WebGLProgram


   public GLProgramId_WebGL(final JavaScriptObject program) {
      _program = program;
   }


   public JavaScriptObject getProgram() {
      return _program;
   }


   @Override
   public boolean isValid() {
      // TODO CHECK
      return _program != null;
   }

}
