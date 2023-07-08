package edu.skku.cs.pa2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.*

class MazeActivity : AppCompatActivity() {

    private var size : Int = 0
    private var turn : Int = 0 //user's turn
    private var hint : Boolean = false //not used
    private var previousDirection = "Down" // initialized down
    //user position
    private var playerRow = 0
    private var playerCol = 0

    private lateinit var mazeGrid: Array<Array<Cell>>// Declare mazeGrid as a class property
    // adapter
    private lateinit var adapter: GridViewAdapter
    // for adapter
    private lateinit var dataList : Array<Cell>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        // get from intent
        val mazeName= intent.getStringExtra("name")

        // GridView
        val gridView = findViewById<GridView>(R.id.GridView)

        // buttons
        val topBtn = findViewById<Button>(R.id.Button_top)
        val downBtn = findViewById<Button>(R.id.Button_down)
        val leftBtn = findViewById<Button>(R.id.Button_left)
        val rightBtn = findViewById<Button>(R.id.Button_right)
        val hintBtn = findViewById<Button>(R.id.Button_hint)
        val turnText = findViewById<TextView>(R.id.TextView_turn)
        turnText.text = "Turn : $turn" //turn

        val client = OkHttpClient()
        val gson = Gson()
        val url = "http://swui.skku.edu:1399/maze/map?name="

        val req= Request.Builder().url(url + mazeName).build()

        // send request
        client.newCall(req).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {

                //fail
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // data from the server
                val str = response.body!!.string()
                val data = gson.fromJson(str, MazeDetailModel::class.java)

                // mazeRows Array<String> 1 row = 1 element in data
                val mazeRows = data.maze.split("\n")

                // maze size
                size = mazeRows[0].trim().toInt()

                // maze grid size
                mazeGrid = Array(size) { Array(size) { Cell() } }


                for (i in 1..size) { // for each size
                    val cells = mazeRows[i].trim().split(" ").toTypedArray() //each cell
                    for (j in 0 until size) {

                        val cellValue = cells[j].toInt()
                        // Add Wall value<Boolean> to each Cell
                        mazeGrid[i - 1][j] = Cell(
                            (cellValue and 8) == 8,
                            (cellValue and 2) == 2,
                            (cellValue and 4) == 4,
                            (cellValue and 1) == 1
                        )
                    }
                }

                // player position appears on the map
                mazeGrid[0][0].playerExistance = true
                // goal position appears on the map
                mazeGrid[size-1][size-1].goalExistance = true

                // Get Cell layout
                var colWidth = 350 / size
                CoroutineScope(Dispatchers.Main).launch {
                    // Set numColumns and columnWidth of GridView
                    gridView.numColumns = size
                    gridView.columnWidth = colWidth

                    // Cell width and height from GridView
                    dataList = mazeGrid.flatten().toTypedArray()

                    adapter = GridViewAdapter(this@MazeActivity,dataList, size,"down",gridView.width,gridView.height)
                    gridView.adapter = adapter
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                throw Exception(e.toString())
            }
        })

        // button event listener
        leftBtn.setOnClickListener {
            if (!mazeGrid[playerCol][playerRow].leftWall)
            {
                mazeGrid[playerCol][playerRow].playerExistance = false
                // change user position
                --playerRow
                mazeGrid[playerCol][playerRow].playerExistance = true
                dataList = mazeGrid.flatten().toTypedArray()
                ++turn
            }
            val adapter = GridViewAdapter(this,dataList,size,"left",gridView.width, gridView.height)
            gridView.adapter = adapter
            turnText.text = "Turn : ${turn.toString()}"


            // change previous direction
            previousDirection = "left"

            // check there are hint
            if(mazeGrid[playerCol][playerRow].playerExistance && mazeGrid[playerCol][playerRow].hintExistance)
            {
                // remove hint when Player enter
                mazeGrid[playerCol][playerRow].hintExistance = false
            }

            // check game is done

            if(mazeGrid[size-1][size-1] == mazeGrid[playerCol][playerRow])
            {
                CoroutineScope(Dispatchers.Main).run{
                    Toast.makeText(this@MazeActivity,"Finish",Toast.LENGTH_SHORT).show()
                }
            }

        }

        rightBtn.setOnClickListener {
            if (!mazeGrid[playerCol][playerRow].rightWall) {
                mazeGrid[playerCol][playerRow].playerExistance = false
                // change user position
                ++playerRow
                mazeGrid[playerCol][playerRow].playerExistance = true
                dataList = mazeGrid.flatten().toTypedArray()
                ++turn
            }
            val adapter = GridViewAdapter(this,dataList,size,"right",gridView.width, gridView.height)
            gridView.adapter = adapter
            turnText.text = "Turn : ${turn.toString()}"

            // change previous direction
            previousDirection = "right"

            // check there are hint
            if(mazeGrid[playerCol][playerRow].playerExistance && mazeGrid[playerCol][playerRow].hintExistance)
            {
                // remove hint when Player enter
                mazeGrid[playerCol][playerRow].hintExistance = false
            }

            // check is finished
            if(mazeGrid[size-1][size-1] == mazeGrid[playerCol][playerRow])
            {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@MazeActivity,"Finish",Toast.LENGTH_SHORT).show()
                }
            }
        }

        topBtn.setOnClickListener {
            if (!mazeGrid[playerCol][playerRow].topWall)
            {
                mazeGrid[playerCol][playerRow].playerExistance = false
                // change user position
                --playerCol
                mazeGrid[playerCol][playerRow].playerExistance = true
                dataList = mazeGrid.flatten().toTypedArray()
                ++turn
            }

            val adapter = GridViewAdapter(this,dataList,size,"up",gridView.width, gridView.height)
            gridView.adapter = adapter
            turnText.text = "Turn : ${turn}"

            // change previous direction
            previousDirection = "up"

            // check there are hint
            if(mazeGrid[playerCol][playerRow].playerExistance && mazeGrid[playerCol][playerRow].hintExistance)
            {
                // remove hint when Player enter
                mazeGrid[playerCol][playerRow].hintExistance = false
            }

            if(mazeGrid[size-1][size-1] == mazeGrid[playerCol][playerRow])
            {
                CoroutineScope(Dispatchers.Main).run{
                    Toast.makeText(this@MazeActivity,"Finish",Toast.LENGTH_SHORT).show()
                }
            }
        }

        downBtn.setOnClickListener {
            if (!mazeGrid[playerCol][playerRow].bottomWall)
            {
                mazeGrid[playerCol][playerRow].playerExistance = false
                // change user position
                ++playerCol
                mazeGrid[playerCol][playerRow].playerExistance = true
                dataList = mazeGrid.flatten().toTypedArray()
                ++turn
            }

            val adapter = GridViewAdapter(this,dataList,size,"down",gridView.width, gridView.height)
            gridView.adapter = adapter
            turnText.text = "Turn : ${turn}"

            // change previous direction
            previousDirection = "down"

            // check there are hint
            if(mazeGrid[playerCol][playerRow].playerExistance && mazeGrid[playerCol][playerRow].hintExistance)
            {
                // remove hint when Player enter
                mazeGrid[playerCol][playerRow].hintExistance = false
            }

            if(mazeGrid[size-1][size-1] == mazeGrid[playerCol][playerRow])
            {
                CoroutineScope(Dispatchers.Main).run{
                    Toast.makeText(this@MazeActivity,"Finish",Toast.LENGTH_SHORT).show()
                }
            }
        }

        hintBtn.setOnClickListener {

            fun getNext(mazeGrid: Array<Array<Cell>>, x: Int, y: Int): List<Pair<Int, Int>> {
                val next = mutableListOf<Pair<Int, Int>>()

                // Check top neighbor
                if (!mazeGrid[x][y].topWall) {
                    next.add(Pair(x - 1, y))
                }

                // Check bottom neighbor
                if (!mazeGrid[x][y].bottomWall) {
                    next.add(Pair(x + 1, y))
                }

                // Check left neighbor
                if (!mazeGrid[x][y].leftWall) {
                    next.add(Pair(x, y - 1))
                }

                // Check right neighbor
                if (!mazeGrid[x][y].rightWall) {
                    next.add(Pair(x, y + 1))
                }

                return next
            }

            // Function to check if a cell is a valid cell within the maze bounds
            fun CellValidation(numRows: Int, numCols: Int, x: Int, y: Int): Boolean {
                return x in 0 until numRows && y in 0 until numCols
            }

            // Function to reconstruct the path from start to goal using the parent map
            fun reconstructPath(parentMap: Map<Pair<Int, Int>, Pair<Int, Int>>, startX: Int, startY: Int, goalX: Int, goalY: Int): List<Pair<Int, Int>> {
                val path = mutableListOf<Pair<Int, Int>>()

                var currentCell = Pair(goalX, goalY)
                path.add(currentCell)

                while (currentCell != Pair(startX, startY)) {
                    currentCell = parentMap[currentCell] ?: break
                    path.add(currentCell)
                }

                return path.reversed()
            }

            // Hint
            fun HintFindShortestPath(mazeGrid: Array<Array<Cell>>, startX: Int, startY: Int, goalX: Int, goalY: Int): List<Pair<Int, Int>>? {
                val numRows = mazeGrid.size
                val numCols = mazeGrid[0].size

                // Array to keep track of visited cells
                val visited = Array(numRows) { BooleanArray(numCols) }

                // Queue for BFS
                val queue: Queue<Pair<Int, Int>> = LinkedList()

                // Starting position
                queue.offer(Pair(startX, startY))
                visited[startX][startY] = true

                // Map to store parent cells for path reconstruction
                val parentMap = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()

                // Perform BFS
                while (queue.isNotEmpty()) {
                    val currentCell = queue.poll()
                    val x = currentCell.first
                    val y = currentCell.second

                    // Check if the goal is reached
                    if (x == goalX && y == goalY) {
                        // Goal reached, reconstruct the path
                        return reconstructPath(parentMap, startX, startY, goalX, goalY)
                    }

                    // Get the neighbors of the current cell
                    val neighbors = getNext(mazeGrid, x, y)

                    for (neighbor in neighbors) {
                        val nx = neighbor.first
                        val ny = neighbor.second

                        // Check if the neighbor is valid and not visited
                        if (CellValidation(numRows, numCols, nx, ny) && !visited[nx][ny]) {
                            // Mark the neighbor as visited and enqueue it
                            visited[nx][ny] = true
                            queue.offer(Pair(nx, ny))
                            // Store the parent cell for path reconstruction
                            parentMap[Pair(nx, ny)] = Pair(x, y)
                        }
                    }
                }

                // No path found
                return null
            }

            // Function to get the valid neighbors of a cell
            if(!hint){ //not used
                hint = true
                val shortestPath = HintFindShortestPath(mazeGrid, playerCol, playerRow, size-1,size-1)
                mazeGrid[shortestPath?.get(1)!!.first][shortestPath?.get(1)!!.second].hintExistance = true
                dataList = mazeGrid.flatten().toTypedArray()
                adapter = GridViewAdapter(this@MazeActivity,dataList, size,previousDirection,gridView.width,gridView.height)
                gridView.adapter = adapter
            } else { //used
                null
            }
        }


    }
}