

package com.glob3mobile.vectorial.storage;

import java.io.Serializable;
import java.util.Comparator;


public class QuadKeyComparator
   implements
      Comparator<byte[]>,
      Serializable {

   private static final long serialVersionUID = 1L;


   @Override
   public int compare(final byte[] o1,
                      final byte[] o2) {
      return QuadKeyUtils.compare(o1, o2);
   }


}
