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