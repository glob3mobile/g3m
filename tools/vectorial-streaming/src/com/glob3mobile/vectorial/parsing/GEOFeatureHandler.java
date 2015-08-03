

package com.glob3mobile.vectorial.parsing;

import java.util.Map;

import com.glob3mobile.vectorial.GEOGeometry;


public interface GEOFeatureHandler {


   void onStart();


   void onError(Map<String, Object> properties,
                GEOGeometry geometry);


   void onFeature(Map<String, Object> properties,
                  GEOGeometry geometry);


   void onFinish();


}
