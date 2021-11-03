package ipca.example.newsmob

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var listviewPost : ListView

    val posts = arrayListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listviewPost = findViewById(R.id.listviewNews)
        val adapter = PostsAdapter()
        listviewPost.adapter = adapter

        GlobalScope.launch (Dispatchers.IO){
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://newsapi.org/v2/top-headlines?country=pt&apiKey=1765f87e4ebc40229e80fd0f75b6416c")
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
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }


                }
            }
        }

    }

    inner class PostsAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return posts.size
        }

        override fun getItem(position: Int): Any {
            return posts[position]
        }

        override fun getItemId(p0: Int): Long {
            return 0L
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            val rootView = layoutInflater.inflate(R.layout.row_post, viewGroup, false)
            val textviewTitle = rootView.findViewById<TextView>(R.id.textViewTitle)
            val textViewDescription = rootView.findViewById<TextView>(R.id.textViewDescription)
            val imageView = rootView.findViewById<ImageView>(R.id.imageView)

            val article = posts[position]

            textviewTitle.text = article.title
            textViewDescription.text = article.description

            GlobalScope.launch (Dispatchers.IO){
                try {
                    val inputStream = URL(article.urlToImage).openStream()
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

            rootView.isClickable = true
            rootView.setOnClickListener {
                val intent = Intent(this@MainActivity, ArticleDetailActivity::class.java)
                intent.putExtra(ArticleDetailActivity.ARTICLE_URL, article.url)
                intent.putExtra(ArticleDetailActivity.ARTICLE_TITLE, article.title)
                startActivity(intent)
            }

            return rootView
        }

    }

}