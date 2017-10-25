package com.example.s157006.rescuerobot

import android.databinding.DataBindingUtil
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import com.example.MsgGen
import com.example.TcpClient
import com.example.s157006.rescuerobot.Networking.ConnectionStatus
import com.example.s157006.rescuerobot.Networking.IConnectionHandler
import com.example.s157006.rescuerobot.Networking.TcpAsync
import com.example.s157006.rescuerobot.ViewModel.ArduinoViewModel
import com.example.s157006.rescuerobot.activity.ConnectActivity
import com.example.s157006.rescuerobot.databinding.ActivityMainBinding
import io.github.controlwear.virtual.joystick.android.JoystickView
import java.util.*

/**
 * Created by joaquin on 7-10-2017.
 *
 */

class MainActivity : AppCompatActivity(), IConnectionHandler {

    private lateinit var binding: ActivityMainBinding
    private var tcpAsync: AsyncTask<Void, Void, Void>? = null
    private var viewModel = ArduinoViewModel(this)

    private val out = ArrayList<String>()

    //#region Views
    private val camera by bind<ImageButton>(R.id.camera)
    private val led by bind<ImageButton>(R.id.light)

    private val joystick by bind<JoystickView>(R.id.joystick)

    private val left_up by bind<ImageButton>(R.id.left_up)
    private val left_stop by bind<ImageButton>(R.id.left_stop)
    private val left_down by bind<ImageButton>(R.id.left_down)

    private val right_up by bind<ImageButton>(R.id.right_up)
    private val right_stop by bind<ImageButton>(R.id.right_stop)
    private val right_down by bind<ImageButton>(R.id.right_down)

    private val slope_up by bind<ImageButton>(R.id.slope_up)
    private val slope_stop by bind<ImageButton>(R.id.slope_stop)
    private val slope_down by bind<ImageButton>(R.id.slope_down)

    //#endregion

    var gimbal_mode:Boolean = true
    var led_on = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // init Tcp client
        val bundle = intent.extras
        if (bundle != null) {
            val ip   = bundle.getString(ConnectActivity.KEY_IP)
            val port = bundle.getInt(ConnectActivity.KEY_PORT)

            val client = TcpClient(ip, port)
            tcpAsync = TcpAsync(client, this)
            tcpAsync!!.execute()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.arduino = viewModel

        // use views
        camera.setOnClickListener{
            gimbal_mode = !gimbal_mode

            if (gimbal_mode){
                camera.setImageDrawable(resources.getDrawable(R.drawable.camera))
            } else {
                camera.setImageDrawable(resources.getDrawable(R.drawable.car))
            }
        }

        joystick.setOnMoveListener { angle, strength ->
            if (gimbal_mode) {
                val v = (strength/100.0)*Math.sin(angle*2*Math.PI/360)
                val h = (strength/100.0)*Math.cos(angle*2*Math.PI/360)
                addToSendQueue(MsgGen.wheels(v, h))
            } else {
                val msg:String = if (strength == 0) {
                    MsgGen.gimbal(MsgGen.G_STOP)
                } else if (angle >= 315 || angle <= 45) {
                    MsgGen.gimbal(MsgGen.G_RIGHT)
                } else if (angle in 46..134) {
                    MsgGen.gimbal(MsgGen.G_UP)
                } else if (angle in 135..225) {
                    MsgGen.gimbal(MsgGen.G_LEFT)
                } else {
                    MsgGen.gimbal(MsgGen.G_DOWN)
                }
                addToSendQueue(msg)
            }
        }
        led.setOnClickListener{view ->

            //TODO fix
            led_on = !led_on
            val color:Int =
            if (led_on) resources.getColor(R.color.orange_900) else resources.getColor(R.color.colorPrimaryDark)

            addToSendQueue(MsgGen.led(led_on))
            view.setBackgroundColor(color)
        }

        right_up.setOnClickListener { addToSendQueue(MsgGen.door(MsgGen.D_RIGHT, MsgGen.D_OUT)) }
        right_stop.setOnClickListener{addToSendQueue(MsgGen.door(MsgGen.D_RIGHT, MsgGen.D_STOP))}
        right_down.setOnClickListener{addToSendQueue(MsgGen.door(MsgGen.D_RIGHT, MsgGen.D_IN))}

        left_up.setOnClickListener { addToSendQueue(MsgGen.door(MsgGen.D_LEFT, MsgGen.D_OUT)) }
        left_stop.setOnClickListener{addToSendQueue(MsgGen.door(MsgGen.D_LEFT, MsgGen.D_STOP))}
        left_down.setOnClickListener{addToSendQueue(MsgGen.door(MsgGen.D_LEFT, MsgGen.D_IN))}

        slope_up.setOnClickListener { addToSendQueue(MsgGen.slope(MsgGen.S_UP)) }
        slope_stop.setOnClickListener{addToSendQueue(MsgGen.slope(MsgGen.S_STOP))}
        slope_down.setOnClickListener{addToSendQueue(MsgGen.slope(MsgGen.S_DOWN))}
    }


    override fun onDestroy() {
        super.onDestroy()

        //making sure to cancel the asyncTask when the activity is destroyed
        if (tcpAsync != null) {
            tcpAsync!!.cancel(true)
        }
    }


    override fun addToSendQueue(msg: String) {
        if (tcpAsync != null) {
            out.add(msg)
        }
    }

    override fun onMessage(msg: String) {

    }

    override fun getNextMessage(): String? {
        return if (out.size > 0)
            out.removeAt(0) else null
    }

    override fun onConnectionStatusChange(status: ConnectionStatus) {
        runOnUiThread { connectionChanged(status) }
    }

    /**
     * Running on GUI thread
     */
    private fun connectionChanged(status: ConnectionStatus) {
        viewModel.status.set(status)
    }
}
