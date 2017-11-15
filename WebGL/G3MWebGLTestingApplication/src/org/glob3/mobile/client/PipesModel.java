package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Cylinder;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class PipesModel {

	public static double coord;
	public static ArrayList<Cylinder.CylinderMeshInfo> cylinderInfo = new ArrayList<>();
	public static int loadedFiles = 0;
	
	public static void addMeshes(final String file, 
			final Planet p, final MeshRenderer mr, final ElevationData ed,
			final double heightOffset){
		//OJO: ¿con el final podré editarle cosas?
		  
		  // Cómo leer de un archivo de texto en web. Imagino que con alguna tarea de download.
		  RequestBuilder requestBuilder = new RequestBuilder( RequestBuilder.GET, file );
	        try {
	            requestBuilder.sendRequest( null, new RequestCallback(){
	                public void onError(Request request, Throwable exception) {
	                    GWT.log( "failed file reading", exception );
	                }

	                public void onResponseReceived(Request request, Response response) {
	                    String result=response.getText();
	                    parseContent(result,p,mr,ed,heightOffset);

	                }} );
	        } catch (RequestException e) {
	            GWT.log( "failed file reading", e );
	        }
	}
	
	private static void parseContent(final String csvContent, final Planet p, MeshRenderer mr, 
			final ElevationData ed, final double heightOffset)
	{
		JsArray lines = NativeUtils.splitString(csvContent, "\n");
		int nPipes = 0;
		for (int i=0; i<lines.length();i++){
			JavaScriptObject line = lines.get(i);
			JsArray numbers = NativeUtils.splitString(line.toString(), " ");
			if (numbers.length() == 6){
				 	
				  double lon = Double.parseDouble(numbers.get(0).toString());
			      double lat = Double.parseDouble(numbers.get(1).toString());
			      double h = Double.parseDouble(numbers.get(2).toString());
			      
			      double lon2 = Double.parseDouble(numbers.get(3).toString());
			      double lat2 = Double.parseDouble(numbers.get(4).toString());
			      double h2 = Double.parseDouble(numbers.get(5).toString());
			      
			      double o1 = (ed == null)? 0.0 : ed.getElevationAt(Geodetic2D.fromDegrees(lat, lon));
			      double o2 = (ed == null)? 0.0 : ed.getElevationAt(Geodetic2D.fromDegrees(lat2, lon2));
			      
			      Geodetic3D g = Geodetic3D.fromDegrees(lat, lon, h + o1 + heightOffset);
			      Geodetic3D g2 = Geodetic3D.fromDegrees(lat2, lon2, h2 + o2 + heightOffset);

			      //Tubes
			      Cylinder c= new Cylinder(p.toCartesian(g), p.toCartesian(g2), 0.5);
			        
			      mr.addMesh(c.createMesh(Color.fromRGBA255(255, 0, 0, 32), 5, p));
//			      mr->addMesh(c.createMesh(Color::fromRGBA255(255, 0, 0, 255), 5, p));
			      cylinderInfo.add(new Cylinder.CylinderMeshInfo(c._info));
			      
			      nPipes++;
			      
			      //Junctions
//			      Cylinder c2(p->toCartesian(Geodetic3D(g._latitude, g._longitude, g._height-0.5)),
//			                  p->toCartesian(Geodetic3D(g._latitude, g._longitude, g._height+0.7)), 0.7);
//			      mr->addMesh(c2.createMesh(Color::blue(), 5));
//			      
//			      Cylinder c3(p->toCartesian(Geodetic3D(g2._latitude, g2._longitude, g2._height-0.5)),
//			                  p->toCartesian(Geodetic3D(g2._latitude, g2._longitude, g2._height+0.7)), 0.7);
//			      mr->addMesh(c3.createMesh(Color::blue(), 5));

			}
		}
		loadedFiles++;
	}

}
