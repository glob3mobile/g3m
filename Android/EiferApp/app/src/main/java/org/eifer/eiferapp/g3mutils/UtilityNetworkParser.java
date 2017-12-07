package org.eifer.eiferapp.g3mutils;

import org.glob3.mobile.generated.DownloadPriority;
import org.glob3.mobile.generated.ElevationData;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;

/**
 * Created by chano on 30/11/17.
 */

public class UtilityNetworkParser {
    private static IThreadUtils _tUtils = null;
    private static Planet _planet = null;
    private static IDownloader _downloader = null;
    private static ElevationData _elevationData = null;
    private static MeshRenderer _mr = null;
    private static double _hOffset;

    public static void initialize(G3MContext context, ElevationData elevationData, MeshRenderer mr, double heightOffset)
    {
        _planet = context.getPlanet();
        _downloader = context.getDownloader();
        _elevationData = elevationData;
        _mr = mr;
        _hOffset = heightOffset;
        _tUtils = context.getThreadUtils();
    }

    private static void processPipeString(String pipeList, int theId, String iMat, String eMat, double iDiam,
                                          double eDiam, String pClass, String pType, boolean isT, boolean isC){
        String [] pipes = pipeList.split(" ");
        for (int i=0; i<(pipes.length - 3); i = i+3){
            double lon = Double.parseDouble(pipes[i]);
            double lat = Double.parseDouble(pipes[i+1]);
            double h = Double.parseDouble(pipes[i+2]);

            double lon2 = Double.parseDouble(pipes[i+3]);
            double lat2 = Double.parseDouble(pipes[i+4]);
            double h2 = Double.parseDouble(pipes[i+5]);

            PipesModel.insertNewCylinder(Geodetic3D.fromDegrees(lat,lon,h),Geodetic3D.fromDegrees(lat2,lon2,h2),_planet,
                    _mr, _elevationData, _hOffset, theId, iMat, eMat, iDiam, eDiam, pClass, pType, isT, isC);
        }
    }

    private static void parseContent(String cityGMLString){
        ILogger.instance().logInfo("UtilityParser starting parse");

        String internalMat = "", externalMat = "", pipeType;

        int pos = 0; int theId = 0;
        final int length = cityGMLString.length();
        while (pos < length) {
            int startPos = cityGMLString.indexOf("<core:cityObjectMember>", pos);
            int endPos = cityGMLString.indexOf("</core:cityObjectMember>", pos);

            if (startPos == length-1 || startPos == -1){
                ILogger.instance().logInfo("FAILURE! ");
                break;

            }

            IStringUtils.StringExtractionResult COMember =
                    IStringUtils.extractSubStringBetween(cityGMLString, "<core:cityObjectMember>", "</core:cityObjectMember>", pos);

            int emPos = COMember._string.indexOf("</utility:ExteriorMaterial>");
            int imPos = COMember._string.indexOf("</utility:InteriorMaterial>");
            if (emPos != -1){
                //Detect external material
                IStringUtils.StringExtractionResult type =
                        IStringUtils.extractSubStringBetween(COMember._string, "<utility:type>", "</utility:type>", 0);
                externalMat = type._string;
                ILogger.instance().logInfo("External material processed ");
            }
            else if (imPos != -1){
                IStringUtils.StringExtractionResult type =
                        IStringUtils.extractSubStringBetween(COMember._string, "<utility:type>", "</utility:type>", 0);
                internalMat = type._string;
                ILogger.instance().logInfo("Internal material processed ");
            }
            else {
                //Operate with networks
                // 1. Take Type. It can be naturalGas, waste water or high power.
                int typePos = COMember._string.indexOf("</utility:transportedMedium>");
                if (typePos != -1){
                    IStringUtils.StringExtractionResult type =
                            IStringUtils.extractSubStringBetween(COMember._string, "<utility:type>", "</utility:type>", 0);
                    pipeType = type._string;
                }
                else {
                    //Autoassign
                    pipeType = "High power";
                }
                //Second mission == Iterate over networks, idea: Catch Components.
                int compPos = 0;
                while (true){
                    compPos = COMember._string.indexOf("<utility:component>",compPos);
                    int compPosEnd = COMember._string.indexOf("</utility:component>",compPos);
                    //Revisar cómo funciona esta parte.
                    if (compPos != -1){
                        //Componente activo: desglosarlo!!!!! y buscar el resto de metralla.
                        ILogger.instance().logInfo("Processing component! ");
                        IStringUtils.StringExtractionResult component =
                                IStringUtils.extractSubStringBetween(COMember._string, "<utility:component>", "</utility:component>", compPos);

                        String pipeClass;
                        double extDiam, intDiam;
                        boolean isTransport = false, isCommunication = false;

                        int cablePos = component._string.indexOf("</utility:Cable>");
                        int pipePos = component._string.indexOf("</utility:RoundPipe>");
                        if (cablePos != -1) {
                            pipeClass = "Cable";
                            IStringUtils.StringExtractionResult crossSection =
                                    IStringUtils.extractSubStringBetween(component._string,
                                            "<utility:crossSection uom=\"cm\">", "</utility:crossSection>", 0);
                            extDiam = Double.parseDouble(crossSection._string);
                            intDiam = extDiam;
                            IStringUtils.StringExtractionResult isT =
                                    IStringUtils.extractSubStringBetween(component._string,
                                            "<utility:isTransmission>", "</utility:isTransmission>", 0);
                            isTransport = Boolean.parseBoolean(isT._string);
                            IStringUtils.StringExtractionResult isC =
                                    IStringUtils.extractSubStringBetween(component._string,
                                            "<utility:isCommunication>", "</utility:isCommunication>", 0);
                            isCommunication = Boolean.parseBoolean(isC._string);
                        }
                        else{
                            if (pipePos != -1) {
                                pipeClass = "RoundPipe";
                                IStringUtils.StringExtractionResult exteriorSection =
                                        IStringUtils.extractSubStringBetween(component._string,
                                                "<utility:exteriorDiameter>", "</utility:exteriorDiameter>", 0);
                                extDiam = Double.parseDouble(exteriorSection._string);
                                IStringUtils.StringExtractionResult interiorSection =
                                        IStringUtils.extractSubStringBetween(component._string,
                                                "<utility:interiorDiameter>", "</utility:interiorDiameter>", 0);
                                intDiam = Double.parseDouble(interiorSection._string);
                            }
                            else
                                break;
                        }

                        IStringUtils.StringExtractionResult gmlList =
                                IStringUtils.extractSubStringBetween(component._string, "<gml:posList>", "</gml:posList>", 0);
                        if (gmlList._endingPos == -1)
                        {
                            break;
                        }

                        //Añadir metralla a cilindros
                        processPipeString(gmlList._string,theId,internalMat,externalMat,intDiam,extDiam,pipeClass,pipeType,
                                isTransport,isCommunication);

                        //Dar la vuelta.
                        compPos = compPosEnd + 1;
                        theId = theId +1;

                    }
                    else break;

                }
            }
            // Change position and win.
            pos = endPos + 1;
        }
    }

    /*public static void parseContent(String cityGMLString)
    {

        ILogger.instance().logInfo("UtilityParser starting parse");

        int pos = 0;
        final int length = cityGMLString.length();
        while (pos < length)
        {

            int startPos = cityGMLString.indexOf("<utility:featureGraphMember>", pos);
            int endPos = cityGMLString.indexOf("</utility:featureGraphMember>", pos);

            if (startPos == length-1 || startPos == -1){
                break;
            }
            pos = cityGMLString.indexOf("<utility:FeatureGraph gml:id=",startPos);

            while (true)
            {
                IStringUtils.StringExtractionResult gmlList = IStringUtils.extractSubStringBetween(cityGMLString, "<gml:posList>", "</gml:posList>", pos);
                if (gmlList._endingPos == length-1 || gmlList._endingPos >= endPos)
                {
                    break;
                }


                IStringUtils.StringExtractionResult firstPoint = IStringUtils.extractSubStringBetween(cityGMLString, "<gml:pos>", "</gml:pos>", gmlList._endingPos);

                if (firstPoint._endingPos == length-1 || firstPoint._endingPos >= endPos)
                {
                    break;
                }

                IStringUtils.StringExtractionResult secondPoint = IStringUtils.extractSubStringBetween(cityGMLString, "<gml:pos>", "</gml:pos>", firstPoint._endingPos);

                if (secondPoint._endingPos == length-1 || secondPoint._endingPos >= endPos)
                {
                    break;
                }

                processPipeString(gmlList._string);

                pos = cityGMLString.indexOf("</utility:FeatureGraph>",pos);

            }
            ILogger.instance().logError("A pipe has been processed ...");
            pos = endPos + 1;
            //if (endPos == 0) break;

        }
        PipesModel.loadedFiles++;
    }*/

    public static void parseFromURL(URL url)
    {

        if (_downloader == null)
        {
            throw new RuntimeException("UtilityParser not initialized");
        }

        _downloader.requestBuffer(url, DownloadPriority.HIGHEST, TimeInterval.fromHours(1), true, new IBufferDownloadListener() {
            @Override
            public void onDownload(URL url, IByteBuffer buffer, boolean expired) {
                final String s = buffer.getAsString();
                if (buffer != null)
                    buffer.dispose();
                parseContent(s);
                /*_tUtils.invokeAsyncTask(new GAsyncTask() {
                    @Override
                    public void runInBackground(G3MContext context) {
                        ILogger.instance().logError("Let the parsing begin ...");
                        parseContent(s);
                    }

                    @Override
                    public void onPostExecute(G3MContext context) { }
                },true);*/

            }

            @Override
            public void onError(URL url) {}

            @Override
            public void onCancel(URL url) {}

            @Override
            public void onCanceledDownload(URL url, IByteBuffer buffer, boolean expired) {}
        }, true);
    }
}
