

package com.glob3mobile.pointcloud.octree.postgresql;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Map;


public class PostgreSQLByteArray
implements
Array {

   private final byte[] _value;
   private final String _stringValue;


   public PostgreSQLByteArray(final byte[] value) {
      _value = value;
      _stringValue = toPostgreSQLArrayString(value);
   }


   private static String toPostgreSQLArrayString(final byte[] value) {
      if (value == null) {
         return "NULL";
      }
      final int al = value.length;
      if (al == 0) {
         return "{}";
      }
      final StringBuilder sb = new StringBuilder();
      sb.append('{');
      for (int i = 0; i < al; i++) {
         if (i > 0) {
            sb.append(',');
         }
         sb.append(value[i]);
      }
      sb.append('}');
      return sb.toString();
   }


   @Override
   public String toString() {
      return _stringValue;
   }


   @Override
   public Object getArray() {
      return (_value == null) ? null : Arrays.copyOf(_value, _value.length);
   }


   @Override
   public Object getArray(final Map<String, Class<?>> map) {
      return getArray();
   }


   @Override
   public Object getArray(final long index,
                          final int count) {
      return (_value == null) ? null : Arrays.copyOfRange(_value, (int) index, (int) index + count);
   }


   @Override
   public Object getArray(final long index,
                          final int count,
                          final Map<String, Class<?>> map) {
      return getArray(index, count);
   }


   @Override
   public int getBaseType() {
      return java.sql.Types.SMALLINT;
   }


   @Override
   public String getBaseTypeName() {
      return "smallint";
   }


   @Override
   public ResultSet getResultSet() {
      throw new UnsupportedOperationException();
   }


   @Override
   public ResultSet getResultSet(final Map<String, Class<?>> map) {
      throw new UnsupportedOperationException();
   }


   @Override
   public ResultSet getResultSet(final long index,
                                 final int count) {
      throw new UnsupportedOperationException();
   }


   @Override
   public ResultSet getResultSet(final long index,
                                 final int count,
                                 final Map<String, Class<?>> map) {
      throw new UnsupportedOperationException();
   }


   @Override
   public void free() {
   }


}
