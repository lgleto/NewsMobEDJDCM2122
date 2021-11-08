package ipca.example.newsmob

import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URL

object Backend {

    const val BASE_API = "https://newsapi.org/v2/"
    const val API_KEY = "&apiKey=1765f87e4ebc40229e80fd0f75b6416c"

    fun fetchLastArticles(urlPath:String, setOnArticlesChanged: (List<Article>)->Unit ) {
        val posts = arrayListOf<Article>()
        GlobalScope.launch (Dispatchers.IO){
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(BASE_API + urlPath + API_KEY)
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful){
                    // apresentar mensagem de erro
                }else {
                    val result = response.body!!.string()
                    //Log.d("newsmob", result)
                    val jsonObject = JSONObject(result)
                    if (jsonObject.has("status")){
                        val statusString = jsonObject.getString("status")
                        if (statusString == "ok"){
                            val jsonArray = jsonObject.getJSONArray("articles")
                            for (index in 0 until jsonArray.length()){
                                val jsonArticle : JSONObject = jsonArray.get(index) as JSONObject
                                val article = Article.fromJson(jsonArticle)
                                posts.add(article)
                            }
                            GlobalScope.launch (Dispatchers.Main){
                                setOnArticlesChanged.invoke(posts)
                            }
                        }
                    }
                }
            }
        }
    }

    fun setImageToImageView(urlToImage:String, imageView: ImageView) {
        GlobalScope.launch (Dispatchers.IO){
            try {
                val inputStream = URL(urlToImage).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                GlobalScope.launch (Dispatchers.Main) {
                    imageView.setImageBitmap(bitmap)
                }
            }catch (e:Exception){
                GlobalScope.launch (Dispatchers.Main) {
                    imageView.setImageResource(R.drawable.ic_launcher_background)
                }
            }
        }
    }

}