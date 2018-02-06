function readLas(filename,output)
	fileId = fopen(filename);
	A = fread(fileId,10^100,'uint8=>uint8');
	[header] = readHeaderBlock(A');
	if (header.numberVariableLengthRecords) 
		variableRecords= readVariableRecords(A',header);
	end
	[points] = readPoints(A',header);
	fclose(fileId);
    save(output,'header','points');
end

function [points] = readPoints(A, header)
    theCounter = header.offsetToPointData+1;
    points = zeros(header.pointRecords,6);
    %i = 2;
    for i = 1:header.pointRecords
        if (rem(i,1000000) == 0)
            fprintf('%d out of %d points processed \n',i,header.pointRecords);
        end
        points(i,1) = double(typecast(A(theCounter:theCounter+3),'uint32')) * header.xScaleFactor + header.xOffset;
        points(i,2) = double(typecast(A(theCounter+4:theCounter+7),'uint32')) * header.yScaleFactor + header.yOffset;
        points(i,3) = double(typecast(A(theCounter+8:theCounter+11),'uint32')) * header.zScaleFactor + header.zOffset;
        points(i,4) = typecast(A(theCounter+12:theCounter+13),'uint16');
        classificationBits = dec2bin(A(theCounter+15),8);
        classificationBits = bin2dec(classificationBits(4:8));
        points(i,5) = classificationBits;
        returningBits = dec2bin(A(theCounter+14),8);
        retNumber = bin2dec(returningBits(6:8));
        %totalReturns = bin2dec(returningBits(3:5));
        %directionFlag = bin2dec(returningBits(2));
        %endFlag = bin2dec(returningBits(1));
        %fprintf('Point:%d, RetNumber: %d, totalRets: %d, dir: %d, end:%d \n',i,retNumber,totalReturns,directionFlag,endFlag);
        points(i,6) = retNumber;
        theCounter = theCounter + double(header.pointDataRecordLength);
        %fprintf('%f %f %f \n',points(i,1),points(i,2),points(i,3));
    end
    
end

% function [points] = readPoints(A, header);
% 	theCounter = header.offsetToPointData;
% 	%% OJO HAY VARIOS FORMATOS DIFERENTES PARA LEER LIDAR
%     %%points(header.pointRecords) = struct;
% 	for i = 1:header.pointRecords
%         if (rem(i,1000) == 0)
%             fprintf('%d out of %d points processed \n',i,header.pointRecords);
%         end
%         switch (header.pointDataFormatId) 
%             case 0
%                 x = typecast(A(theCounter:theCounter+3),'uint32');
%                 y = typecast(A(theCounter+4:theCounter+7),'uint32');
%                 z = typecast(A(theCounter+8:theCounter+11),'uint32');
%                 intensity = typecast(A(theCounter+12:theCounter+13),'uint16');
%                 theByte = A(theCounter+14);
%                 classification = A(theCounter+15);
%                 scanAngleRank = char(A(theCounter+16));
%                 userData = char(A(theCounter+17));
%                 pointSource = typecast(A(theCounter+12:theCounter+13),'uint16');
%                 % Operate
%                 x = x * header.xScaleFactor + header.xOffset;
%                 y = y * header.yScaleFactor + header.yOffset;
%                 z = z * header.zScaleFactor + header.zOffset;
%                 theByteBits = dec2bin(theByte);
%                 classificationBits = dec2bin(classification);
%                 
%                 returnNumber = bin2dec(theByteBits(1:3));
%                 numberOfReturns = bin2dec(theByteBits(4:6));
%                 scanDirectionFlag = theByteBits(7);
%                 edgeFlightLine = theByteBits(8);
%                 classification = classify(bin2dec(classificationBits(1:5)));
%                 isSynthetic = classificationBits(6);
%                 isKeypoint = classificationBits(7);
%                 isDeleted = classificationBits(8);
%                 
%                 if (i == 1)
%                     points(header.pointRecords) = struct('x',x,'y',y,'z',z,'intensity',intensity,'returnNumber',returnNumber,'numberOfReturns',numberOfReturns, ...
%                     'scanDirectionFlag',scanDirectionFlag, 'edgeFlightLine', edgeFlightLine','classification', classification, 'isSynthetic', ...
%                     isSynthetic, 'isKeypoint', isKeypoint, 'isDeleted', isDeleted, 'scanAngleRank', scanAngleRank, 'userData', userData, ...
%                     'pointSource', pointSource);
%                 end
%                 points(i) = struct('x',x,'y',y,'z',z,'intensity',intensity,'returnNumber',returnNumber,'numberOfReturns',numberOfReturns, ...
%                     'scanDirectionFlag',scanDirectionFlag, 'edgeFlightLine', edgeFlightLine','classification', classification, 'isSynthetic', ...
%                     isSynthetic, 'isKeypoint', isKeypoint, 'isDeleted', isDeleted, 'scanAngleRank', scanAngleRank, 'userData', userData, ...
%                     'pointSource', pointSource);
%                 break;
%             case 1
%                 %% DO STUFF
%                 break;
%             case 2
%                 %% DO STUFF
%                 break;
%             case 3
%                 x = typecast(A(theCounter:theCounter+3),'uint32');
%                 y = typecast(A(theCounter+4:theCounter+7),'uint32');
%                 z = typecast(A(theCounter+8:theCounter+11),'uint32');
%                 intensity = typecast(A(theCounter+12:theCounter+13),'uint16');
%                 theByte = A(theCounter+14); %% 
%                 classificationByte = A(theCounter+15); %%
%                 scanAngleRank = char(A(theCounter+16));
%                 userData = char(A(theCounter+17));
%                 pointSource = typecast(A(theCounter+18:theCounter+19),'uint16');
%                 GPSTime = typecast(A(theCounter+20:theCounter+27),'double');
%                 red = typecast(A(theCounter+28:theCounter+29),'uint16');
%                 green = typecast(A(theCounter+30:theCounter+31),'uint16');
%                 blue = typecast(A(theCounter+32:theCounter+33),'uint16');
%                 % Operate
%                 x = x * header.xScaleFactor + header.xOffset;
%                 y = y * header.xScaleFactor + header.xOffset;
%                 z = z * header.xScaleFactor + header.xOffset;
%                 theByteBits = dec2bin(theByte,8);
%                 classificationBits = dec2bin(classificationByte,8);
%                 
%                 returnNumber = bin2dec(theByteBits(1:3));
%                 numberOfReturns = bin2dec(theByteBits(4:6));
%                 scanDirectionFlag = theByteBits(7);
%                 edgeFlightLine = theByteBits(8);
%                 classification = classify(bin2dec(classificationBits(1:5)));
%                 isSynthetic = classificationBits(6);
%                 isKeypoint = classificationBits(7);
%                 isDeleted = classificationBits(8);
%                 if (i == 1)
%                      points(header.pointRecords) = struct('x',x,'y',y,'z',z,'intensity',intensity,'returnNumber',returnNumber,'numberOfReturns',numberOfReturns, ...
%                     'scanDirectionFlag',scanDirectionFlag, 'edgeFlightLine', edgeFlightLine','classification', classification, 'isSynthetic', ...
%                     isSynthetic, 'isKeypoint', isKeypoint, 'isDeleted', isDeleted, 'scanAngleRank', scanAngleRank, 'userData', userData, ...
%                     'pointSource', pointSource, 'GPSTime', GPSTime, 'red', red, 'green', green, 'blue', blue);
%                 end
%                 points(i) = struct('x',x,'y',y,'z',z,'intensity',intensity,'returnNumber',returnNumber,'numberOfReturns',numberOfReturns, ...
%                     'scanDirectionFlag',scanDirectionFlag, 'edgeFlightLine', edgeFlightLine','classification', classification, 'isSynthetic', ...
%                     isSynthetic, 'isKeypoint', isKeypoint, 'isDeleted', isDeleted, 'scanAngleRank', scanAngleRank, 'userData', userData, ...
%                     'pointSource', pointSource, 'GPSTime', GPSTime, 'red', red, 'green', green, 'blue', blue);
%         end
% 		
% 		
% 		theCounter = theCounter + double(header.pointDataRecordLength);
% 	end
% end

function c = classify(value)
    switch(value)
        %% OJO ES LITTLE ENDIAN. WHATEVER IT MEANS ...
        case 0
            c = 'Unclassified';
        case 1
            c = 'Unclassified';
        case 2
            c = 'Ground';
        case 3
            c = 'LowVeg';
        case 4
            c = 'MedVeg';
        case 5
            c = 'HighVeg';
        case 6
            c = 'Building';
        case 7
            c = 'LowPoint';
        case 8
            c = 'KeyPoint/Mass Point';
        case 9
            c = 'Water';
        case 12
            c = 'Overlap';
        otherwise
            c = 'Others';
    end
end

function [variableRecords] = readVariableRecords(A)
%%% TODO SOMEDAY
end

function [header] = readHeaderBlock(A)
    signature = char(A(1:4));
    sourceId = typecast(A(5:6),'uint16');
    globalEncoding = typecast(A(7:8),'uint16');
    GUIDdata1 = typecast(A(9:12),'uint32');
    GUIDdata2 = typecast(A(13:14),'uint16');
    GUIDdata3 = typecast(A(15:16),'uint16');
    GUIDdata4 = char(A(17:24));
    versionMajor = char(A(25));
    versionMinor = char(A(26));
    systemId = char(A(27:58));
    generatingSoftware = char(A(59:90));
    creationDay = typecast(A(91:92),'uint16');
    creationYear = typecast(A(93:94),'uint16');
    headerSize = typecast(A(95:96),'uint16');
    offsetToPointData = typecast(A(97:100),'uint32');
    numberVariableLengthRecords = typecast(A(101:104),'uint32');
    pointDataFormatId = A(105);%char(A(105));
    pointDataRecordLength = typecast(A(106:107),'uint16');
    pointRecords = typecast(A(108:111),'uint32');
    numberOfPointsByReturn = typecast(A(112:131),'uint32');
    xScaleFactor = typecast(A(132:139),'double');
    yScaleFactor = typecast(A(140:147),'double');
    zScaleFactor = typecast(A(148:155),'double');
    xOffset = typecast(A(156:163),'double');
    yOffset = typecast(A(164:171),'double');
    zOffset = typecast(A(172:179),'double');
    maxX = typecast(A(180:187),'double');
    minX = typecast(A(188:195),'double');
    maxY = typecast(A(196:203),'double');
    minY = typecast(A(204:211),'double');
    maxZ = typecast(A(212:219),'double');
    minZ = typecast(A(220:227),'double');
    
    header = struct('signature',signature, 'sourceId', sourceId, 'globalEncoding', globalEncoding, 'GUIDdata1', GUIDdata1, 'GUIDdata2', GUIDdata2,... 
    'GUIDdata3', GUIDdata3, 'GUIDdata4', GUIDdata4, 'versionMajor', versionMajor, 'versionMinor', versionMinor, 'systemId', systemId, ...
    'generatingSoftware', generatingSoftware, 'creationDay', creationDay, 'creationYear', creationYear, 'headerSize', headerSize, 'offsetToPointData', ...
    offsetToPointData, 'numberVariableLengthRecords', numberVariableLengthRecords,  'pointDataFormatId', pointDataFormatId, 'pointDataRecordLength', ...
    pointDataRecordLength, 'pointRecords', pointRecords, 'numberOfPointsByReturn', numberOfPointsByReturn, 'xScaleFactor', xScaleFactor, 'yScaleFactor', ...
    yScaleFactor, 'zScaleFactor', zScaleFactor, 'xOffset', xOffset, 'yOffset', yOffset, 'zOffset', zOffset, 'maxX', maxX, 'minX', minX, 'maxY', ...
    maxY, 'minY', minY, 'maxZ', maxZ, 'minZ', minZ);
end