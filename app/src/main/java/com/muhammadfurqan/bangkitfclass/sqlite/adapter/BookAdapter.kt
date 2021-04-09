package com.muhammadfurqan.bangkitfclass.sqlite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.muhammadfurqan.bangkitfclass.R
import com.muhammadfurqan.bangkitfclass.databinding.ItemBookBinding
import com.muhammadfurqan.bangkitfclass.sqlite.model.BookModel

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookHolder>(){
    var listBook = mutableListOf<BookModel>()
    lateinit var bookAdapterInterface : BookAdapterInterface


    fun setData(list : MutableList<BookModel>){
        listBook.clear()
        listBook.addAll(list)
        notifyDataSetChanged()
    }

    fun setInterface(interfacez : BookAdapterInterface){
        this.bookAdapterInterface = interfacez
    }

    inner class BookHolder(v: View) : RecyclerView.ViewHolder(v){
        val vbinding = ItemBookBinding.bind(v)
        fun bind(model : BookModel){
            vbinding.tvBookName.text=model.name
            vbinding.deleteButton.setOnClickListener {
                bookAdapterInterface.onDeleteClick(model)
            }
            vbinding.editButton.setOnClickListener {
                bookAdapterInterface.onEditClick(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book,parent,false)
        return BookHolder(view)
    }

    override fun getItemCount(): Int {
      return listBook.size
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.bind(listBook[position])
    }

    interface BookAdapterInterface{
        fun onDeleteClick(model: BookModel)
        fun onEditClick(model: BookModel)
    }
}
