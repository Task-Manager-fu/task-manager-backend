package com.example.comment

import com.example.exceptions.AccessDeniedCustom
import com.example.team.TeamRepository
import javax.print.attribute.standard.RequestingUserName

class CommentService(val repository: CommentRepository , val teamRepository: TeamRepository) {
    suspend fun addComment( userId: Int , username: String , taskId: Int , content: String , teamId: Int): Comment {
        if (!isMemberOfTeam(teamId , userId)) throw AccessDeniedCustom("You are not a member of this team.")
        return repository.addComment(userId , taskId , content , username)
    }
    suspend fun getComments(taskId: Int , userId: Int , page: Int , pageSize: Int, teamId: Int) : List<Comment> {
        if (!isMemberOfTeam(teamId , userId) ) throw AccessDeniedCustom("You are not a member of this team")
        return repository.getComments(taskId , page, pageSize , userId )

    }

    suspend fun deleteComment(userId: Int , commentId: Int  , teamId: Int): Boolean {
        if (!isAdmin(teamId , userId) && !isOwnerOfComment(commentId , userId) ) throw AccessDeniedCustom("You can't delete this comment from this team")
        return repository.deleteComment(commentId)
    }
    private suspend fun isAdmin(teamId: Int , userId: Int ): Boolean {
        val team = teamRepository.getTeamById(teamId)
        if (team?.creatorId == userId) {
            return true
        }
        return false
    }
    private suspend fun isOwnerOfComment(commentId: Int , userId: Int ): Boolean {
        val ownerId = repository.getCommentsByUserId(commentId).userId
        if (ownerId == userId) return true
        else return false
    }
    private suspend fun isMemberOfTeam(teamId: Int , userId: Int ): Boolean {
        val team = teamRepository.getTeamById(teamId)
        team?.userIds?.forEach { user ->
            if (user == userId) {
                return true
            }
        }
        return false
    }
    private fun Comment.toDTO(isDeletable: Boolean) = Comment(
        id = id,
        taskId = taskId,
        userId = userId,
        username = username,
        content = content,
        createdAt = createdAt,
        isDeletable = isDeletable
    )
}