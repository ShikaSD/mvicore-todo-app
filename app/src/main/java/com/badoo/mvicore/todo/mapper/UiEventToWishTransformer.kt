package com.badoo.mvicore.todo.mapper

import com.badoo.mvicore.todo.feature.TodoListFeature
import com.badoo.mvicore.todo.feature.TodoListFeature.Wish.*
import com.badoo.mvicore.todo.model.TodoItem
import com.badoo.mvicore.todo.ui.TodoEvent
import com.badoo.mvicore.todo.ui.TodoViewModel

object UiEventToWish: (TodoEvent) -> TodoListFeature.Wish? {
    override fun invoke(event: TodoEvent): TodoListFeature.Wish? = when (event) {
        is TodoEvent.MarkedDone -> Update(event.item.copy(done = !event.item.done))
        is TodoEvent.Created -> Create(TodoItem(title = event.title))
        is TodoEvent.Deleted -> Delete(event.item)
    }
}
