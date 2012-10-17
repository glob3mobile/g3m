

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IGLProgramId;


public final class GLProgramId_Android
         implements
            IGLProgramId {

   final private int _program;


   public GLProgramId_Android(final int program) {
      _program = program;
   }


   public int getProgram() {
      return _program;
   }


   @Override
   public boolean isValid() {
      return (_program > -1);
   }

}
