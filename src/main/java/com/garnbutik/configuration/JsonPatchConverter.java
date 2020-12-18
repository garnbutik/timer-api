package com.garnbutik.configuration;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonReader;
import java.io.StringReader;

public class JsonPatchConverter {

    public JsonPatch convertToJsonPatch(String stringToConvert) {
        JsonReader jsonReader = Json.createReader(new StringReader(stringToConvert));
        return Json.createPatch(jsonReader.readArray());
    }
}
