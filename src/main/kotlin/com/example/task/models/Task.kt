package com.example.task.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Class used to create the priority of a task.
 * This enumeration define 4 types of priority:
 * - Low
 * - Medium
 * - High
 * - Vital
 */
enum class Priority {
    Low, Medium, High, Vital
}

/**
 * Object used to serialize LocalDateTime.
 * @property formatter The format of the date.
 */
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


/**
 * Object used to serialize UUID.
 */
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}

/**
 * Class used to create task.
 * @property id The id of the task.
 * @property name The name of the task.
 * @property description The description of the task.
 * @property priority The priority of the task.
 * @property dueDate The date till the task have to be completed.
 * @property dateDeleted The date when the task was deleted.
 * @property dateCompleted The date when the task was completed.
 */
@Serializable
data class Task(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val description: String,
    val priority: Priority,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dueDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dateDeleted: LocalDateTime?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dateCompleted: LocalDateTime?,
)

/**
 * Class used to create task without id
 * @property name The name of the task
 * @property description The description of the task
 * @property priority The priority of the task
 * @property dueDate The date till the task have to be completed
 */
@Serializable
data class NewTask(
    val name: String,
    val description: String,
    val priority: Priority,
    @Serializable(with = LocalDateTimeSerializer::class)
    val dueDate: LocalDateTime,
)