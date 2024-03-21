package atlantis.units;

import bwapi.Unit;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UnitSerializer extends JsonSerializer<Unit> {
    @Override
    public void serialize(Unit unit, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Custom serialization logic here
        gen.writeStartObject();
        //gen.writeObject(unit.game);
        // Getting game information from the unit is going to be kind of tricky

        gen.writeStringField("Testing", "This is just a test");

//        gen.writeStartObject();
//        gen.writeFieldName("initialType");
//        serializers.findValueSerializer(UnitType.class).serialize(unit.getInitialType(), gen, serializers);
//        gen.writeEndObject();

//        gen.writeObject(unit.getInitialType());
//        gen.writeObject(unit.getInitialResources());
//        gen.writeObject(unit.getInitialHitPoints());
//        gen.writeObject(unit.getInitialPosition());
//        gen.writeObject(unit.getInitialTilePosition());
//        gen.writeObject(unit.getID());
//        gen.writeObject(unit.getPosition());
//        gen.writeObject(unit.getTargetPosition());
//        gen.writeObject(unit.getRallyPosition());
        gen.writeEndObject();
    }
}