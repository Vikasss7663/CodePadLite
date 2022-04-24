package com.coding.codepadlite.utils

import android.util.Log

data class Pair(
    val start: Int,
    val end: Int
)

enum class Key{
    KEYWORD,
    COMMENT,
    SEMICOLON
}

object Parser {

    private fun getCKeyWord(): HashSet<String> {
        val keyword = hashSetOf<String>()
        keyword.add("auto")
        keyword.add("double")
        keyword.add("int")
        keyword.add("struct")
        keyword.add("break")
        keyword.add("else")
        keyword.add("long")
        keyword.add("switch")
        keyword.add("case")
        keyword.add("enum")
        keyword.add("register")
        keyword.add("typedef")
        keyword.add("char")
        keyword.add("extern")
        keyword.add("return")
        keyword.add("union")
        keyword.add("continue")
        keyword.add("for")
        keyword.add("signed")
        keyword.add("void")
        keyword.add("do")
        keyword.add("if")
        keyword.add("static")
        keyword.add("while")
        keyword.add("default")
        keyword.add("goto")
        keyword.add("sizeof")
        keyword.add("volatile")
        keyword.add("const")
        keyword.add("float")
        keyword.add("short")
        keyword.add("unsigned")

        return keyword
    }

    private fun getJavaKeyWord(): HashSet<String> {
        val keyword = hashSetOf<String>()
        keyword.add("abstract")
        keyword.add("assert")
        keyword.add("boolean")
        keyword.add("break")
        keyword.add("byte")
        keyword.add("case")
        keyword.add("catch")
        keyword.add("char")
        keyword.add("class")
        keyword.add("continue")
        keyword.add("default")
        keyword.add("do")
        keyword.add("double")
        keyword.add("else")
        keyword.add("enum")
        keyword.add("extends")
        keyword.add("final")
        keyword.add("finally")
        keyword.add("float")
        keyword.add("for")
        keyword.add("if")
        keyword.add("implements")
        keyword.add("import")
        keyword.add("instanceof")
        keyword.add("int")
        keyword.add("interface")
        keyword.add("long")
        keyword.add("native")
        keyword.add("new")
        keyword.add("null")
        keyword.add("package")
        keyword.add("private")
        keyword.add("protected")
        keyword.add("public")
        keyword.add("return")
        keyword.add("short")
        keyword.add("strictfp")
        keyword.add("super")
        keyword.add("switch")
        keyword.add("synchronized")
        keyword.add("this")
        keyword.add("throw")
        keyword.add("throws")
        keyword.add("transient")
        keyword.add("try")
        keyword.add("void")
        keyword.add("volatile")
        keyword.add("while")
        keyword.add("const")
        keyword.add("goto")

        return keyword
    }

    private fun getPythonKeyWord(): HashSet<String> {
        val keyword = hashSetOf<String>()
        keyword.add("False")
        keyword.add("await")
        keyword.add("else")
        keyword.add("elif")
        keyword.add("import")
        keyword.add("pass")
        keyword.add("None")
        keyword.add("break")
        keyword.add("except")
        keyword.add("in")
        keyword.add("raise")
        keyword.add("True")
        keyword.add("class")
        keyword.add("finally")
        keyword.add("is")
        keyword.add("return")
        keyword.add("and")
        keyword.add("continue")
        keyword.add("for")
        keyword.add("lambda")
        keyword.add("try")
        keyword.add("as")
        keyword.add("def")
        keyword.add("from")
        keyword.add("nonlocal")
        keyword.add("while")
        keyword.add("assert")
        keyword.add("del")
        keyword.add("global")
        keyword.add("not")
        keyword.add("with")
        keyword.add("async")
        keyword.add("elif")
        keyword.add("if")
        keyword.add("or")
        keyword.add("yield")

        return keyword
    }

    fun getCSpan(code: String): HashMap<Key, ArrayList<Pair>> {

        val map = hashMapOf<Key, ArrayList<Pair>>()
        map[Key.KEYWORD] = arrayListOf()
        map[Key.COMMENT] = arrayListOf()
        map[Key.SEMICOLON] = arrayListOf()

        val keyword = getCKeyWord()
        var i=0
        val curr = StringBuilder()
        while (i < code.length) {
            val x = code[i]
            if (x == ' ' || x == '(') {
                val temp = curr.toString()
                Log.d("LOGGING", temp)
                if(keyword.contains(temp)) {
                    map[Key.KEYWORD]?.add(Pair(i-curr.length,i))
                }
                curr.clear()
                i += 1
            } else if (x == '"') {
                val prev = i++
                while(i < code.length && code[i] != '"') i++
                if(i+1 < code.length) i += 1
                map[Key.SEMICOLON]?.add(Pair(prev,i))
                curr.clear()
            } else if (x == '/') {
                // check for single line or multiline comment
                // based on next character
                if(i+1 >= code.length) break
                val prev = i
                i += 1
                if(code[i] == '/') {
                    // Single Line Comment
                    // wait for new line
                    while(i < code.length && code[i] != '\n') i++
                    if(i+1 < code.length) i += 1
                    map[Key.COMMENT]?.add(Pair(prev,i))
                } else if(code[i] == '*') {
                    // Multi Line Comment
                    if(i+1 < code.length) i += 1
                    while(i < code.length) {
                        if(code[i-1] == '*' && code[i] == '/') break;
                        i++
                    }
                    if(i+1 < code.length) i += 1
                    map[Key.COMMENT]?.add(Pair(prev,i+1))
                }
                curr.clear()
            } else if(code[i] in 'a'..'z') {
                curr.append(x)
                i += 1
            } else {
                curr.clear()
                i += 1
            }
        }

        return map

    }

    fun getJavaSpan(code: String): HashMap<Key, ArrayList<Pair>> {

        val map = hashMapOf<Key, ArrayList<Pair>>()
        map[Key.KEYWORD] = arrayListOf()
        map[Key.COMMENT] = arrayListOf()
        map[Key.SEMICOLON] = arrayListOf()

        val keyword = getJavaKeyWord()
        var i=0
        val curr = StringBuilder()
        while (i < code.length) {
            val x = code[i]
            if (x == ' ' || x == '(' || x == ',' || x == '.' || x == ')') {
                val temp = curr.toString()
                Log.d("LOGGING", temp)
                if(keyword.contains(temp)) {
                    map[Key.KEYWORD]?.add(Pair(i-curr.length,i))
                }
                curr.clear()
                i += 1
            } else if (x == '"') {
                val prev = i++
                while(i < code.length && code[i] != '"') i++
                if(i+1 < code.length) i += 1
                map[Key.SEMICOLON]?.add(Pair(prev,i))
                curr.clear()
            } else if (x == '/') {
                // check for single line or multiline comment
                // based on next character
                if(i+1 >= code.length) break
                val prev = i
                i += 1
                if(code[i] == '/') {
                    // Single Line Comment
                    // wait for new line
                    while(i < code.length && code[i] != '\n') i++
                    if(i+1 < code.length) i += 1
                    map[Key.COMMENT]?.add(Pair(prev,i))
                } else if(code[i] == '*') {
                    // Multi Line Comment
                    if(i+1 < code.length) i += 1
                    while(i < code.length) {
                        if(code[i-1] == '*' && code[i] == '/') break;
                        i++
                    }
                    if(i+1 < code.length) i += 1
                    map[Key.COMMENT]?.add(Pair(prev,i+1))
                }
                curr.clear()
            } else if(code[i] in 'a'..'z') {
                curr.append(x)
                i += 1
            } else {
                curr.clear()
                i += 1
            }
        }

        return map

    }

    fun getPythonSpan(code: String): HashMap<Key, ArrayList<Pair>> {

        val map = hashMapOf<Key, ArrayList<Pair>>()
        map[Key.KEYWORD] = arrayListOf()
        map[Key.COMMENT] = arrayListOf()
        map[Key.SEMICOLON] = arrayListOf()

        val keyword = getPythonKeyWord()
        var i=0
        val curr = StringBuilder()
        while (i < code.length) {
            val x = code[i]
            if (x == ' ' || x == '(' || x == ':') {
                val temp = curr.toString()
                Log.d("LOGGING", temp)
                if(keyword.contains(temp)) {
                    map[Key.KEYWORD]?.add(Pair(i-curr.length,i))
                }
                curr.clear()
                i += 1
            } else if (x == '"') {
                val prev = i++
                while(i < code.length && code[i] != '"') i++
                if(i+1 < code.length) i += 1
                map[Key.SEMICOLON]?.add(Pair(prev,i))
                curr.clear()
            } else if (x == '#') {
                // check for single line or multiline comment
                // based on next character
                if(i+1 >= code.length) break
                val prev = i
                i += 1
                while(i < code.length && code[i] != '\n') i++
                if(i+1 < code.length) i += 1
                map[Key.COMMENT]?.add(Pair(prev,i))
                curr.clear()
            } else if(code[i] in 'a'..'z') {
                curr.append(x)
                i += 1
            } else {
                curr.clear()
                i += 1
            }
        }

        return map

    }

    fun getNoSpan(): HashMap<Key, ArrayList<Pair>> {

        val map = hashMapOf<Key, ArrayList<Pair>>()
        map[Key.KEYWORD] = arrayListOf()
        map[Key.COMMENT] = arrayListOf()
        map[Key.SEMICOLON] = arrayListOf()

        return map

    }


}