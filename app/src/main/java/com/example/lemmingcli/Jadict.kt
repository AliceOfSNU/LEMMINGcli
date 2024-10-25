package com.example.lemmingcli

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "meanings")
data class DictEntry(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "meaning") val meaning: String,
)

@Entity(tableName = "vocab")
data class DictWord(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "word") val word: String,
    @PrimaryKey val pk:Int
)

data class DictWordWithMeaning(
    @ColumnInfo(name = "searchword") val word: String,
    @ColumnInfo(name = "wordmeaning") val meaning: String
)

@Dao
interface DictDao {
    @Query("SELECT * FROM meanings WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<DictEntry>

    @Query("SELECT v.word AS searchword, m.meaning AS wordmeaning " +
            "FROM meanings m " +
            "left join vocab v " +
            "on v.id = m.id " +
            "where v.word = :word")
    fun searchByWord(word: String): List<DictWordWithMeaning>

}

@Database(entities = [DictEntry::class, DictWord::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dictDao(): DictDao
}

// 'sense' as in which 'sense' a word is used.
data class Sense (val pos: String, val meaning: String)
data class DictLookupResult (val word: String, val senses: ArrayList<Sense>)