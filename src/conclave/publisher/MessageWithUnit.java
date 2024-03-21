package conclave.publisher;

import atlantis.units.AUnit;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

@JsonSerialize(using = MessageWithUnitSerializer.class)
public class MessageWithUnit {
    private String message;
    private AUnit unit;

    public MessageWithUnit(String message, AUnit unit) {
        this.message = message;
        this.unit = unit;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for unit
    public AUnit getUnit() {
        return unit;
    }

    // Setter for unit
    public void setUnit(AUnit unit) {
        this.unit = unit;
    }
}

class MessageWithUnitSerializer extends JsonSerializer<MessageWithUnit> {
    @Override
    public void serialize(MessageWithUnit message, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Custom serialization logic here
        gen.writeStartObject();
        gen.writeObjectField("message", message.getMessage());
        gen.writeObjectField("unit", message.getUnit());
        gen.writeEndObject();
    }
}