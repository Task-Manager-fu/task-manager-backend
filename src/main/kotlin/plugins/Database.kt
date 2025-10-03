package com.example.plugins

import com.example.Task.TaskAssignmentsTable
import com.example.Task.TasksTable
import com.example.auth.PasswordResetTokensTable
import com.example.comment.CommentsTable
import com.example.config.AppConfig
import com.example.team.TeamsTable
import com.example.team.TeamsUsersTable
import com.example.user.UsersTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase(cfg: AppConfig) {
    val hc = HikariConfig().apply {
        jdbcUrl = cfg.database.url
        driverClassName = cfg.database.driver
        username = cfg.database.user
        password = cfg.database.password
        maximumPoolSize = 5
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    val ds = HikariDataSource(hc)
    Database.connect(ds)

    transaction {
        SchemaUtils.create(UsersTable)
        SchemaUtils.create(TeamsTable)
        SchemaUtils.create(TeamsUsersTable)
        SchemaUtils.create(TasksTable)
        SchemaUtils.create(TaskAssignmentsTable)
        SchemaUtils.create(CommentsTable)
        SchemaUtils.create(PasswordResetTokensTable)
    }
}
