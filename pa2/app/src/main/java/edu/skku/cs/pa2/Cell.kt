package edu.skku.cs.pa2

// Cell class
class Cell(

    //wall
    val topWall : Boolean = false,
    val bottomWall : Boolean = false,
    val leftWall : Boolean = false,
    val rightWall : Boolean = false,

    var playerExistance : Boolean = false,
    var hintExistance : Boolean = false,
    var goalExistance : Boolean = false,
) {
}