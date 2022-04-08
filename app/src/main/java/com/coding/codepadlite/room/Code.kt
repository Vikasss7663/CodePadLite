package com.coding.codepadlite.room

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CodeLang(val value: String) {
    C("C"),
    CPP("C++"),
    JAVA("Java"),
    PYTHON("Python"),
    OTHER("Other")
}

fun getAllCodeLang(): List<CodeLang>{
    return listOf(CodeLang.C, CodeLang.CPP, CodeLang.JAVA, CodeLang.PYTHON, CodeLang.OTHER)
}

fun getCodeLang(value: String): CodeLang {
    val map = CodeLang.values().associateBy(CodeLang::value)
    return map[value] ?: CodeLang.OTHER
}

@Entity(tableName = "code")
data class Code(
    @PrimaryKey(autoGenerate = true)
    val codeId: Int = 0,
    val codeTitle: String,
    val codeDesc: String,
    val codeContent: String,
    val codeLang: CodeLang,
    var codeFav: Boolean = false
)

fun getDummyCodeItem(): Code {
    return Code(
        0,
        "",
        "",
        "",
        CodeLang.OTHER,
        false
    )
}