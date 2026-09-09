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

}
