package edu.skku.cs.pa1

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class WordAdapter(val data: ArrayList<Word>, val answer: String, val context: Context): BaseAdapter(){
    var input2:String ?= null;

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
        val generatedView = inflater.inflate(R.layout.item_word, null)

        //define the letter
        val letter1 = generatedView.findViewById<TextView>(R.id.textView)
        val letter2 = generatedView.findViewById<TextView>(R.id.textView2)
        val letter3 = generatedView.findViewById<TextView>(R.id.textView3)
        val letter4 = generatedView.findViewById<TextView>(R.id.textView4)
        val letter5 = generatedView.findViewById<TextView>(R.id.textView5)

        //letter array
        val letters= arrayListOf(letter1,letter2,letter3,letter4,letter5);

        val chars = charArrayOf(
            data[position].letter1,
            data[position].letter2,
            data[position].letter3,
            data[position].letter4,
            data[position].letter5,
        )

        val textOfPosition = String(chars)

        //connect to the main
        letter1.text = data[position].letter1.toString()
        letter2.text = data[position].letter2.toString()
        letter3.text = data[position].letter3.toString()
        letter4.text = data[position].letter4.toString()
        letter5.text = data[position].letter5.toString()

        for (i in 0..4) {
            if(answer[i] == textOfPosition[i]){
                letters[i].setBackgroundColor(Color.parseColor("#99F691"))
                letters[i].setTextColor(Color.parseColor("#000000"))
            }
            else if(answer.contains(textOfPosition[i])){
                letters[i].setBackgroundColor(Color.parseColor("#FFE46F"))
                letters[i].setTextColor(Color.parseColor("#000000"))
            }
            else{
                letters[i].setBackgroundColor(Color.parseColor("#787C7E"))
                letters[i].setTextColor(Color.parseColor("#FFFFFF"))
            }
        }


        return generatedView
    }
}