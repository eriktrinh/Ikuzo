package com.eriktrinh.ikuzo.ui.page

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eriktrinh.ikuzo.R
import com.eriktrinh.ikuzo.data.ani.Review
import com.eriktrinh.ikuzo.utils.ext.loadAndCropInto
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_reviews_view.view.*

class ReviewAdapter(val context: Context, var reviews: List<Review>) : RecyclerView.Adapter<ReviewHolder>() {
    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.list_item_reviews_view, parent, false)

        return ReviewHolder(context, view)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bindItem(reviews[position])
    }

    fun addItems(items: List<Review>) {
        val oldSize = reviews.size
        reviews = reviews.plus(items)
        notifyItemRangeInserted(oldSize, items.size)
    }

    fun clearItems() {
        val oldSize = reviews.size
        reviews = emptyList()
        notifyItemRangeRemoved(0, oldSize)
    }
}

class ReviewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.reviewer_image
    private val nameText = itemView.reviewer_name
    private val titleText = itemView.review_title
    private val reviewText = itemView.review_text
    private val scoreText = itemView.review_score
    private val expandButton = itemView.review_expand_button
    private var expanded = false

    fun bindItem(review: Review) {
        val score = review.score / 10 + (if (review.score % 10 >= 5) 1 else 0)
        val expandable = review.text.length > 200
        val shortText = if (expandable) review.text.substring(0..200) + "..." else review.text

        Picasso.with(context)
                .loadAndCropInto(review.user.imageUrlSmall, imageView)
        nameText.text = review.user.displayName
        titleText.text = review.summary
        scoreText.text = score.toString()
        reviewText.text = if (expandable) shortText else review.text
        expandButton.visibility = if (expandable) View.VISIBLE else View.INVISIBLE
        expandButton.isEnabled = expandable
        expandButton.setOnClickListener {
            expanded = !expanded
            expandButton.setText(if (expanded) R.string.collapse else R.string.expand)
            reviewText.text = if (!expanded) shortText else review.text
        }
    }
}
