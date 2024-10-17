package com.example.task.errors

/**
 * Exception thrown when the task id couldn't be found.
 */
class TaskIdNotFoundException : RuntimeException("The id of the task was not found.")