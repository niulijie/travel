package com.niulijie.ucenter.validate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SqlTransformDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String reValue = node.asText();
        reValue = reValue.replace("\\", "\\\\");
        reValue = reValue.replace("_", "\\_");
        reValue = reValue.replace("%", "\\%");

        return reValue;
    }
}
