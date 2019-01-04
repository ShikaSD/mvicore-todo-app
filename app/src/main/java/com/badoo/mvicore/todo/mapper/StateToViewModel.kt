package com.badoo.mvicore.todo.mapper

import com.badoo.mvicore.todo.feature.TodoListFeature
import com.badoo.mvicore.todo.model.TodoItem
import com.badoo.mvicore.todo.ui.TodoViewModel

object StateToViewModel: (TodoListFeature.State) -> TodoViewModel {
    override fun invoke(state: TodoListFeature.State): TodoViewModel = 
        TodoViewModel(state.todos.sortedWith(comparator))

    private val comparator: Comparator<TodoItem> = Comparator { todo1, todo2 ->
        val doneCompareResult = todo1.done.compareTo(todo2.done)
         if (doneCompareResult == 0) {
            todo1.id.compareTo(todo2.id)
        } else {
            doneCompareResult
        }
    }
}
