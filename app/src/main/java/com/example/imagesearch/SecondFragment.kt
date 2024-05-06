package com.example.imagesearch

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray

class SecondFragment : Fragment() {

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
        imageResultAdapter = ImageResultAdapter(imageResults)
        recyclerView.adapter = imageResultAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        loadData() // 데이터 불러오기
    }

    private fun loadData() {
        val sharedPreferences = requireActivity().getSharedPreferences("SelectedImageInfo", Context.MODE_PRIVATE)
        val imagesJson = sharedPreferences.getString("items", "[]")
        val jsonArray = JSONArray(imagesJson)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val imageURL = jsonObject.getString("imageURL")
            val siteName = jsonObject.getString("siteName")
            val dateTime = jsonObject.getString("dateTime")
            val imageResult = ImageResult(imageURL, siteName, dateTime)
            imageResults.add(imageResult)
        }
        imageResultAdapter.notifyDataSetChanged() // 어댑터에 데이터 변경을 알림
    }
}

