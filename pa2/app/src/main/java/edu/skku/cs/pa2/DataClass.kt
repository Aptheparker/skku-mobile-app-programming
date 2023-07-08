package edu.skku.cs.pa2

// post user information
data class UserModel(val username: String)

// get login result
data class SignInModel(val success: Boolean)

// get map list
data class MazeOverviewModel(val name:String, val size: Int)

// get map detail
data class MazeDetailModel(val maze:String)