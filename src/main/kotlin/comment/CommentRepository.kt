package com.example.comment

import com.example.Task.TasksTable
import com.example.team.TeamsTable
import com.example.team.TeamsUsersTable
import org.h2.api.H2Type.row
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant

class CommentRepository {
    private fun rowToCommentEntity(row: ResultRow): Comment {
        return Comment(
            id = row[CommentsTable.id],
            userId = row[CommentsTable.userId],
            username = row[CommentsTable.username],
            content = row[CommentsTable.content],
            createdAt = row[CommentsTable.createdAt].toEpochMilli(),
            taskId = row[CommentsTable.id],
        )

    }
    suspend fun addComment(userId: Int, taskId: Int, content: String, username: String): Comment =
         newSuspendedTransaction {
             val insertStatement = CommentsTable.insert {
                it[CommentsTable.userId] = userId
                it[CommentsTable.taskId]= taskId
                it[CommentsTable.username]=username
                it[CommentsTable.content] = content
                it[CommentsTable.createdAt] = Instant.now()
            }
             val commentId = insertStatement[CommentsTable.id]
             CommentsTable.selectAll()
                 .where{ CommentsTable.id eq commentId}
                 .single()
                 .let(::rowToCommentEntity)
        }
    suspend fun getComments(taskId: Int , page: Int , pageSize: Int , userId: Int): List<Comment>{
        return newSuspendedTransaction {
            val comments = (CommentsTable innerJoin TasksTable innerJoin TeamsTable).selectAll()
                .where{ CommentsTable.taskId eq taskId }
                .orderBy(CommentsTable.createdAt to SortOrder.ASC)
                .map{
                    val comment = rowToCommentEntity(it)
                    comment.isDeletable = (it[TeamsTable.creatorId] == userId || comment.userId == userId)
                    comment
                }
            val fromIndex = (page - 1) * pageSize
            val toIndex = (fromIndex + pageSize).coerceAtMost(comments.size)
            if (fromIndex >= comments.size) emptyList()
            else comments.subList(fromIndex, toIndex)
        }
    }
    suspend fun getCommentsByUserId(commentId: Int): Comment{
        return newSuspendedTransaction {
            CommentsTable.selectAll()
                .where { CommentsTable.id eq commentId }
                .single()
                .let { rowToCommentEntity(it) }
        }
    }
    suspend fun deleteComment(id: Int): Boolean {
        newSuspendedTransaction {
            CommentsTable.deleteWhere { CommentsTable.id eq id }
        }
        return true
    }
}