package edu.skku.cs.pa1

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    var myAdapter: WordAdapter? = null;

    var letterAdapter1: LetterAdapter? = null;
    var letterAdapter2: LetterAdapter? = null;
    var letterAdapter3: LetterAdapter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //read file
        val inputStream = applicationContext.assets.open("wordle_words.txt")
        val fileContent = inputStream.readBytes().toString(Charsets.UTF_8)
        val lineList = fileContent.lines()

        //generate answer
        val answerNum = (0..lineList.size).random()
        println("answerNum:$answerNum")

        val answer = lineList[answerNum]
        println("answer: $answer")

        //define
        val listWord = findViewById<ListView>(R.id.list_view_word)
        val listLetter1 = findViewById<ListView>(R.id.list_view_letter1)
        val listLetter2 = findViewById<ListView>(R.id.list_view_letter2)
        val listLetter3 = findViewById<ListView>(R.id.list_view_letter3)
        val btn = findViewById<Button>(R.id.button)
        val items = ArrayList<Word>()

        val list1Chars = arrayListOf<Char>()
        val list2Chars = arrayListOf<Char>()
        val list3Chars = arrayListOf<Char>()

        fun onClick(v: View) {

            //input
            val inputText = findViewById<EditText>(R.id.editText)
            val input = inputText.text

            if (input.length < 5 ) {
                return
            }

            if (fileContent.contains(input)) {
                items.add(Word(input[0], input[1], input[2], input[3], input[4]))
                for (i in 0..4) {
                    println("Checking index = $i")
                    if (input[i] == answer[i]){
                        if(!list1Chars.contains(input[i])){
                            list1Chars.add(input[i]);
                        }
                        if(list2Chars.contains(input[i])){
                            list2Chars.remove(input[i])
                        }
                        // If the char is correct
                    } else if (answer.contains(input[i])) {
                        // If the char is in the input
                        if(!list2Chars.contains(input[i]) && !list1Chars.contains(input[i])){
                            list2Chars.add(input[i])
                        }

                    } else {
                        // Wrong char
                        if(!list3Chars.contains(input[i])){
                            list3Chars.add(input[i])
                        }
                    }
                }

                println("List1 = $list1Chars")


                myAdapter?.notifyDataSetChanged()
                letterAdapter1?.notifyDataSetChanged();
                letterAdapter2?.notifyDataSetChanged();
                letterAdapter3?.notifyDataSetChanged();

                input.clear()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Word '$input' not in dictionary!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btn.setOnClickListener {
            onClick(it)
        }

        myAdapter = WordAdapter(items, answer, this)
        listWord.adapter = myAdapter

        letterAdapter1 =
            LetterAdapter(
                list3Chars,
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#787C7E"),
                this
            )
        listLetter1.adapter = letterAdapter1

        letterAdapter2 =
            LetterAdapter(
                list2Chars,
                Color.parseColor("#000000"),
                Color.parseColor("#FFE46F"),
                this
            )
        listLetter2.adapter = letterAdapter2

        letterAdapter3 =
            LetterAdapter(
                list1Chars,
                Color.parseColor("#000000"),
                Color.parseColor("#99F691"),
                this
            )
        listLetter3.adapter = letterAdapter3
    }

}



