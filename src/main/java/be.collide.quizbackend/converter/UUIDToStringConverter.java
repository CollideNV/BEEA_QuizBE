package be.collide.quizbackend.converter;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.UUID;

@RegisterForReflection
public class UUIDToStringConverter implements AttributeConverter<UUID> {
    @Override
    public AttributeValue transformFrom(UUID input) {
        return AttributeValue.fromS(input.toString());
    }

    @Override
    public UUID transformTo(AttributeValue input) {
        return UUID.fromString(input.s());
    }

    @Override
    public EnhancedType<UUID> type() {
        return EnhancedType.of(UUID.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
