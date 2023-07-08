package edu.skku.cs.pa2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import kotlin.math.floor

class GridViewAdapter(private var context: Context?, private var items: Array<Cell>, private var mazeSize: Int, private var direction : String, private var gridViewWidth : Int, private var gridViewHeight: Int):BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Cell {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //from to dp to px
        fun changeDpToPx(context: Context, dp:Int ) : Int {
            val density = context.resources.displayMetrics.density //in it
            return (dp * density).toInt()
        }

        // inflater
        val inflater = LayoutInflater.from(context)
        val generatedView = inflater.inflate(R.layout.maze_cell,null) //basic view

        // cell
        val item = getItem(position)

        val cellLayout = generatedView.findViewById<ConstraintLayout>(R.id.Layout_cell)
        val cellMarginImageView = generatedView.findViewById<ImageView>(R.id.ImageView_margin)
        val cellWidth = (gridViewWidth / mazeSize)
        val cellHeight = (gridViewHeight / mazeSize)

        // Set ImageView width && height
        // Set layoutParams to cell Image View(Cell)
        val cellLayoutParams = ViewGroup.LayoutParams(cellWidth,cellHeight)
        // set layout size
        cellLayout.layoutParams = cellLayoutParams


        val marginTop = if (item.topWall) changeDpToPx(context!!,3) else 0 // in pixels
        val marginBottom = if(item.bottomWall) changeDpToPx(context!!,3) else 0 // in pixels
        val marginLeft = if(item.leftWall) changeDpToPx(context!!,3) else 0 // in pixels
        val marginRight = if(item.rightWall) changeDpToPx(context!!,3) else 0 // in pixels

        val layoutParams = cellMarginImageView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        cellMarginImageView.layoutParams = layoutParams

        //goal exist & item not exist (create goal)
        if(item.goalExistance && !item.playerExistance) {

            val goalImageView = ImageView(context)
            goalImageView.setImageResource(R.drawable.goal)

            val goalLayoutParams = ConstraintLayout.LayoutParams(
                changeDpToPx(context!!, 30),
                changeDpToPx(context!!, 30)
            )

            goalLayoutParams.startToStart = cellMarginImageView.id
            goalLayoutParams.topToTop = cellMarginImageView.id
            goalLayoutParams.endToEnd = cellMarginImageView.id
            goalLayoutParams.bottomToBottom = cellMarginImageView.id
            goalImageView.layoutParams = goalLayoutParams
            cellLayout.addView(goalImageView)

            return cellLayout
            // create new imageView on Top
        }

        // draw player
        if (item.playerExistance) {

            val userCharacterImageView = ImageView(context)
            userCharacterImageView.setImageResource(R.drawable.user)

            when (direction) {
                "left" -> userCharacterImageView.rotation = -90.0f
                "right" -> userCharacterImageView.rotation = 90.0f
                "down" -> userCharacterImageView.rotation = -180.0f
                "up" -> userCharacterImageView.rotation = 0.0f
            }

            val userCharacterLayoutParams = ConstraintLayout.LayoutParams(
                changeDpToPx(context!!, 30),
                changeDpToPx(context!!, 30)
            )

            userCharacterLayoutParams.topToTop = cellMarginImageView.id
            userCharacterLayoutParams.bottomToBottom = cellMarginImageView.id
            userCharacterLayoutParams.startToStart = cellMarginImageView.id
            userCharacterLayoutParams.endToEnd = cellMarginImageView.id
            userCharacterImageView.layoutParams = userCharacterLayoutParams

            cellLayout.addView(userCharacterImageView)

            return cellLayout
        }

        //hint & player not exist
        if(item.hintExistance && !item.playerExistance){

            // call hint image
            val hintImageView = ImageView(context)
            hintImageView.setImageResource(R.drawable.hint)

            val hintLayoutParams = ConstraintLayout.LayoutParams(
                changeDpToPx(context!!, 30),
                changeDpToPx(context!!, 30)
            )

            // cell margin
            hintLayoutParams.startToStart = cellMarginImageView.id
            hintLayoutParams.topToTop = cellMarginImageView.id
            hintLayoutParams.endToEnd = cellMarginImageView.id
            hintLayoutParams.bottomToBottom = cellMarginImageView.id
            hintImageView.layoutParams = hintLayoutParams
            cellLayout.addView(hintImageView)

            return cellLayout
        }
        return generatedView
    }
}