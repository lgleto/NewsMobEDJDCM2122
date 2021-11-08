package ipca.example.newsmob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    lateinit var listviewPost : ListView

    var posts = arrayListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listviewPost = findViewById(R.id.listviewNews)
        val adapter = PostsAdapter()
        listviewPost.adapter = adapter
        Backend.fetchLastArticles("top-headlines?country=pt"){ articles ->
            posts = articles as ArrayList<Article>
            adapter.notifyDataSetChanged()
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

            article.urlToImage?.let {
                Backend.setImageToImageView(it, imageView)
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