package tech.caspers.ufovolume

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hmomeni.verticalslider.VerticalSlider
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketFrame
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // because i can
        volumeSlider.cornerRadius = resources.displayMetrics.density * 30

        Thread(Runnable {
            // create websocket client, hardcoded since im lazy lol
            val ws = WebSocketFactory().createSocket("ws://192.168.0.241:9002", 5000)

            // reg listeners
            ws.addListener(object : WebSocketAdapter() {
                override fun onTextMessage(websocket: WebSocket?, text: String?) {
                    super.onTextMessage(websocket, text)
                    // eventually going to send the uh volume from the server but hey i cant be arsed, pr if you want to idc
                }

                override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) {
                    super.onCloseFrame(websocket, frame)
                    // pointless but cba to remove
                }
            })
            // connect, lol?
            ws.connect()

            // watch for volume slider changes (duh...)
            volumeSlider.onProgressChangeListener = object : VerticalSlider.OnSliderProgressChangeListener {
                override fun onChanged(progress: Int, max: Int) {
                    progressText.text = "%.0f%%".format(Locale.ENGLISH, progress.toFloat() / max.toFloat() * 100) // set the progress text and do some math things before it does
                    ws.sendText(progress.toString()) // send volume to server
                }
            }
        }).start() // start new thread because i cant run the websocket client on the mainthread and i cba to run on ui thread

    }
}