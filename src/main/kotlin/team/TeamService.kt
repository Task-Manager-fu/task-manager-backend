package com.example.team

import com.example.exceptions.AccessDeniedCustom
import io.ktor.server.plugins.NotFoundException

class TeamService(private val repository:  TeamRepository) {
    suspend fun createTeam(name: String, description: String?, userIds: List<Int> , creatorId: Int) : TeamEntity {
        val filteredUserIds = userIds.toMutableList().apply { add(creatorId)  }
        return repository.createTeam(name, description, filteredUserIds , creatorId)
    }

    suspend fun getTeam(teamId: Int): TeamDTO? {
        return repository.getTeamById(teamId)
    }
    suspend fun getAllTeams(userId: Int , pageSize: Int , pageNumber: Int): List<TeamEntity> {
        return repository.getTeamsByUser(userId , page = pageNumber , pageSize = pageSize)
    }
    suspend fun addUserToTeam(teamId: Int , teamDTO: TeamDTO , userId: Int) : Boolean {
        if (!isAdmin(teamId , userId)) throw AccessDeniedCustom("You are not allowed to add user to this team")
        return repository.addUserToTeam(teamId , teamDTO.userIds)
    }
    suspend fun removeUserFromTeam(teamId: Int , userId: Int , creatorId: Int): Boolean {
        if (!isAdmin(teamId , creatorId)) throw AccessDeniedCustom("You are not allowed to remove user from this team")
        return repository.removeUserFromTeam(teamId , userId)
    }
    suspend fun deleteTeam(teamId: Int , creatorId: Int): Boolean {
        val team  = repository.getTeamById(teamId)?: throw NotFoundException("Team $teamId not found")
        val userIds = team.userIds
        if (!isAdmin(teamId = teamId, userId = creatorId)) {
            throw AccessDeniedCustom("You are not allowed to delete this team")
        }
        return repository.deleteTeam(teamId , userIds)
    }
    suspend fun updateTeam(teamId: Int , userId: Int , teamEntity: TeamEntity): TeamEntity {
        if (!isAdmin(teamId = teamId, userId = userId)) {
            throw AccessDeniedCustom("You are not allowed to update this team")
        }
        return repository.updateTeam(teamId  , teamEntity.name!!, teamEntity.description!!)
    }

    private suspend fun isAdmin(teamId: Int , userId: Int ): Boolean {
        val team = repository.getTeamById(teamId)
        if (team?.creatorId == userId) {
            return true
        }
        return false
    }
}