package com.coding.codepadlite.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.coding.codepadlite.room.Code
import com.coding.codepadlite.room.CodeDao
import com.coding.codepadlite.room.CodeLang
import com.coding.codepadlite.room.getDummyCodeItem
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CodeViewModel(private val codeDao: CodeDao): ViewModel() {

    private val _codes = mutableStateOf(emptyList<Code>())
    val codes: State<List<Code>> = _codes
    var topBarVisible: MutableState<Boolean> = mutableStateOf(true)
    var favCodes: MutableState<Boolean> = mutableStateOf(false)

    var selectedItem = getDummyCodeItem()

    init {
        fetchAllCodes()
    }

    private fun fetchAllCodes() {
        viewModelScope.launch {
            codeDao.fetchAll().collect {
                _codes.value = it
            }
        }
    }


    fun addNewItem(it: Code) {
        val newCode = Code(
            codeId = it.codeId,
            codeTitle = it.codeTitle,
            codeDesc = it.codeDesc,
            codeContent = it.codeContent,
            codeLang = it.codeLang,
            codeFav = it.codeFav
        )
        insertCode(newCode)
    }

    fun deleteItem(code: Code) {
        deleteCode(code)
    }

    private fun insertCode(code: Code) {
        viewModelScope.launch {
            codeDao.insert(code)
        }
    }

    private fun deleteCode(code: Code) {
        viewModelScope.launch {
            codeDao.delete(code)
        }
    }
}


class CodeViewModelFactory(private val codeDao: CodeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CodeViewModel(codeDao = codeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}