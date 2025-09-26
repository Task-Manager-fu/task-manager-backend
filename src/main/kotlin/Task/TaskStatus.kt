package com.example.Task

enum class TaskStatus (val value: String) {
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    IN_REVIEW("In Review"),
    BLOCKED("Blocked"),
    ON_HOLD("On Hold"),
    DONE("Done"),
    CANCELLED("Cancelled"),
    ARCHIVED("Archived")
}