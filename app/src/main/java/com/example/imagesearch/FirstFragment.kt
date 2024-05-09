package com.example.imagesearch

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearch.databinding.FragmentFirstBinding
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class FirstFragment : Fragment() {

    lateinit var fragmentBinding: FragmentFirstBinding
    private lateinit var adapter: ImageResultAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = FragmentFirstBinding.inflate(inflater, container, false)
        return fragmentBinding.root

    }

    // SharedPreferences에서 마지막 검색어 저장하기
    private fun saveLastEditText(content: String) {
        val sharedPreferences = activity?.getSharedPreferences("EditText", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.apply {
            putString("lastEditText", content)
            apply()
        }
    }
    // 마지막 검색어 불러오기
    private fun getLastEditText(): String? {
        val sharedPreferences = activity?.getSharedPreferences("EditText", Context.MODE_PRIVATE)
        return sharedPreferences?.getString("lastEditText", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // recyclerview 설정
        adapter = ImageResultAdapter(mutableListOf(), object: ImageResultAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // 뭘 적어야지?
                Toast.makeText(context, "아이템을 저장하였습니다", Toast.LENGTH_SHORT).show()
            }
        })
        fragmentBinding.imageSearchRecyclerView.layoutManager = GridLayoutManager(context, 2)
        fragmentBinding.imageSearchRecyclerView.adapter = adapter

        // 검색어 설정 후 recyclerview 업데이트
        fragmentBinding.search.setOnClickListener {
            val query = fragmentBinding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchImages(query)
                saveLastEditText(query) // EditText 내용 저장
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
            } else {
                Toast.makeText(context, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 저장된 마지막 검색어 불러와서 사용하는 부분
        val lastEditText = getLastEditText()
        fragmentBinding.searchEditText.setText(lastEditText)

    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://dapi.kakao.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun searchImages(query: String) {
        val apiService = retrofit.create(ApiService::class.java)
        apiService.searchImages(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    val results = response.body()?.documents ?: emptyList()
                    adapter.updateData(results)

                } else {
                    // 에러 처리
                    Toast.makeText(context, "에러 발생가 발생했습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                // onFailure 구현
                Toast.makeText(context, "인터넷에 검색이 안됩니다", Toast.LENGTH_SHORT).show()
            }
        })
    }

}