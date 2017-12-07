package org.eifer.eiferapp.g3mutils;

/**
 * Created by chano on 7/11/17.
 */

import java.util.ArrayList;

import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.Cylinder;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

public class PipesModel {

    public static double coord;
    public static ArrayList<Cylinder> cylinders = new ArrayList<>();
    public static ArrayList<Cylinder.CylinderMeshInfo> cylinderInfo = new ArrayList<>();
    public static int loadedFiles = 0;

    public static void addMeshes(final String file,
                                 final Planet p, final MeshRenderer mr, final ElevationData ed,
                                 final double heightOffset,
                                 IDownloader downloader){
        //OJO: ¿con el final podré editarle cosas?
        downloader.requestBuffer(new URL(file, false), 1000, TimeInterval.fromDays(30), true, new IBufferDownloadListener() {
            @Override
            public void onDownload(URL url, IByteBuffer buffer, boolean expired) {
                String result = buffer.getAsString();
                parseContent(result,p,mr,ed,heightOffset);
            }

            @Override
            public void onError(URL url) {}

            @Override
            public void onCancel(URL url) {}

            @Override
            public void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired) {}
        },true);
    }



    private static void parseContent(final String csvContent, final Planet p, MeshRenderer mr,
                                     final ElevationData ed, final double heightOffset)
    {
        String[] lines = csvContent.split("\n");
        int nPipes = 0;
        for (int i=0; i<lines.length;i++){
            String line = lines[i];
            String [] numbers = line.split(" ");
            if (numbers.length == 6){

                double lon = Double.parseDouble(numbers[0]);
                double lat = Double.parseDouble(numbers[1]);
                double h = Double.parseDouble(numbers[2]);

                double lon2 = Double.parseDouble(numbers[3]);
                double lat2 = Double.parseDouble(numbers[4]);
                double h2 = Double.parseDouble(numbers[5]);

                double o1 = (ed == null)? 0.0 : ed.getElevationAt(Geodetic2D.fromDegrees(lat, lon));
                double o2 = (ed == null)? 0.0 : ed.getElevationAt(Geodetic2D.fromDegrees(lat2, lon2));

                Geodetic3D g = Geodetic3D.fromDegrees(lat, lon, h + o1 + heightOffset);
                Geodetic3D g2 = Geodetic3D.fromDegrees(lat2, lon2, h2 + o2 + heightOffset);

                //Tubes
                Cylinder c= new Cylinder(p.toCartesian(g), p.toCartesian(g2), 0.5);

                mr.addMesh(c.createMesh(Color.fromRGBA255(255, 0, 0, 32), 5, p));
                cylinderInfo.add(new Cylinder.CylinderMeshInfo(c._info));
                cylinders.add(c);

                nPipes++;


            }
        }
        loadedFiles++;
    }

    public static void insertNewCylinder(Geodetic3D start, Geodetic3D end,final Planet p, MeshRenderer mr,
                                         final ElevationData ed, double heightOffset, int theId, String iMat, String eMat,
                                         double iDiam, double eDiam, String pClass, String pType, boolean isT, boolean isC){

        //PipesModel.insertNewCylinder(Geodetic3D.fromDegrees(lat,lon,h),Geodetic3D.fromDegrees(lat2,lon2,h2),_planet,
        //_mr, _elevationData,_hOffset, iMat, eMat, iDiam, eDiam, pClass, pType, isT, isC);

        double o1 = (ed == null)? 0.0 : ed.getElevationAt(start.asGeodetic2D());
        double o2 = (ed == null)? 0.0 : ed.getElevationAt(end.asGeodetic2D());
        double h = start._height;
        double h2 = end._height;

        Geodetic3D g = new Geodetic3D(start.asGeodetic2D(),h + o1 + heightOffset);
        Geodetic3D g2 = new Geodetic3D(end.asGeodetic2D(),h2 + o2 + heightOffset);

        //
        Cylinder c= new Cylinder(p.toCartesian(g), p.toCartesian(g2), eDiam / 10. ); //OJO que es 10.
        c._info.setClassAndType(pClass,pType);
        c._info.setMaterials(eMat,iMat);
        c._info.setWidths(iDiam,eDiam);
        c._info.setTransportComm(isT,isC);
        c._info.setID(theId);

        Color pipeColor;
        if (c._info.cylinderType.contentEquals("naturalGas")){
            pipeColor = Color.fromRGBA255(255, 0, 0, 32);
        }
        else if (c._info.cylinderType.contentEquals("High power")){
            pipeColor = Color.fromRGBA255(255,255,0,32);
        }
        else {
            pipeColor = Color.fromRGBA255(0,255,0,32);
        }

        mr.addMesh(c.createMesh(pipeColor, 5, p));
        cylinderInfo.add(new Cylinder.CylinderMeshInfo(c._info));
        cylinders.add(c);
    }

    public static void reset(){
        PipesModel.cylinderInfo.clear();
        PipesModel.cylinders.clear();
    }

}
