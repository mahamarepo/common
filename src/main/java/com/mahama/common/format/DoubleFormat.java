package com.mahama.common.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

public class DoubleFormat extends JsonSerializer<Double> {
    DecimalFormat df = new DecimalFormat("###.00");

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        if (value != null) {
            gen.writeNumber(df.format(value));
        }
    }
}
