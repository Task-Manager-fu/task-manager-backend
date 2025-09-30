package com.example.Task

import com.example.user.User
import com.example.user.UsersTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.time.LocalDateTime

class TaskRepository {

    private fun rowToTaskEntity(row: ResultRow): TaskEntity {
        return TaskEntity(
            id = row[TasksTable.id].value,
            teamId = row[TasksTable.teamId],
            title = row[TasksTable.title],
            description = row[TasksTable.description],
            status = row[TasksTable.status],
            deadline = row[TasksTable.deadline].toString(),
            priority = row[TasksTable.priority],
            createdAt = row[TasksTable.createdAt].toEpochMilli(),
            updatedAt = row[TasksTable.updatedAt].toEpochMilli()
        )
    }
    suspend fun createTask(
        teamId: Int,
        title: String?,
        description: String?,
        assignedTo: List<Int>?,
        deadline: String?,
        status: TaskStatus,
        priority: Int?,
    ): TaskEntity =
        newSuspendedTransaction {
            val insertStatement = TasksTable.insert {
                it[TasksTable.teamId] = teamId
                it[TasksTable.title] = title!!
                it[TasksTable.description] = description!!
                it[TasksTable.status] = status
                it[TasksTable.priority] = priority!!
                //todo: the deadline validation should be checked in TaskService class
                if (deadline != null) it[TasksTable.deadline] = LocalDateTime.parse(deadline)
                it[TasksTable.createdAt] = Instant.now()
                it[TasksTable.updatedAt] = Instant.now()
            }
            val taskId = insertStatement[TasksTable.id].value
            assignedTo?.forEach { userId ->
                TaskAssignmentsTable.insert {
                    it[TaskAssignmentsTable.userId] = userId
                    it[TaskAssignmentsTable.taskId] = taskId
                }
            }
        TasksTable.selectAll().where { TasksTable.id eq taskId }.single().let(::rowToTaskEntity)
        }

    suspend fun updateTask(
        taskId: Int,
        title: String?,
        description: String?,
        deadline: String?,
        status: TaskStatus,
        priority: Int?,
    ): TaskEntity = newSuspendedTransaction {
        TasksTable.update({ TasksTable.id eq taskId }) {
            it[TasksTable.title] = title!!
            it[TasksTable.description] = description!!
            it[TasksTable.status] = status
            //todo: the deadline validation should be checked in TaskService class
            if (deadline != null) it[TasksTable.deadline] = LocalDateTime.parse(deadline)
            it[TasksTable.priority] = priority!!
            it[TasksTable.updatedAt] = Instant.now()
        }
            TasksTable.selectAll()
                .where { TasksTable.id eq taskId }
                .single()
                .let (::rowToTaskEntity )
    }
    suspend fun deleteTask(taskId: Int): Boolean{
        newSuspendedTransaction {
            TaskAssignmentsTable.deleteWhere {
                (TaskAssignmentsTable.taskId eq taskId)
            }
            TasksTable.deleteWhere { TasksTable.id eq taskId }
        }
        return true
    }
    suspend fun assignTaskToUser(userIds: List<Int>, taskId: Int){
        newSuspendedTransaction {
            userIds.forEach { userId ->
                TaskAssignmentsTable.insert {
                    it[TaskAssignmentsTable.userId] = userId
                    it[TaskAssignmentsTable.taskId] = taskId
                }
            }
            TasksTable.update({ TasksTable.id eq taskId }) {
                it[TasksTable.updatedAt]=Instant.now()
            }
        }
    }
    suspend fun getMyTasks(userId: Int , page: Int ,pageSize: Int): List<TaskEntity> =
        newSuspendedTransaction {
            val tasks = (TasksTable innerJoin TaskAssignmentsTable)
                .selectAll()
                .where{ TaskAssignmentsTable.userId eq userId }
                .map { rowToTaskEntity(it) }
            val fromIndex = (page - 1) * pageSize
            val toIndex = (fromIndex + pageSize).coerceAtMost(tasks.size)
            if (fromIndex >= tasks.size) emptyList()
            else tasks.subList(fromIndex, toIndex)
        }

    suspend fun getTasksOfTeam(teamId: Int, page: Int, pageSize: Int): List<TaskDTO> =
        newSuspendedTransaction{
            val tasksQuery = TasksTable
                .selectAll()
                .where{ TasksTable.teamId eq teamId }
                .limit(pageSize, ((page - 1) * pageSize).toLong())

            val tasks = tasksQuery.associate { row ->
                row[TasksTable.id].value to TaskDTO(
                    id = row[TasksTable.id].value,
                    teamId = row[TasksTable.teamId],
                    title = row[TasksTable.title],
                    description = row[TasksTable.description],
                    status = row[TasksTable.status],
                    createdAt = row[TasksTable.createdAt].toEpochMilli(),
                    updatedAt = row[TasksTable.updatedAt].toEpochMilli(),
                    assignedToIds = mutableListOf()
                )
            }

            val taskIds = tasks.keys.toList()
            if (taskIds.isNotEmpty()) {
                val assignments = (TaskAssignmentsTable innerJoin UsersTable)
                    .selectAll()
                    .where{ TaskAssignmentsTable.taskId inList taskIds }
                    .map {
                        row ->
                        val taskId = row[TaskAssignmentsTable.taskId].value
                        val user = User(
                            id = row[UsersTable.id],
                            username = row[UsersTable.username],
                            email = row[UsersTable.email],
                            role = row[UsersTable.role],
                            isActive = row[UsersTable.isActive],
                            avatar = row[UsersTable.avatar],
                            bio = row[UsersTable.bio]
                        )
                        taskId to user
                    }

                assignments.forEach { pair ->
                    val (taskId, user) = pair
                    tasks[taskId]?.assignedToUsers?.add(user)
                    tasks[taskId]?.assignedToIds?.add(user.id!!)
                }
            }

            tasks.values.toList()
        }
    suspend fun changeTaskStatus(taskId: Int, status: TaskStatus){
        newSuspendedTransaction {
            TasksTable.update({ TasksTable.id eq taskId }) {
                it[TasksTable.status] = status
                it[TasksTable.updatedAt] = Instant.now()
            }
        }
    }


}