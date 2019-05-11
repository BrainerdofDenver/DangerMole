package com.example.dangermolemobile

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

// This video was immensely helpful https://www.youtube.com/watch?v=fwwu2mDD4cw
class GalleryArrayAdapter(var context: Context, var items: ArrayList<GalleryItem>): BaseAdapter(){

    internal class ViewHolder(row: View?){
        var txtData: TextView
        var ivMolePreview: ImageView

        init {
            this.txtData = row?.findViewById(R.id.data) as TextView
            this.ivMolePreview = row?.findViewById(R.id.molePreview) as ImageView
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        var viewHolder: ViewHolder
        if (convertView == null) {
            var layout = LayoutInflater.from(context)
            view = layout.inflate(R.layout.my_list_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var item: GalleryItem = getItem(position) as GalleryItem
        viewHolder.txtData.text = item.getmMoleData()
        viewHolder.ivMolePreview.setImageBitmap(BitmapFactory.decodeFile(item.getmImageFile()))

        return view as View
    }

    override fun getItem(position: Int): Any {
        return items.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.count()
    }

}
