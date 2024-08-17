package org.roko.dls.api.service.util.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class SublockConfigTest {

    private static final String TEST_CONFIG_FILE_PATH = "./src/test/resources/sublock_config.json";

    private static final String SUBLOCK1_URL = "http://sublock1:8080";
    private static final String SUBLOCK2_URL = "http://sublock2:8080";
    private static final String SUBLOCK3_URL = "http://sublock3:8080";

    private static final int EXPECTED_NUMBER_OF_SUBLOCK_CLIENTS = 3;

    @Test
    public void sublockConfigIsProperlyParsed() throws JsonSyntaxException, JsonIOException, FileNotFoundException{
        Gson gson = new Gson();

        SublockConfig sublockConfig = gson.fromJson(readTestConfigFile(), SublockConfig.class);

        assertEquals(EXPECTED_NUMBER_OF_SUBLOCK_CLIENTS, sublockConfig.getSublockClients().size());

        assertEquals(SUBLOCK1_URL, sublockConfig.getSublockClients().get(0).getUrl());
        assertEquals(SUBLOCK2_URL, sublockConfig.getSublockClients().get(1).getUrl());
        assertEquals(SUBLOCK3_URL, sublockConfig.getSublockClients().get(2).getUrl());
    }

    private FileReader readTestConfigFile() throws FileNotFoundException{
        File f = new File(TEST_CONFIG_FILE_PATH);
        return new FileReader(f);
    }
}
