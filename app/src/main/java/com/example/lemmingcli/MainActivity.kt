package com.example.lemmingcli

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import androidx.core.text.toSpanned
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.lemmingcli.databinding.ActivityMainBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.example.lemmingcli.Fragments.JishoFragment
import com.example.lemmingcli.Fragments.SentenceFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
//    private lateinit var btnGenerate: Button
//    private lateinit var btnPrev: Button
//    private lateinit var btnNext: Button
    private lateinit var wordText: TextView
    private lateinit var sentText: TextView

    private var wordList = arrayListOf<String>("手放す", "締める", "閉じる", "当てる")
    private var cardList = arrayListOf<Card>()
    private var cardidx = -1
    private var wordidx = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root);

        viewPager = findViewById(R.id.view_pager)

        // add fragment to the list
        val viewPagerAdapter = SentenceViewAdapter(this)
        viewPager.adapter = viewPagerAdapter

        // setup tab layout
        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.addOnTabSelectedListener(object: OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab){
                viewPager.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: Tab?) {
                Log.d("MAIN", "unselected")
            }

            override fun onTabReselected(tab: Tab?) {
                viewPager.setCurrentItem(tab!!.position)
            }
        })

        viewPager.registerOnPageChangeCallback(object: OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.getTabAt(position)?.select()
            }
        })

        //should go into sentence frag
//        wordText = findViewById(R.id.word_text)
//        sentText = findViewById(R.id.sent_text)
//
//        Log.d("MAIN", cardList.size.toString())
//        Log.d("MAIN", wordList.size.toString())
//
//        // first word!
//        getSentence(wordList[0])
//        binding.next.setOnClickListener(
//            View.OnClickListener {
//                if (cardidx >= cardList.size - 1 && wordidx + 1 < wordList.size) {
//                    // create more on reaching end of already created cards
//                    getSentence(wordList[++wordidx])
//                }
//                else if(cardidx + 1 < cardList.size){
//                    showCard(cardList[++cardidx])
//                }
//            }
//        )
    }

    private fun showCard(card: Card){
        wordText.text = card.word
        val spantext = SpannableString(card.sentence)
        val furicolor = ContextCompat.getColor(this, android.R.color.holo_red_dark)
        for (idx in 0 until card.furiganas.size){
            val f: Furigana = card.furiganas[idx]
            val replacementSpan = FuriganaReplacementSpan(f.yomi, furicolor)
            spantext.setSpan(replacementSpan, f.start, f.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        sentText.text = spantext
    }


    private fun getSentence(word: String){
        val generationEp = "http://86.127.246.87:22513/generate_sentences"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val reqObj = JSONObject()
        reqObj.put("word", word)
        val request =
            JsonObjectRequest(
                Request.Method.POST, generationEp,
                reqObj,
                {
                    response ->
                    val sentences = response.getJSONArray("sentences")
                    val furiganas: JSONArray = response.getJSONArray("furiganas")
                    val dictforms: JSONArray = response.getJSONArray("dictforms")
                    for(i in 0 until sentences.length()){
                        Log.d("MAIN", sentences.getString(i))
                        val fl:JSONArray = furiganas.getJSONArray(i)
                        val dl:JSONArray = dictforms.getJSONArray(i)
                        val furiganaList = ArrayList<Furigana>()
                        val dictformList = ArrayList<Dictform>()

                        for(fidx in 0 until fl.length()) {
                            val f:JSONArray = fl.getJSONArray(fidx)
                            val w: String = f.getString(0)
                            furiganaList.add(Furigana(word=w, yomi=f.getString(1), start=f.getInt(2), end=f.getInt(2)+w.length))
                        }

                        for(didx in 0 until dl.length()){
                            val d:JSONArray = dl.getJSONArray(didx)
                            val w:String = d.getString(0)
                            dictformList.add(Dictform(word=w, dictform = d.getString(1), start=d.getInt(2), end=d.getInt(2)+w.length))
                        }
                        cardList.add(Card(word, sentences.getString(i), furiganaList, dictformList))
                    }
                    if(cardidx+1 < cardList.size){
                        showCard(cardList[++cardidx])
                    }
                },
                {
                    error -> sentText.text = error.toString()
                }
            )
        queue.add(request)
    }
//
//    override fun onClick(v: View?){
//        var result = ""
//        var word = ""
//        when(v?.id){
//            R.id.generate -> {result = "new sentence"}
//            R.id.next -> { word = "word0" }
//            R.id.prev -> { word = "word1"}
//        }
//        wordText.text = word
//        sentText.text = result
//    }
}