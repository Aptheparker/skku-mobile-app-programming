package edu.skku.cs.pa1

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class LetterAdapter(
    val data: ArrayList<Char>,
    val textColor: Int,
    val backgroundColor: Int,
    val context: Context
) : BaseAdapter() {
    var input2: String? = null;

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //inflator
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val generatedView = inflater.inflate(R.layout.item_letter, null)

        //define the letter
        val letter = generatedView.findViewById<TextView>(R.id.textView7)

        letter.text = data[position].toString();
        println("Text: ${data[position]}")
        letter.setBackgroundColor(backgroundColor)
        letter.setTextColor(textColor)

        return generatedView
    }

}