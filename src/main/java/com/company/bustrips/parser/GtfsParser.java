package com.company.bustrips.parser;

import com.company.bustrips.model.StopTime;
import com.company.bustrips.util.TimeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;

public class GtfsParser {
    private final Path dataDir;
    public GtfsParser(Path dataDir){
        this.dataDir=dataDir;
    }

    private int getColumnIndex(String header, String colName){
        String[] cols= header.split(",");
        for (int i=0;i<cols.length;i++){
            if(cols[i].trim().equalsIgnoreCase(colName)){
                return i;
            }
        }
        return 0;
    }

    private String getColumn(String[] cols, int index) {
        if (index >= cols.length || index < 0) return "";
        return cols[index].trim().replace("\"", "");
    }

    public Optional<String> findStopName(String stationId) throws IOException{
        Path path=dataDir.resolve("stops.txt");
        if(!Files.exists(path)) return Optional.empty();

        try(BufferedReader reader=Files.newBufferedReader(path)){
            String header=reader.readLine();
            int idIndex=getColumnIndex(header,"stop_id");
            int nameIndex=getColumnIndex(header,"stop_name");

            String line;
            while ((line=reader.readLine())!=null){
                String[] cols= line.split(",",-1);
                String currentId = getColumn(cols, idIndex);
                if (currentId.equals(stationId)) {
                    return Optional.of(getColumn(cols, nameIndex));
                }
            }
        }
        return Optional.empty();
    }

}
