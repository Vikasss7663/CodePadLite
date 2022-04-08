package com.coding.codepadlite.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeDao {

    @Query("SELECT * FROM code")
    fun fetchAll(): Flow<List<Code>>

    @Query("SELECT * FROM code WHERE codeId = :codeId")
    fun fetchById(codeId: Int): Flow<Code>

    @Query("SELECT * FROM code WHERE codeId IN (:codeIds)")
    fun fetchAllByIds(codeIds: IntArray): Flow<List<Code>>

    @Query("SELECT * FROM code WHERE codeTitle LIKE :codeTitle LIMIT 1")
    fun findByTitle(codeTitle: String): Flow<Code>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(code: Code)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(codes: List<Code>)

    @Delete
    suspend fun delete(code: Code)

    @Query("DELETE FROM code")
    suspend fun deleteAll()

    /*@Transaction
    fun updateAll(codes: List<Code>) {
        deleteAll()
        insertAll(codes)
    }*/

}