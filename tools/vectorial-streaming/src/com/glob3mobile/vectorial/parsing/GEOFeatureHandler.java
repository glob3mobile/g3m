

package com.glob3mobile.vectorial.parsing;

import java.util.Map;

import com.glob3mobile.vectorial.GEOGeometry;


public interface GEOFeatureHandler<E extends Exception> {


   void onStart() throws E;


   void onError(Map<String, Object> properties,
                GEOGeometry geometry) throws E;


   void onFeature(Map<String, Object> properties,
                  GEOGeometry geometry) throws E;


   void onFinish() throws E;


   void onFinishWithException();


}
