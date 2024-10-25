package com.example.lemmingcli
import org.json.JSONObject
data class Furigana(val word:String, val yomi:String, val start:Int, val end:Int)
data class Dictform(val word:String, val dictform: String, val start:Int, val end:Int)

class Card(
    val word:String,
    val sentence:String,
    val furiganas:ArrayList<Furigana>,
    val dictforms:ArrayList<Dictform>
) {
    //val word: String = w
    //val sentence: String = sent

}