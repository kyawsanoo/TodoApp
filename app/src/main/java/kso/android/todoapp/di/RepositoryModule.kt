package kso.android.todoapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kso.android.todoapp.repository.CreateNewTaskBaseRepository
import kso.android.todoapp.repository.CreateNewTaskRepository
import kso.android.todoapp.repository.TodoListBaseRepository
import kso.android.todoapp.repository.TodoListRepository
import kso.android.todoapp.repository.UpdateTodoBaseRepository
import kso.android.todoapp.repository.UpdateTodoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun todoListRepository(repo: TodoListRepository) : TodoListBaseRepository

    @Binds
    @Singleton
    abstract fun createNewTaskRepository(repo: CreateNewTaskRepository) : CreateNewTaskBaseRepository

    @Binds
    @Singleton
    abstract fun updateTodoRepository(repo: UpdateTodoRepository) : UpdateTodoBaseRepository
}