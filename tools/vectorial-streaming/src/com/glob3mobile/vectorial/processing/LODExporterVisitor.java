

package com.glob3mobile.vectorial.processing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.glob3mobile.geo.Geodetic2D;
import com.glob3mobile.geo.Sector;
import com.glob3mobile.utils.Progress;
import com.glob3mobile.vectorial.lod.PointFeatureLODStorage;
import com.glob3mobile.vectorial.storage.PointFeature;


public final class LODExporterVisitor
   implements
      PointFeatureLODStorage.NodeVisitor {

   private final PointFeatureLODStorage _clusterStorage;
   private final int                    _nodesCount;
   private final int                    _minDepth;
   private final int                    _maxDepth;

   private Progress                     _progress;

   private final AtomicLong             _visitedFeaturesCounter;
   private final Sector                 _sectorToProcess;
   private final BufferedWriter         _writter;

   private final Set<String>            _properties;


   public LODExporterVisitor(final Sector sectorToProcess,
                             final PointFeatureLODStorage clusterStorage,
                             final int nodesCount,
                             final int minDepth,
                             final int maxDepth,
                             final AtomicLong visitedFeaturesCounter,
                             final BufferedWriter writter,
                             final Set<String> properties) {
      _sectorToProcess = sectorToProcess;
      _clusterStorage = clusterStorage;
      _nodesCount = nodesCount;
      _minDepth = minDepth;
      _maxDepth = maxDepth;
      _visitedFeaturesCounter = visitedFeaturesCounter;
      _writter = writter;
      _properties = properties;
   }


   @Override
   public void start() {
      _progress = new Progress(_nodesCount) {
         @Override
         public void informProgress(final long stepsDone,
                                    final double percent,
                                    final long elapsed,
                                    final long estimatedMsToFinish) {
            System.out.println(_clusterStorage.getName() + " [" + _minDepth + "-" + _maxDepth + "] - Processing: "
                               + progressString(stepsDone, percent, elapsed, estimatedMsToFinish));
         }
      };
   }


   @Override
   public void stop() {
      //      try {
      //         _bitmap.save(new File("LOD_" + _clusterStorage.getName() + "_" + _minDepth + "-" + _maxDepth + ".png"));
      //      }
      //      catch (final IOException e) {
      //         throw new RuntimeException(e);
      //      }
      //      _bitmap = null;

      _progress.finish();
      _progress = null;

      System.out.println("- Visited featues: " + _visitedFeaturesCounter.longValue());
   }


   @Override
   public boolean visit(final PointFeatureLODStorage.Node node) {
      final Sector nodeSector = node.getNodeSector();
      if (nodeSector.touchesWith(_sectorToProcess)) {
         final int nodeDepth = node.getDepth();
         if ((nodeDepth >= _minDepth) && (nodeDepth <= _maxDepth)) {
            for (final PointFeature feature : node.getFeatures()) {
               final Geodetic2D featurePosition = feature._position;
               if (_sectorToProcess.contains(featurePosition)) {

                  try {
                     _writter.append("{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[");
                     _writter.append(Double.toString(featurePosition._longitude._degrees));
                     _writter.append(",");
                     _writter.append(Double.toString(featurePosition._latitude._degrees));
                     _writter.append("]},\"properties\":{");

                     final Map<String, Object> properties = feature._properties;
                     boolean firstEntry = true;
                     for (final Map.Entry<String, Object> entry : properties.entrySet()) {
                        final String propertyName = entry.getKey();
                        if (_properties.contains(propertyName)) {
                           if (firstEntry) {
                              firstEntry = false;
                           }
                           else {
                              _writter.append(",");
                           }
                           _writter.append(toJSON(propertyName));
                           _writter.append(":");
                           _writter.append(toJSON(entry.getValue()));
                        }
                     }

                     _writter.append("}");
                     _writter.append("},");
                     _writter.newLine();
                     //{ "type": "Feature",
                     //   "geometry": {"type": "Point", "coordinates": [102.0, 0.5]},
                     //   "properties": {"prop0": "value0"}
                     //   },
                  }
                  catch (final IOException e) {
                     throw new RuntimeException(e);
                  }

                  _visitedFeaturesCounter.incrementAndGet();
               }
            }
         }
      }
      _progress.stepDone();
      return true;
   }


   @SuppressWarnings("unchecked")
   public static String toJSON(final Object value) {
      if (value == null) {
         return "null";
      }
      else if (value instanceof String) {
         return toJSON((String) value);
      }
      else if (value instanceof Number) {
         return toJSON((Number) value);
      }
      else if (value instanceof Boolean) {
         return toJSON((Boolean) value);
      }
      else if (value instanceof List) {
         return toJSON((List<?>) value);
      }
      else if (value instanceof Object[]) {
         return toJSON((Object[]) value);
      }
      else if (value instanceof Map) {
         return toJSON((Map<String, ?>) value);
      }
      else {
         throw new RuntimeException("Unsupported type: " + value.getClass());
      }
   }


   public static String toJSON(final Number number) {
      return number.toString();
   }


   public static String toJSON(final Boolean bool) {
      return bool.booleanValue() ? "true" : "false";
   }


   private static String quote(final String string) {
      if ((string == null) || (string.length() == 0)) {
         return "\"\"";
      }

      char c = 0;
      int i;
      final int len = string.length();
      final StringBuilder sb = new StringBuilder(len + 4);
      String t;

      sb.append('"');
      for (i = 0; i < len; i += 1) {
         c = string.charAt(i);
         switch (c) {
            case '\\':
            case '"':
               sb.append('\\');
               sb.append(c);
               break;
            case '/':
               //                if (b == '<') {
               sb.append('\\');
               //                }
               sb.append(c);
               break;
            case '\b':
               sb.append("\\b");
               break;
            case '\t':
               sb.append("\\t");
               break;
            case '\n':
               sb.append("\\n");
               break;
            case '\f':
               sb.append("\\f");
               break;
            case '\r':
               sb.append("\\r");
               break;
            default:
               if (c < ' ') {
                  t = "000" + Integer.toHexString(c);
                  sb.append("\\u" + t.substring(t.length() - 4));
               }
               else {
                  sb.append(c);
               }
         }
      }
      sb.append('"');
      return sb.toString();
   }


   public static String toJSON(final String string) {
      // return "\"" + string.replace("\"", "\\\"") + "\"";
      return quote(string);
   }


   public static String toJSON(final Map<String, ?> map) {
      final StringBuilder sb = new StringBuilder();

      sb.append('{');
      boolean first = true;
      for (final Map.Entry<String, ?> entry : map.entrySet()) {
         if (first) {
            first = false;
         }
         else {
            sb.append(',');
         }
         sb.append(toJSON(entry.getKey()));
         sb.append(':');
         sb.append(toJSON(entry.getValue()));
      }
      sb.append('}');

      return sb.toString();
   }


   public static String toJSON(final List<?> list) {
      final StringBuilder sb = new StringBuilder();

      sb.append('[');
      for (int i = 0; i < list.size(); i++) {
         if (i != 0) {
            sb.append(',');
         }
         sb.append(toJSON(list.get(i)));
      }
      sb.append(']');

      return sb.toString();
   }


   public static String toJSON(final Object[] array) {
      final StringBuilder sb = new StringBuilder();

      sb.append('[');
      final int length = array.length;
      for (int i = 0; i < length; i++) {
         if (i != 0) {
            sb.append(',');
         }
         sb.append(toJSON(array[i]));
      }
      sb.append(']');

      return sb.toString();
   }


   public static String toJSON(final String name,
                               final Object value) {
      return "{" + toJSON(name) + ":" + toJSON(value) + "}";
   }


}
