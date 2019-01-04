package com.badoo.mvicore.todo.ui

import com.badoo.mvicore.todo.model.TodoItem

data class TodoViewModel(
    val todos: List<TodoItem>
)

sealed class TodoEvent {
    data class MarkedDone(val item: TodoItem): TodoEvent()
    data class Deleted(val item: TodoItem): TodoEvent()
    data class Created(val title: String): TodoEvent()
}
