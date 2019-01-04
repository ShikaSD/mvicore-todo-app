package com.badoo.mvicore.todo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badoo.mvicore.android.AndroidTimeCapsule
import com.badoo.mvicore.android.lifecycle.CreateDestroyBinderLifecycle
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.using
import com.badoo.mvicore.todo.R
import com.badoo.mvicore.todo.feature.TodoListFeature
import com.badoo.mvicore.todo.mapper.StateToViewModel
import com.badoo.mvicore.todo.mapper.UiEventToWish
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

class MainActivity(
    private val events: PublishSubject<TodoEvent> = PublishSubject.create()
) : AppCompatActivity(),
    Consumer<TodoViewModel>,
    ObservableSource<TodoEvent> by events {

    private val adapter = TodoListAdapter(events)

    private lateinit var list: RecyclerView
    private lateinit var input: EditText
    private lateinit var submit: Button

    private lateinit var capsule: AndroidTimeCapsule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        capsule = AndroidTimeCapsule(savedInstanceState)
        val feature = TodoListFeature(capsule)

        list = findViewById(R.id.main_todoList)
        input = findViewById(R.id.main_todoInput)
        submit = findViewById(R.id.main_todoSubmit)

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        val view = this
        Binder(CreateDestroyBinderLifecycle(lifecycle)).apply {
            bind(view to feature using UiEventToWish)
            bind(feature to view using StateToViewModel)
        }

        submit.setOnClickListener {
            if (input.text.isNotEmpty()) {
                events.onNext(
                    TodoEvent.Created(input.text.toString())
                )
                input.text.clear()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        capsule.saveState(outState)
    }

    override fun accept(model: TodoViewModel) {
        adapter.items = model.todos
    }
}
