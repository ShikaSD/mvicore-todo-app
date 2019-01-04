package com.badoo.mvicore.todo.ui

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.badoo.mvicore.todo.R
import com.badoo.mvicore.todo.model.TodoItem
import com.badoo.mvicore.todo.ui.TodoEvent.*
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

class TodoListAdapter(
    private val events: PublishSubject<TodoEvent>
): ListAdapter<TodoItem, TodoListAdapter.ViewHolder>(DiffCallback()) {

    var items: List<TodoItem> = emptyList()
        set(value) {
            field = value
            submitList(items)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.accept(items[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), Consumer<TodoItem> {
        private val checkBox: CheckBox = itemView.findViewById(R.id.todoItem_checkbox)
        private val delete: View = itemView.findViewById(R.id.todoItem_delete)
        private var item: TodoItem? = null

        init {
            checkBox.setOnClickListener {
                item?.let {
                    events.onNext(MarkedDone(it))
                }
            }

            delete.setOnClickListener {
                item?.let {
                    events.onNext(Deleted(it))
                }
            }
        }

        override fun accept(item: TodoItem) {
            checkBox.text = item.title
            checkBox.isChecked = item.done
            checkBox.setTextColor(
                if (item.done) Color.GRAY else Color.BLACK
            )
            checkBox.paintFlags = if (item.done) checkBox.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else checkBox.paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG.inv())

            this.item = item
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
            oldItem == newItem
    }
}
