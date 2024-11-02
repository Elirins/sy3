package com.example.sy3

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var buttonShowDialog: Button
    private lateinit var buttonShowMenu: Button
    private lateinit var buttonShowActionMode: Button
    private lateinit var testText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        listView = findViewById(R.id.listView)
        buttonShowDialog = findViewById(R.id.button_show_dialog)
        buttonShowMenu = findViewById(R.id.button_show_menu)
        buttonShowActionMode = findViewById(R.id.button_show_actionmode)
        testText = findViewById(R.id.test_text)

        val animals = arrayOf("Lion", "Tiger", "Monkey", "Dog", "Cat", "Elephant")
        val images = arrayOf(
            R.drawable.lion, R.drawable.tiger, R.drawable.monkey,
            R.drawable.dog, R.drawable.cat, R.drawable.elephant
        )

        val dataList = ArrayList<Map<String, Any>>()
        for (i in animals.indices) {
            val map = HashMap<String, Any>()
            map["name"] = animals[i]
            map["image"] = images[i]
            dataList.add(map)
        }

        val adapter = SimpleAdapter(
            this,
            dataList,
            R.layout.list_item,
            arrayOf("name", "image"),
            intArrayOf(R.id.item_text, R.id.item_image)
        )
        listView.adapter = adapter


        // 设置列表项点击事件
        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                val itemName = animals[position]
                Toast.makeText(this@MainActivity, itemName, Toast.LENGTH_SHORT).show()
            }

        buttonShowDialog.setOnClickListener {
            showCustomDialog()
        }

        buttonShowMenu.setOnClickListener {
            showMenu(it)
        }

        // 设置按钮点击事件，打开新的页面
        buttonShowActionMode.setOnClickListener {
            startActivity(Intent(this, MultiSelectActivity::class.java))
        }
    }

    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = dialogBuilder.create()
        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.mymenu_layout, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.font_size_small -> {
                    testText.textSize = 10f
                    true
                }
                R.id.font_size_medium -> {
                    testText.textSize = 16f
                    true
                }
                R.id.font_size_large -> {
                    testText.textSize = 20f
                    true
                }
                R.id.action_normal_item -> {
                    Toast.makeText(this, "Toast提示", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.color_red -> {
                    testText.setTextColor(Color.RED)
                    true
                }
                R.id.color_black -> {
                    testText.setTextColor(Color.BLACK)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

}
