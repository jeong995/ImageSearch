package com.example.imagesearch

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject

class SecondFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageResultAdapter: ImageResultAdapter
    private val imageResults: MutableList<ImageResult> by lazy { mutableListOf() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // adapter 연결
        recyclerView = view.findViewById(R.id.imageSearchRecyclerView)
        imageResultAdapter =
            ImageResultAdapter(imageResults, object : ImageResultAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    imageResults.removeAt(position)
                    imageResultAdapter.notifyItemRemoved(position)
                    Toast.makeText(context, "아이템을 삭제합니다", Toast.LENGTH_SHORT).show()
                    saveDataToSharedPreferences()
                }
            })
        recyclerView.adapter = imageResultAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        loadDataFromSharedPreferences()
    }
    private fun saveDataToSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("SelectedImageInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonArray = JSONArray()
        imageResults.forEach { imageResult ->
            val jsonObject = JSONObject().apply {
                put("imageURL", imageResult.thumbnailUrl)
                put("siteName", imageResult.displaySiteName)
                put("dateTime", imageResult.datetime)
                put("checkItem", imageResult.check)
            }
            jsonArray.put(jsonObject)
        }
        editor.putString("items", jsonArray.toString()).apply()
    }

    private fun loadDataFromSharedPreferences() {
        val sharedPreferences = requireContext().getSharedPreferences("SelectedImageInfo", Context.MODE_PRIVATE)
        val itemsString = sharedPreferences.getString("items", "[]")
        val jsonArray = JSONArray(itemsString)
        imageResults.clear()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val imageResult = ImageResult(
                thumbnailUrl = jsonObject.getString("imageURL"),
                displaySiteName = jsonObject.getString("siteName"),
                datetime = jsonObject.getString("dateTime"),
                check = jsonObject.getBoolean("checkItem")
            )
            imageResults.add(imageResult)
        }
        imageResultAdapter.notifyDataSetChanged()
    }

}

