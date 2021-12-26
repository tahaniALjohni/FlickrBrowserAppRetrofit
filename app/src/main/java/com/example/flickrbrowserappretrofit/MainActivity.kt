package com.example.flickrbrowserappretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var imageList: ArrayList<Image>
    private lateinit var rvMain: RecyclerView
    private lateinit var rvAdapter: RecyclerViewAdapter
    private lateinit var llBottom: LinearLayout
    private lateinit var etSearch: EditText
    private lateinit var btSearch: Button
    private lateinit var ivMain: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageList = arrayListOf()
        rvMain = findViewById(R.id.rvMain)
        rvAdapter = RecyclerViewAdapter(this, imageList)
        rvMain.adapter = rvAdapter
        rvMain.layoutManager = LinearLayoutManager(this)

        llBottom = findViewById(R.id.llBottom)
        etSearch = findViewById(R.id.etSearch)
        btSearch = findViewById(R.id.btSearch)
        btSearch.setOnClickListener {
            imageList.clear()
            if (etSearch.text.isNotEmpty()) {
                updateList()
            } else {
                Toast.makeText(this, "Search field is empty", Toast.LENGTH_LONG).show()
            }
        }

        ivMain = findViewById(R.id.ivMain)
        ivMain.setOnClickListener { closeImg() }
    }

    private fun updateList() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {

            apiInterface.doGetListResources(etSearch.text.toString())
                ?.enqueue(object : Callback<ImageDetails> {
                    override fun onResponse(call: Call<ImageDetails>, response: Response<ImageDetails>) {

                        Log.d("TAG", response.code().toString() + "")

                        val response = response.body()!!.photos.photo
                        for (index in response!!) {

                            val photoLink =
                                "https://farm${index.farm}.staticFlickr.com/${index.server}/${index.id}_${index.secret}.jpg"
                            imageList.add(Image(index.title, photoLink))
                        }

                        Log.d("Image List", imageList.toString())
                        rvAdapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<ImageDetails>, t: Throwable) {

                        Log.d("Error", t.message.toString())
                        call.cancel()
                    }
                })
        }
    }

    //open image glide
    fun openImg(link: String) {
        Glide.with(this).load(link).into(ivMain)
        ivMain.isVisible = true
        rvMain.isVisible = false
        llBottom.isVisible = false
    }

    // close image
    private fun closeImg() {
        ivMain.isVisible = false
        rvMain.isVisible = true
        llBottom.isVisible = true
    }
}