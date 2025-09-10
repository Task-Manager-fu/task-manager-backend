package com.example.team

import com.example.user.UserRepository

class TeamService(private val repository:  TeamRepository) {
    suspend fun createTeam(name: String, description: String?, userIds: List<Int> , creatorId: Int) : TeamEntity {
        val filteredUserIds = userIds.filter { it != creatorId }
        return repository.createTeam(name, description, filteredUserIds)
    }

}