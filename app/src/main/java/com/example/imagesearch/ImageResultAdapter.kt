package com.example.imagesearch

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

// recyclerview
class ImageResultAdapter(
    private val items: MutableList<ImageResult>
) : RecyclerView.Adapter<ImageResultAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.image_thumbnail)
        val siteName: TextView = view.findViewById(R.id.text_site_name)
        val dateTime: TextView = view.findViewById(R.id.text_date_time)
        var checkImage: ImageView = view.findViewById(R.id.checkImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.siteName.text = item.displaySiteName
        // Glide를 사용하여 이미지 로드 및 ImageView에 표시
        Glide.with(holder.thumbnail.context)
            .load(item.thumbnailUrl) // 이미지 URL
            .into(holder.thumbnail) // ImageView 객체

        // 시간을 yyyy-MM-dd로 보여지게 하기 위한 함수
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val targetFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = originalFormat.parse(item.datetime)
        date?.let {
            val formattedDate = targetFormat.format(it)
            holder.dateTime.text = formattedDate
        }

        holder.itemView.setOnClickListener {
            // SharedPreferences에 아이템 정보 저장하는 함수 호출
            saveItemInfoToSharedPreferences(holder.itemView.context, item)
            if (!item.check) holder.checkImage.visibility = View.VISIBLE
        }
    }

    // 최대 10개만 보임
    override fun getItemCount(): Int {
        return if (items.size > 10) 10 else items.size
    }

    // recyclerview 저장
    private fun saveItemInfoToSharedPreferences(context: Context, item: ImageResult) {
        val sharedPreferences =
            context.getSharedPreferences("SelectedImageInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val itemsString = sharedPreferences.getString("items", "[]")
        val jsonArray = JSONArray(itemsString)
        val jsonObject = JSONObject().apply {
            put("imageURL", item.thumbnailUrl)
            put("siteName", item.displaySiteName)
            put("dateTime", item.datetime)
            put("checkItem", item.check)
        }
        jsonArray.put(jsonObject)
        editor.putString("items", jsonArray.toString())
        editor.apply()

        Toast.makeText(context, "아이템 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    // update
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<ImageResult>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}