package com.coding.codepadlite.application

import android.app.Application
import com.coding.codepadlite.room.CodeDatabase

class MyApplication : Application(){
    // initialize only when required
    val database: CodeDatabase by lazy { CodeDatabase.getDatabase(this) }
}