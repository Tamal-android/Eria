package com.eria.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eria.R
import com.eria.data.model.response.TopPicks
import com.eria.ui.base.HomeBaseActivity
import com.eria.ui.fragment.DashboardFragment
import com.eria.ui.fragment.MenuListFragment

class TopPicksAdapter(
    private val _context: HomeBaseActivity,
    _topPicksdata: List<TopPicks?>?
) :
    RecyclerView.Adapter<TopPicksAdapter.ViewHolder>() {


    private var mContext: HomeBaseActivity = _context
    private var _topPicksdata: List<TopPicks>? = _topPicksdata as List<TopPicks>?


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem =
            layoutInflater.inflate(R.layout.top_brands_restaurant, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(_topPicksdata!![position])
        holder.itemView.setOnClickListener(View.OnClickListener {
         // _context.showCustomDialog("Order","order")

            mContext.changeFragment(MenuListFragment.newInstance(),isAddToBackStack = true)


        })


    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var iv_gallery = itemView.findViewById(R.id.banner_image) as AppCompatImageView

        fun bindItems(galleryDataItem: TopPicks) {
            var imgUrl = galleryDataItem.imagePath
            Glide.with(itemView.context)
                .asBitmap()
                .load(imgUrl)
                .placeholder(R.drawable.intersection)
                .error(R.drawable.intersection)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(iv_gallery)


        }

    }


}