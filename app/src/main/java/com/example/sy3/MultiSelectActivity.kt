package com.example.sy3

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode

class MultiSelectActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var actionMode: ActionMode? = null
    private val selectedItems = mutableListOf<Int>() // 存储选中的项

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_select)

        listView = findViewById(R.id.listView)
        actionMode = startSupportActionMode(actionModeCallback)
        actionMode?.title = "0 selected"

        val items = arrayOf("One", "Two", "Three", "Four", "Five")
        val imageResId = android.R.drawable.sym_def_app_icon

        val dataList = ArrayList<Map<String, Any>>()
        for (i in items.indices) {
            val map = HashMap<String, Any>()
            map["name"] = items[i]
            map["image"] = imageResId
            dataList.add(map)
        }

        val adapter = SimpleAdapter(
            this,
            dataList,
            R.layout.list_item_with_icon,
            arrayOf("name", "image"),
            intArrayOf(R.id.item_text, R.id.item_image)
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, view, position, _ ->
            if (actionMode == null) {
                actionMode = startSupportActionMode(actionModeCallback)
            }
            toggleSelection(position, view)
        }
    }

    private fun toggleSelection(position: Int, view: View) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
            view.setBackgroundColor(Color.TRANSPARENT)
        } else {
            selectedItems.add(position)
            view.setBackgroundColor(Color.GRAY)
        }
        actionMode?.title = "${selectedItems.size} selected"
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.action_mode_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.delete -> {
                    deleteSelectedItems()
                    // 不结束 ActionMode，而是更新标题
                    mode.title = "${selectedItems.size} selected"
                    true
                }
                R.id.close_action_mode -> {
                    finishToMainLayout()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
            selectedItems.clear()
            (listView.adapter as SimpleAdapter).notifyDataSetChanged()
            for (i in 0 until listView.childCount) {
                listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    private fun deleteSelectedItems() {
        if (selectedItems.isNotEmpty()) {
            Toast.makeText(this, "Deleted ${selectedItems.size} items", Toast.LENGTH_SHORT).show()
            selectedItems.clear()
            // 更新列表视图
            (listView.adapter as SimpleAdapter).notifyDataSetChanged()
        } else {
            Toast.makeText(this, "No items selected to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun finishToMainLayout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
