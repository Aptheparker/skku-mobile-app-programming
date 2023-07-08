package edu.skku.cs.pa2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

class MazeOverviewAdapter(private val context :Context, private val mazes: Array<MazeOverviewModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return mazes.size
    }

    // 특정 위치의 item을 리턴
    override fun getItem(position: Int): Any {
        return mazes[position]
    }
    // 특정 위치의 item id 리턴
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // View와 데이터 연결
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // inflater
        val generatedView = LayoutInflater.from(context).inflate(R.layout.maze_entry,null)

        val nameView = generatedView.findViewById<TextView>(R.id.TextView_mazeName)
        val sizeView = generatedView.findViewById<TextView>(R.id.TextView_mazeSize)

        // Name of the maze.
        nameView.text = mazes[position].name.toString()
        // Size of the maze.
        sizeView.text = mazes[position].size.toString()

        // Start button.
        val startButton = generatedView.findViewById<Button>(R.id.Button_startMaze)

        startButton.setOnClickListener {

            // Start maze
            val intent = Intent(context, MazeActivity::class.java).apply {
                putExtra("name", mazes[position].name)
                putExtra("size", mazes[position].size.toString())
            }
            context.startActivity(intent)
        }

        return generatedView
    }
}