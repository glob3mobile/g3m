//
//  UtilityNetworkParser.cpp
//  EIFER App
//
//  Created by Chano on 30/11/17.
//
//

#include "UtilityNetworkParser.hpp"

#include "PipesModel.hpp"
#include <stdio.h>

#include <G3MiOSSDK/IBufferDownloadListener.hpp>
/*

*/

class UtilityNetworkBufferListener : public IBufferDownloadListener {
public:
    /**
     Callback method invoked on a successful download.  The buffer has to be deleted in C++ / .disposed() in Java
     */
    void onDownload(const URL& url, IByteBuffer* buffer, bool expired){
        const std::string s = buffer->getAsString();
        if (buffer != NULL)
            delete buffer;
        UtilityNetworkParser::parseContent(s);
    };
    
    /**
     Callback method invoke after an error trying to download url
     */
    void onError(const URL& url){};
    
    /**
     Callback method invoke after canceled request
     */
    void onCancel(const URL& url){};
    
    /**
     This method will be call, before onCancel, when the data arrived before the cancelation.
     
     The buffer WILL be deleted/disposed after the method finishs.  If you need to keep the buffer, use shallowCopy() to store a copy of the buffer.
     */
    void onCanceledDownload(const URL& url,
                                    IByteBuffer* buffer,
                                    bool expired){};
    
};


const Planet * UtilityNetworkParser::_planet;
IDownloader * UtilityNetworkParser::_downloader;
const ElevationData * UtilityNetworkParser::_elevationData;
MeshRenderer * UtilityNetworkParser::_mr;
double UtilityNetworkParser::_hOffset;
const IThreadUtils * UtilityNetworkParser::_tUtils;


void UtilityNetworkParser::initialize (const G3MContext *context,
                                       const ElevationData *elevationData,
                                       MeshRenderer *mr,
                                       double heightOffset)
{
    _planet = context->getPlanet();
    _downloader = context->getDownloader();
    _elevationData = elevationData;
    _mr = mr;
    _hOffset = heightOffset;
    _tUtils = context->getThreadUtils();
}

void UtilityNetworkParser::processPipeString(const std::string &pipeList, int theId, std::string iMat, std::string eMat, double iDiam, double eDiam, std::string pClass, std::string pType, bool isT, bool isC){
    
    NSString *s = [NSString stringWithFormat:@"%s",pipeList.c_str()];
    NSArray* pipes = [s componentsSeparatedByString:@" "];

    for (size_t i=0; i<(pipes.count - 3); i = i+3){
        double lon = ((NSString*)pipes[i]).doubleValue;
        double lat = ((NSString*)pipes[i+1]).doubleValue;
        double h = ((NSString*)pipes[i+2]).doubleValue;
        
        double lon2 = ((NSString*)pipes[i+3]).doubleValue;
        double lat2 = ((NSString*)pipes[i+4]).doubleValue;
        double h2 = ((NSString*)pipes[i+5]).doubleValue;
        
        Geodetic3D start = Geodetic3D::fromDegrees(lat,lon,h);
        Geodetic3D end = Geodetic3D::fromDegrees(lat2,lon2,h2);
        PipesModel::insertNewCylinder(start,end,_planet, _mr, _elevationData,_hOffset, theId, iMat, eMat, iDiam, eDiam, pClass, pType, isT, isC);
    }
}

void UtilityNetworkParser::parseContent(const std::string &cityGMLString){
    ILogger::instance()->logInfo("UtilityParser starting parse");
        
    std::string internalMat = "", externalMat = "", pipeType;
        
    size_t pos = 0; int theId = 0;
    const size_t length = cityGMLString.length();
    while (pos < length) {
        size_t startPos = cityGMLString.find("<core:cityObjectMember>", pos);
        size_t endPos = cityGMLString.find("</core:cityObjectMember>", pos);
            
        if (startPos == std::string::npos) { // || startPos == -1){
            break;
        }
            
        IStringUtils::StringExtractionResult COMember =
            IStringUtils::extractSubStringBetween(cityGMLString, "<core:cityObjectMember>", "</core:cityObjectMember>", pos);
            
        size_t emPos = COMember._string.find("</utility:ExteriorMaterial>",0);
        size_t imPos = COMember._string.find("</utility:InteriorMaterial>",0);
        if (emPos != std::string::npos){
                //Detect external material
            IStringUtils::StringExtractionResult type =
                IStringUtils::extractSubStringBetween(COMember._string, "<utility:type>", "</utility:type>", 0);
            externalMat = type._string;
        }
        else if (imPos != std::string::npos){
            IStringUtils::StringExtractionResult type =
                IStringUtils::extractSubStringBetween(COMember._string, "<utility:type>", "</utility:type>", 0);
            internalMat = type._string;
        }
        else {
            size_t typePos = COMember._string.find("</utility:transportedMedium>",0);
            if (typePos != std::string::npos){
                IStringUtils::StringExtractionResult type =
                    IStringUtils::extractSubStringBetween(COMember._string, "<utility:type>", "</utility:type>", 0);
                pipeType = type._string;
            }
            else {
                //Autoassign
                pipeType = "High power";
            }
            //Second mission == Iterate over networks, idea: Catch Components.
            size_t compPos = 0;
            while (true){
                compPos = COMember._string.find("<utility:component>",compPos);
                size_t compPosEnd = COMember._string.find("</utility:component>",compPos);
                //Revisar cómo funciona esta parte.
                if (compPos != std::string::npos){
                    //Componente activo: desglosarlo!!!!! y buscar el resto de metralla.
                    IStringUtils::StringExtractionResult component =
                    IStringUtils::extractSubStringBetween(COMember._string, "<utility:component>", "</utility:component>", compPos);
                        
                    std::string pipeClass;
                    double extDiam, intDiam;
                    bool isTransport = false, isCommunication = false;
                        
                    size_t cablePos = component._string.find("</utility:Cable>");
                    size_t pipePos = component._string.find("</utility:RoundPipe>");
                    if (cablePos != std::string::npos) {
                        pipeClass = "Cable";
                        IStringUtils::StringExtractionResult crossSection =
                        IStringUtils::extractSubStringBetween(component._string,
                                                                 "<utility:crossSection uom=\"cm\">", "</utility:crossSection>", 0);
                        extDiam = std::atof(crossSection._string.c_str());
                        intDiam = extDiam;
                        IStringUtils::StringExtractionResult isT =
                        IStringUtils::extractSubStringBetween(component._string,
                                                                 "<utility:isTransmission>", "</utility:isTransmission>", 0);
                        isTransport = (isT._string == "true") ? true : false;
                        IStringUtils::StringExtractionResult isC =
                        IStringUtils::extractSubStringBetween(component._string,
                                                                 "<utility:isCommunication>", "</utility:isCommunication>", 0);
                        isCommunication = (isC._string == "true") ? true : false;
                    }
                    else{
                        if (pipePos != std::string::npos) {
                            pipeClass = "RoundPipe";
                            IStringUtils::StringExtractionResult exteriorSection =
                            IStringUtils::extractSubStringBetween(component._string,
                                                                     "<utility:exteriorDiameter>", "</utility:exteriorDiameter>", 0);
                            extDiam = std::atof(exteriorSection._string.c_str());
                            IStringUtils::StringExtractionResult interiorSection =
                            IStringUtils::extractSubStringBetween(component._string,
                                                                     "<utility:interiorDiameter>", "</utility:interiorDiameter>", 0);
                            intDiam = std::atof(interiorSection._string.c_str());
                        }
                        else
                            break;
                    }
                        
                    IStringUtils::StringExtractionResult gmlList =
                    IStringUtils::extractSubStringBetween(component._string, "<gml:posList>", "</gml:posList>", 0);
                    if (gmlList._endingPos == std::string::npos)
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

void UtilityNetworkParser::parseFromURL(URL &url)
{
    
    if (_downloader == NULL)
    {
        throw ("UtilityParser not initialized");
    }
    
    _downloader->requestBuffer(url, DownloadPriority::HIGHEST, TimeInterval::fromHours(1), true, new UtilityNetworkBufferListener(), true);
}

