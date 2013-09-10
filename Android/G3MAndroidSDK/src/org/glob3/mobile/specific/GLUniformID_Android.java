

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IGLUniformID;


public final class GLUniformID_Android
         implements
            IGLUniformID {

   private final int _id;


   GLUniformID_Android(final int id) {
      _id = id;
   }


   public int getID() {
      return _id;
   }


   @Override
   public boolean isValid() {
      return (_id > -1);
   }


   @Override
   public void dispose() {

   }

}
