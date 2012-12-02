

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IGLUniformID;

//import com.google.gwt.core.client.JavaScriptObject;


public final class GLUniformID_WebGL
         implements
            IGLUniformID {

   private final int _id;


   GLUniformID_WebGL(final int id) {
      _id = id;
   }


   public int getId() {
      return _id;
   }


   @Override
   public boolean isValid() {
      return _id >-1;
   }


}
