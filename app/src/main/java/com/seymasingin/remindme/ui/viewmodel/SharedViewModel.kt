package com.seymasingin.remindme.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seymasingin.remindme.data.models.Priority
import com.seymasingin.remindme.data.models.ToDoTask
import com.seymasingin.remindme.data.repos.DataStoreRepository
import com.seymasingin.remindme.data.repos.TodoRepository
import com.seymasingin.remindme.util.Action
import com.seymasingin.remindme.util.Constants.MAX_TITLE_LENGTH
import com.seymasingin.remindme.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repo: TodoRepository,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    var action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableState<Int> = mutableIntStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    val date: MutableState<String> = mutableStateOf("")
    val time: MutableState<String> = mutableStateOf("")

    val searchTextState: MutableState<String> =  mutableStateOf("")

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks : StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchTasks : StateFlow<RequestState<List<ToDoTask>>> = _searchTasks

    val lowPriorityTasks: StateFlow<List<ToDoTask>> =
        repo.sortByLowPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    val highPriorityTasks: StateFlow<List<ToDoTask>> =
        repo.sortByHighPriority.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState : StateFlow<RequestState<Priority>> = _sortState

    fun readSortState(){
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map {
                        Priority.valueOf(it)
                    }
                    .collect{
                        _sortState.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(priority)
        }
    }

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repo.getAllTasks.collect{
                    _allTasks.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun getSearchTasks(query: String) {
        _searchTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repo.searchDatabase("%$query%").collect{
                    _searchTasks.value = RequestState.Success(it)
                }
            }
        }catch (e: Exception){
            _searchTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int){
        viewModelScope.launch {
            repo.getSelectedTask(taskId).collect{task ->
                _selectedTask.value = task
            }
        }
    }

    private fun addTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value,
                date = date.value,
                time = time.value
            )
            repo.addTask(toDoTask)
        }
    }

    private fun updateTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value,
                date = date.value,
                time = time.value
            )
            repo.updateTask(toDoTask)
        }
    }

    private fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value,
                date = date.value,
                time = time.value
            )
            repo.deleteTask(toDoTask)
            repo.getAllTasks
        }
    }

    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO)  {
            repo.deleteAllTasks()
            repo.getAllTasks
        }
    }

    fun handleDatabaseActions(action: Action){
        when(action) {
            Action.ADD ->{
                addTask()
            }
            Action.UPDATE ->{
                updateTask()
            }
            Action.DELETE ->{
                deleteTask()
            }
            Action.DELETE_ALL ->{
                deleteAll()
            }

            else -> {

            }
        }
        this.action.value = Action.NO_ACTION
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if( selectedTask != null){
            id.value = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
            date.value = selectedTask.date
            time.value = selectedTask.time
        }
        else {
            id.value =0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
            date.value = ""
            time.value = ""
        }
    }

    fun updateTitle(newTitle: String){
        if(newTitle.length < MAX_TITLE_LENGTH){
            title.value = newTitle
        }
    }

    fun updateDate(newDate: String) {
        date.value = newDate
    }

    fun updateTime(newTime: String) {
        time.value = newTime
    }

    fun validateFields(): Boolean {
        return title.value.isNotEmpty() &&
                description.value.isNotEmpty() &&
                date.value.isNotEmpty() &&
                time.value.isNotEmpty()
    }

    fun updateAction(newAction: Action) {
        action.value = newAction
    }
}