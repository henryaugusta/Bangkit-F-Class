package com.muhammadfurqan.bangkitfclass.sqlite

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.marginStart
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammadfurqan.bangkitfclass.R
import com.muhammadfurqan.bangkitfclass.databinding.ActivitySqliteBinding
import com.muhammadfurqan.bangkitfclass.sqlite.adapter.BookAdapter
import com.muhammadfurqan.bangkitfclass.sqlite.db.BookDatabaseManager
import com.muhammadfurqan.bangkitfclass.sqlite.model.BookModel
import kotlinx.coroutines.launch


/**
 *
 * Contact : 081375496583
 *
 * Step :
 * 1. Fork our Repository (https://github.com/fueerqan/Bangkit-F-Class)
 *
 * CHALLENGE :
 * 1. Recycler View to show all of the data, previously we only show them in toast
 * 2. Add Function to edit the books data for each item in your Recycler View Items
 * 3. Add Function to delete the books data for each item in your Recycler View Items
 * 4. Notify Data Changes for you Recycler View
 *
 * Reward : Rp20.000 Go-Pay / OVO
 * Limit : No Limit Person
 * Dateline : 23.00
 *
 * Submit to https://forms.gle/CytSQSyQDJeivpkd7
 *
 */

class SQLiteActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivitySqliteBinding
    lateinit var bookAdapter: BookAdapter

    private var m_Text = ""
    private lateinit var etBookName: AppCompatEditText
    private lateinit var btnAdd: AppCompatButton
    private lateinit var btnRead: AppCompatButton

    private val bookDb: BookDatabaseManager by lazy {
        BookDatabaseManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySqliteBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.rvBookList.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
        }

        bookAdapter = BookAdapter()
        viewBinding.rvBookList.adapter = bookAdapter
        onRead()


        bookAdapter.setInterface(object : BookAdapter.BookAdapterInterface {
            override fun onDeleteClick(model: BookModel) {
                lifecycleScope.launch {
                    Toast.makeText(
                        applicationContext,
                        "Menghapus Buku ${model.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    bookDb.deleteById(model.id.toString())
                }
                onRead()
            }

            override fun onEditClick(model: BookModel) {
                val mContext = this@SQLiteActivity
                Toast.makeText(mContext, "Test Edit", Toast.LENGTH_LONG).show()

                val builder = AlertDialog.Builder(mContext)
                builder.setTitle("Masukkan Nama Buku Yang Baru")

                val input = EditText(mContext)
                input.setText(model.name)
                builder.setView(input)

                builder.setPositiveButton(
                    "OK"
                ) { dialog, which ->

                    m_Text = input.text.toString()
                    onEdit(model.id,m_Text)
                    Toast.makeText(mContext,m_Text,Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                }
                builder.setNegativeButton(
                    "Cancel"
                ) { dialog, which -> dialog.cancel() }

                mContext.runOnUiThread(Runnable {
                    builder.show()
                })


            }

        })



        etBookName = findViewById(R.id.et_book_name)
        btnAdd = findViewById(R.id.btn_add)
        btnRead = findViewById(R.id.btn_read)

        btnAdd.setOnClickListener {
            onAdd()
        }

        btnRead.setOnClickListener {
            onRead()
        }
    }

    fun onEdit(id: Int, newTitle: String) {
        Log.d("onEdit","onEdit")
        lifecycleScope.launch {
            bookDb.update(id, newTitle)
        }
        onRead()
    }

    private fun onAdd() {
        val bookName = etBookName.text.toString()
        if (bookName.isNotEmpty()) {
            lifecycleScope.launch {
                bookDb.saveData(bookName)
            }
            etBookName.setText("")
        } else {
            Toast.makeText(this, "Please fill in the book name", Toast.LENGTH_SHORT).show()
        }
        onRead()
    }

    private fun onRead() {
        val bookList = bookDb.getData()
        bookAdapter.setData(bookList.toMutableList())
        bookAdapter.notifyDataSetChanged()
        val bookListString = bookList.joinToString(separator = "\n") {
            "Book ${it.id} is ${it.name}"
        }
    }


}