

package com.glob3mobile.vectorial.storage.mapdb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;


public class SerializerUtils {

   private SerializerUtils() {
   }


   public static void serializeGeodetic2D(final DataOutput out,
                                          final Geodetic2D position) throws IOException {
      if (position == null) {
         out.writeDouble(Double.NaN);
         out.writeDouble(Double.NaN);
      }
      else {
         out.writeDouble(position._latitude._radians);
         out.writeDouble(position._longitude._radians);
      }
   }


   public static Geodetic2D deserializeGeodetic2D(final DataInput in) throws IOException {
      final double latitude = in.readDouble();
      final double longitude = in.readDouble();
      if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
         return null;
      }
      return Geodetic2D.fromRadians(latitude, longitude);
   }


   public static int geodetic2DSerializationSize() {
      return 8 * 2;
   }


   public static void serializeSector(final DataOutput out,
                                      final Sector sector) throws IOException {
      if (sector == null) {
         final Geodetic2D position = null;
         serializeGeodetic2D(out, position);
         serializeGeodetic2D(out, position);
      }
      else {
         serializeGeodetic2D(out, sector._lower);
         serializeGeodetic2D(out, sector._upper);
      }
   }


   public static int sectorSerializationSize() {
      return geodetic2DSerializationSize() * 2;
   }


   public static Sector deserializeSector(final DataInput in) throws IOException {
      final Geodetic2D lower = deserializeGeodetic2D(in);
      final Geodetic2D upper = deserializeGeodetic2D(in);
      if ((lower == null) || (upper == null)) {
         return null;
      }
      return new Sector(lower, upper);
   }


   private static enum Type {
      NULL((byte) 0),
      TRUE((byte) 1),
      FALSE((byte) 2),
      STRING((byte) 3),
      BYTE((byte) 4),
      SHORT((byte) 5),
      INTEGER((byte) 6),
      LONG((byte) 7),
      FLOAT((byte) 8),
      DOUBLE((byte) 9),
      MAP((byte) 10),
      LIST((byte) 11);

      private byte _token;


      private Type(final byte token) {
         _token = token;
      }


      private static Type fromToken(final byte token) {
         for (final Type e : values()) {
            if (e._token == token) {
               return e;
            }
         }
         return null;
      }
   }


   private static <K, V> void serialize(final DataOutput out,
                                        final Map<K, V> map) throws IOException {
      out.writeByte(Type.MAP._token);
      serializeMap(out, map);
   }


   public static <K, V> void serializeMap(final DataOutput out,
                                          final Map<K, V> map) throws IOException {
      final int size = map.size();
      out.writeInt(size);
      for (final Map.Entry<K, V> entry : map.entrySet()) {
         serialize(out, entry.getKey());
         serialize(out, entry.getValue());
      }
   }


   public static <K, V> Map<K, V> deserializeMap(final DataInput in) throws IOException {
      final int size = in.readInt();
      final Map<K, V> map = new LinkedHashMap<>(size);
      for (int i = 0; i < size; i++) {
         final K key = deserialize(in);
         final V value = deserialize(in);
         map.put(key, value);
      }
      return map;
   }


   @SuppressWarnings("unchecked")
   private static <T> T deserialize(final DataInput in) throws IOException {
      final byte token = in.readByte();
      final Type type = Type.fromToken(token);
      if (type == null) {
         throw new IOException("Invalid token: " + token);
      }

      switch (type) {
         case NULL:
            return null;
         case TRUE:
            return (T) Boolean.TRUE;
         case FALSE:
            return (T) Boolean.FALSE;
         case STRING:
            return (T) in.readUTF();
         case BYTE:
            return (T) Byte.valueOf(in.readByte());
         case SHORT:
            return (T) Short.valueOf(in.readShort());
         case INTEGER:
            return (T) Integer.valueOf(in.readInt());
         case LONG:
            return (T) Long.valueOf(in.readLong());
         case FLOAT:
            return (T) Float.valueOf(in.readFloat());
         case DOUBLE:
            return (T) Double.valueOf(in.readDouble());
         case MAP:
            return (T) deserializeMap(in);
         case LIST:
            return (T) deserializeList(in);
         default:
            throw new IOException("Unsupported type: " + type);
      }
   }


   public static <T> List<T> deserializeList(final DataInput in) throws IOException {
      final int size = in.readInt();
      final List<T> list = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
         final T element = deserialize(in);
         list.add(element);
      }
      return list;
   }


   private static <T> void serialize(final DataOutput out,
                                     final List<T> list) throws IOException {
      out.writeByte(Type.LIST._token);
      serializeList(out, list);
   }


   public static <T> void serializeList(final DataOutput out,
                                        final List<T> list) throws IOException {
      final int size = list.size();
      out.writeInt(size);
      for (final T value : list) {
         serialize(out, value);
      }
   }


   @SuppressWarnings("unchecked")
   private static <T> void serialize(final DataOutput out,
                                     final T value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else if (value instanceof String) {
         serialize(out, (String) value);
      }
      else if (value instanceof Byte) {
         serialize(out, (Byte) value);
      }
      else if (value instanceof Short) {
         serialize(out, (Short) value);
      }
      else if (value instanceof Integer) {
         serialize(out, (Integer) value);
      }
      else if (value instanceof Long) {
         serialize(out, (Long) value);
      }
      else if (value instanceof Float) {
         serialize(out, (Float) value);
      }
      else if (value instanceof Double) {
         serialize(out, (Double) value);
      }
      else if (value instanceof Boolean) {
         serialize(out, (Boolean) value);
      }
      else if (value instanceof Map) {
         serialize(out, (Map) value);
      }
      else if (value instanceof List) {
         serialize(out, (List) value);
      }
      else {
         throw new IOException("Class not yet supported: " + value.getClass());
      }
   }


   private static void serialize(final DataOutput out,
                                 final String value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else {
         out.writeByte(Type.STRING._token);
         out.writeUTF(value);
      }
   }


   private static void serialize(final DataOutput out,
                                 final Byte value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else {
         out.writeByte(Type.BYTE._token);
         out.writeByte(value.byteValue());
      }
   }


   private static void serialize(final DataOutput out,
                                 final Short value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else {
         out.writeByte(Type.SHORT._token);
         out.writeShort(value.shortValue());
      }
   }


   private static void serialize(final DataOutput out,
                                 final Integer value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else {
         out.writeByte(Type.INTEGER._token);
         out.writeInt(value.intValue());
      }
   }


   private static void serialize(final DataOutput out,
                                 final Long value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else {
         out.writeByte(Type.LONG._token);
         out.writeLong(value.longValue());
      }
   }


   private static void serialize(final DataOutput out,
                                 final Float value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else {
         out.writeByte(Type.FLOAT._token);
         out.writeFloat(value.floatValue());
      }
   }


   private static void serialize(final DataOutput out,
                                 final Double value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else {
         out.writeByte(Type.DOUBLE._token);
         out.writeDouble(value.doubleValue());
      }
   }


   private static void serialize(final DataOutput out,
                                 final Boolean value) throws IOException {
      if (value == null) {
         out.writeByte(Type.NULL._token);
      }
      else {
         out.writeByte(value.booleanValue() ? Type.TRUE._token : Type.FALSE._token);
      }
   }


}
