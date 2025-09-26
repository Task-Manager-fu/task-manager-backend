package com.example.Task

import com.example.exceptions.AccessDeniedCustom
import com.example.team.TeamRepository

class TaskService(private val taskRepository: TaskRepository,
                  private val teamRepository: TeamRepository) {
    suspend fun addTask(task: TaskDTO , userId: Int): TaskEntity {
        if (!isAdmin(task.teamId!! , userId))
            throw AccessDeniedCustom("You are not allowed to add task to this team")
        return taskRepository.createTask(
            task.teamId,
            task.title,
            task.description,
            task.assignedToIds,
            task.deadline,
            task.status!!,
            task.priority
        )
    }
    suspend fun updateTask(task: TaskEntity, userId: Int ,teamId: Int , taskId: Int): TaskEntity {
        if (!isAdmin(teamId, userId))
            throw AccessDeniedCustom("You are not allowed to edit task to this team")
        return taskRepository.updateTask(
            taskId = taskId,
            title = task.title,
            description = task.description,
            deadline = task.deadline,
            status = task.status!!,
            priority = task.priority
        )
    }
    suspend fun deleteTask(taskId: Int , teamId: Int , userId: Int): Boolean {
        if (!isAdmin(teamId, userId)) throw AccessDeniedCustom("You are not allowed to delete task")
        taskRepository.deleteTask(taskId)
        return true
    }
    suspend fun assignTaskToUser(taskId: Int, userIds: List<Int> , userId: Int , teamId: Int){
        if (!isAdmin(teamId, userId)) throw AccessDeniedCustom("You are not allowed to assign task to these users")
        taskRepository.assignTaskToUser(taskId = taskId , userIds = userIds )
    }
    suspend fun getMyTasks(userId: Int , page: Int , pageSize: Int): List<TaskEntity> {
        return taskRepository.getMyTasks(page = page, pageSize = pageSize , userId = userId)
    }
    suspend fun getTasksOfTeam(teamId : Int , page: Int ,  pageSize: Int , userId: Int): List<TaskDTO> {
        val team = teamRepository.getTeamById(teamId)
        if (!team?.users?.any { it.id == userId }!!)
            throw AccessDeniedCustom("Access denied")
        return taskRepository.getTasksOfTeam(teamId = teamId , page = page, pageSize = pageSize)
    }
    suspend fun changeTaskStatus(teamId: Int , taskId: Int, status: TaskStatus , userId: Int){
        val team = teamRepository.getTeamById(teamId)
        if (!team?.users?.any { it.id == userId }!!) throw AccessDeniedCustom("Access denied")
        return taskRepository.changeTaskStatus(taskId , status )
    }
    private suspend fun isAdmin(teamId: Int , userId: Int ): Boolean {
        val team = teamRepository.getTeamById(teamId)
        if (team?.creatorId == userId) {
            return true
        }
        return false
    }
}