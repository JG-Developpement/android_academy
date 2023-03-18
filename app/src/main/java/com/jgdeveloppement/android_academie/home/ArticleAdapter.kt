package com.jgdeveloppement.android_academie.home

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.jgdeveloppement.android_academie.R
import com.jgdeveloppement.android_academie.models.Article
import com.jgdeveloppement.android_academie.utils.Categories
import com.jgdeveloppement.android_academie.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(private var articleList: MutableList<Article>,
                     private val onClickItem: OnClickItem,
                     private val isFromBookmark: Boolean?)
    : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    interface OnClickItem{
        fun onClickArticleCard(articleId: Int, categories: Categories)
        fun onClickBookmarkBtn(articleId: Int, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleList[position]


        val parser =  SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE)
        val formatter = SimpleDateFormat("dd/MM/yy", Locale.FRANCE)
        val formattedDate = parser.parse(article.date)?.let { formatter.format(it) }

        holder.categories.text = Utils.myApp.getString(R.string.date_category_item_list, Categories.getValues(article.categories), formattedDate)


        if (isFromBookmark == true)
            holder.bookmarkBtn.setImageResource(R.drawable.trash)
        else
            holder.bookmarkBtn.setImageResource(R.drawable.bookmark_new)

        holder.title.text = article.title
        holder.articleImage.setImageDrawable(Categories.categoriesImage(article.categories))
        holder.title.ellipsize = TextUtils.TruncateAt.END

        holder.articleCard.setOnClickListener { onClickItem.onClickArticleCard(article.id, article.categories) }
        holder.bookmarkBtn.setOnClickListener { onClickItem.onClickBookmarkBtn(article.id, position) }
    }

    fun notifyArticleRemoved(position: Int){
        this.articleList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = articleList.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val categories: TextView = view.findViewById(R.id.categories_title)
        val title: TextView = view.findViewById(R.id.article_title)
        val bookmarkBtn: ImageButton = view.findViewById(R.id.bookmark_btn)
        val articleImage: ImageView = view.findViewById(R.id.article_image)
        val articleCard: CardView = view.findViewById(R.id.article_card_item)
    }
}