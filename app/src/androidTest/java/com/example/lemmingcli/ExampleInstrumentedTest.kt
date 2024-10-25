package com.example.lemmingcli

import android.util.Log
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun testDBsimple() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.databaseBuilder(appContext, AppDatabase::class.java, "dict.db")
            .createFromAsset("jadict/jadict.db")
            .build()
        val dictDao = db.dictDao()
        val words = arrayListOf<String>("きっかけ", "手放す","おかず")
        // search up the database
        val results = ArrayList<DictLookupResult>()
        for (word in words) {
            val searchResults: List<DictWordWithMeaning> = dictDao.searchByWord(word)
            for(pair in searchResults){
                val senseStrs = pair.meaning.split("||")
                val senses = ArrayList<Sense>()
                for(senseStr in senseStrs){
                    val s = senseStr.split("$$")
                    assertNotEquals(s.size, 0)
                    if(s.size == 1) {
                        senses.add(Sense("", s[0]))
                    }else if (s.size > 1){
                        senses.add(Sense(s[0], s[1]))
                    }
                }
                results.add(DictLookupResult(word, senses))
            }
        }
        assertEquals(results.size, words.size)

        // look at what we have
        for (result in results){
            Log.d("TEST", result.word)
            for(sense in result.senses){
                Log.d("TEST", sense.toString())
            }
        }
    }
}