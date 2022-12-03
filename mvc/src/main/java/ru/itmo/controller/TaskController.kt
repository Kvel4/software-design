package ru.itmo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import ru.itmo.repository.TaskRepository

@Controller
class TaskController(
    private val taskRepository: TaskRepository
) {

    @GetMapping("/")
    fun getTasks(modelMap: ModelMap): String {
        modelMap["listsTasks"] = taskRepository.findAllListsTasks()

        return "index";
    }

    @GetMapping("/get-list-tasks")
    fun getTasksByList(@ModelAttribute("listId") id: Int, modelMap: ModelMap, ): String {
        prepareTasks(modelMap, id)

        return "tasks"
    }

    @PostMapping("/create-new-list-tasks")
    fun createNewListTasks(@ModelAttribute("listName") listName: String): String {
        taskRepository.saveListTasks(listName)

        return "redirect:/";
    }

    @PostMapping("/delete-list-tasks")
    fun deleteListTasks(@ModelAttribute("listId") id: Int): String {
        taskRepository.deleteListTasks(id)

        return "redirect:/"
    }

    @PostMapping("/add-new-task")
    fun addTask(
        @ModelAttribute("listId") id: Int,
        @ModelAttribute("taskName") taskName: String,
        modelMap: ModelMap,
    ): String {
        taskRepository.saveTask(id, taskName)

        prepareTasks(modelMap, id)
        return "tasks"
    }

    @PostMapping("/delete-task")
    fun deleteTask(
        @ModelAttribute("listId") listId: Int,
        @ModelAttribute("taskId") taskId: Int,
        modelMap: ModelMap,
    ): String {
        taskRepository.deleteTask(taskId)

        prepareTasks(modelMap, listId)
        return "tasks"
    }

    @PostMapping("/done-task")
    fun doneTask(
        @ModelAttribute("listId") listId: Int,
        @ModelAttribute("taskId") taskId: Int,
        modelMap: ModelMap,
    ): String {
        taskRepository.markDoneTask(taskId)

        prepareTasks(modelMap, listId)
        return "tasks"
    }

    private fun prepareTasks(modelMap: ModelMap, id: Int) {
        modelMap["listId"] = taskRepository.findListById(id).get().listId
        modelMap["listName"] = taskRepository.findListById(id).get().listName
        modelMap["tasks"] = taskRepository.findTasksByListId(id)
    }
}