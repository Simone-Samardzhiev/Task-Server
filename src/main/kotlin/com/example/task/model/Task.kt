package com.example.task.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

// Class used to set priority to the tasks
enum class Priority {
    Low, Medium, High, Vital
}

// Object used to serialize LocalDate
object LocalDateSerializer : KSerializer<LocalDate> {
    // The formatter of the date
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}

// Object used to serialize LocalDateTime
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    // The formatter of the date
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}

// Object used to serialize UUID
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}

// Class used to store task
@Serializable
data class Task(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val description: String,
    val priority: Priority,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dueDate: LocalDateTime,
    @Serializable(with = LocalDateSerializer::class)
    val dateDeleted: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val dateCompleted: LocalDate,
)

// Class used to create task without id
@Serializable
data class TaskWithoutId(
    val name: String,
    val description: String,
    val priority: Priority,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dueDate: LocalDateTime,
    @Serializable(with = LocalDateSerializer::class)
    val dateDeleted: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val dateCompleted: LocalDate
)