package com.mypong.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.awt.Rectangle

class MyPong : ApplicationAdapter() {
    lateinit var player : Rectangle
    lateinit var opponent : Rectangle
    lateinit var ball : Ball

    class Rectangle{
        var drawRectangles : ShapeRenderer
        private var rectangleX : Float = 0.0f
        private var rectangleY : Float = 0.0f
        private var rectangleWidth : Float = 0.0f
        private var rectangleHeight : Float = 0.0f

        constructor(){
            drawRectangles = ShapeRenderer()
        }

        fun setPosition(x : Float, y : Float){
            rectangleX = x
            rectangleY = y
        }

        fun setSize(width : Float, height : Float){
            rectangleWidth = width
            rectangleHeight = height
        }

        fun getPosition() : MutableList<Float>{
            var list = mutableListOf<Float>()
            list.add(rectangleX)
            list.add(rectangleY)
            return list
        }

        fun getSize() : MutableList<Float>{
            var list = mutableListOf<Float>()
            list.add(rectangleWidth)
            list.add(rectangleHeight)
            return list
        }

        fun moveUp(){
            if(rectangleY < getDeviceDimensions()[1].toFloat() - rectangleHeight)
            {
                rectangleY+=4.0f
            }
        }

        fun moveDown(){
            if(rectangleY > 0)
            {
                rectangleY-=4.0f
            }
        }

        fun getDeviceDimensions() : MutableList<Int>{
            val deviceWidth = Gdx.graphics.width
            val deviceHeight = Gdx.graphics.height
            var list = mutableListOf<Int>()
            list.add(deviceWidth)
            list.add(deviceHeight)
            return list
        }
    }

    class Ball{
        var drawBall : ShapeRenderer
        private var ballX : Float = 0.0f
        private var ballY : Float = 0.0f
        private var radius : Float = 0.0f
        private var directionX : Float = 5.0f
        private var directionY : Float = -5.0f

        constructor(){
            drawBall = ShapeRenderer()
        }

        fun setSize(rad : Float){
            radius = rad
        }

        fun setPosition(x : Float, y : Float){
            ballX = x
            ballY = y
        }

        fun getSize() : Float{
            return radius
        }

        fun getPosition() : MutableList<Float>{
            val list = mutableListOf<Float>()
            list.add(ballX)
            list.add(ballY)
            return list
        }

        fun getDeviceDimensions() : MutableList<Int>{
            val deviceWidth = Gdx.graphics.width
            val deviceHeight = Gdx.graphics.height
            var list = mutableListOf<Int>()
            list.add(deviceWidth)
            list.add(deviceHeight)
            return list
        }

        fun changeDirectionX(){
            directionX*=-1
        }

        fun changeDirectionY(){
            directionY*=-1
        }

        fun move(player : Rectangle, opponent : Rectangle){
            ballX += directionX
            ballY += directionY
            if(ballY < 0 + getSize()){
                changeDirectionY()
            }
            else if(ballX > getDeviceDimensions()[0] - getSize()){
                changeDirectionX()
            }
            else if(ballY > getDeviceDimensions()[1] - getSize()){
                changeDirectionY()
            }
            else if(ballX < 0 + getSize()){
                changeDirectionX()
            }

            if(ballY < player.getPosition()[1] + player.getSize()[1] && ballY > player.getPosition()[1] && ballX < player.getPosition()[0] + player.getSize()[0] + getSize()){
                changeDirectionX()
            }

            if(ballY < opponent.getPosition()[1] + opponent.getSize()[1] && ballY > opponent.getPosition()[1] && ballX > opponent.getPosition()[0] - getSize()){
                changeDirectionX()
            }
        }
    }

    override fun create() {
        val distanceFromEdgeX = 50f
        //Init player
        player = Rectangle()
        player.setSize(player.getDeviceDimensions()[1].toFloat()/50, player.getDeviceDimensions()[0].toFloat()/10)
        player.setPosition(distanceFromEdgeX, 0f)

        //Init AI
        opponent = Rectangle()
        opponent.setSize(player.getDeviceDimensions()[1].toFloat()/50, player.getDeviceDimensions()[0].toFloat()/10)
        opponent.setPosition(opponent.getDeviceDimensions()[0].toFloat() - (player.getSize()[0] + distanceFromEdgeX), 0f)

        //Init Ball
        ball = Ball()
        ball.setSize(player.getDeviceDimensions()[1].toFloat()/75)
        ball.setPosition(ball.getDeviceDimensions()[0].toFloat()/2, ball.getDeviceDimensions()[1].toFloat()/3)
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        player.drawRectangles.begin(ShapeRenderer.ShapeType.Filled)
        player.drawRectangles.rect(player.getPosition()[0], player.getPosition()[1], player.getSize()[0], player.getSize()[1])
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.moveUp()
        }

        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player.moveDown()
        }

        if(Gdx.input.isTouched(0)){
            if(Gdx.input.getY(0) < player.getDeviceDimensions()[1]/2){
                player.moveUp()
            }
            else{
                player.moveDown()
            }
        }
        player.drawRectangles.end()

        opponent.drawRectangles.begin(ShapeRenderer.ShapeType.Filled)
        if(ball.getPosition()[1] < opponent.getPosition()[1] + (opponent.getSize()[1]/2)){
            opponent.moveDown()
        }
        else{
            opponent.moveUp()
        }
        opponent.drawRectangles.rect(opponent.getPosition()[0], opponent.getPosition()[1], opponent.getSize()[0], opponent.getSize()[1])
        opponent.drawRectangles.end()

        ball.drawBall.begin(ShapeRenderer.ShapeType.Filled)
        ball.drawBall.circle(ball.getPosition()[0], ball.getPosition()[1], ball.getSize(), 50)
        ball.move(player, opponent)
        ball.drawBall.end()
    }

    override fun dispose() {
        player.drawRectangles.dispose()
        opponent.drawRectangles.dispose()
        ball.drawBall.dispose()
    }
}
