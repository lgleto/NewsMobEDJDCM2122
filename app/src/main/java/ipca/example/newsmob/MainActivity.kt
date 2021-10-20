package ipca.example.newsmob

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

class MainActivity : AppCompatActivity() {

    lateinit var listviewPost : ListView

    val posts = arrayListOf("Um", "Dois", "Três")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listviewPost = findViewById(R.id.listviewNews)
        listviewPost.adapter = PostsAdapter()

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
                    Log.d("newsmob", result)
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
            textviewTitle.text = posts[position]

            return rootView
        }

    }

}