function generateFootprints(filename)
    fileID = fopen(filename,'r');
    A = fscanf(fileID,'%f',Inf);
    polygons = {};
    i = 1; ct = 1;
    while (i < length(A))
        t = A(i);
        vx = A(i+1:2:(t*2)+i);
        vy = A(i+2:2:(t*2)+i);
        m = [ min(vx),max(vx),min(vy),max(vy)];
        polygons(ct,:) = {m,vx,vy};
        i = i + (t*2) + 1;
        ct = ct + 1;
    end
    save('polygons.mat','polygons');    
end