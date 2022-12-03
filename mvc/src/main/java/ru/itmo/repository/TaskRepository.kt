package ru.itmo.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import ru.itmo.model.ListTasks
import ru.itmo.model.Task
import java.util.Optional
import javax.sql.DataSource

@Repository
class TaskRepository(
    dataSource: DataSource
) : JdbcTemplate(dataSource) {

    private val listMapper = RowMapper { rs, _ ->
        ListTasks(
            rs.getInt("ListId"),
            rs.getString("ListName"),
        )
    }

    private val taskMapper = RowMapper { rs, _ ->
        Task(
            rs.getInt("TaskId"),
            rs.getString("TaskName"),
            rs.getBoolean("IsReady"),
        )
    }

    fun findAllListsTasks(): List<ListTasks> = query(
        "select ListId, ListName from ListsTasks;",
        listMapper,
    )

    fun findTasksByListId(id: Int): List<Task> = query(
        """
            select TaskId, TaskName, IsReady
            from Tasks
                     natural join ListsTasks
            where ListId = ?;
        """.trimIndent(),
        taskMapper,
        id,
    )

    fun findListById(id: Int): Optional<ListTasks> = query(
        "select ListId, ListName from ListsTasks where ListId = ?;",
        listMapper,
        id,
    ).stream().findFirst()

    fun saveListTasks(listName: String) {
        update("""insert into ListsTasks(ListName) values (?)""", listName)
    }

    fun deleteListTasks(listId: Int) {
        update("""delete from ListsTasks where ListId = ?""", listId)
    }

    fun saveTask(listId: Int, taskName: String) {
        update("""insert into Tasks(ListId, TaskName) values (?, ?)""", listId, taskName)
    }

    fun deleteTask(taskId: Int) {
        update("""delete from Tasks where TaskId = ?;""", taskId)
    }

    fun markDoneTask(taskId: Int) {
        update("""update Tasks set isReady = true where TaskId = ?""", taskId)
    }
}