package ipca.example.newsmob

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView

class ArticleDetailActivity : AppCompatActivity() {

    var articleTitle : String? = null
    var articleUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)
        articleTitle = intent.getStringExtra(ARTICLE_TITLE)
        articleUrl = intent.getStringExtra(ARTICLE_URL)

        title = articleTitle

        val webView = findViewById<WebView>(R.id.webViewArticle)
        articleUrl?.let {
            webView.loadUrl(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT,articleTitle)
                intent.putExtra(Intent.EXTRA_TEXT,articleUrl)
                startActivity(Intent.createChooser(intent, "News Mob"))
                true
            }
            R.id.action_browser ->{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(articleUrl)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        const val ARTICLE_TITLE = "article_title"
        const val ARTICLE_URL = "article_url"
    }

}