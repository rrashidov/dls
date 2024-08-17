package org.roko.dls.api.service.util.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

@Component
public class SublockConfigSupplier {

    public SublockConfig get(String configFilePath){
        File configFile = new File(configFilePath);
        
        try (FileReader fileReader = new FileReader(configFile)) {
            Gson gson = new Gson();
            return gson.fromJson(fileReader, SublockConfig.class);
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            return null;
        }
    }
}
