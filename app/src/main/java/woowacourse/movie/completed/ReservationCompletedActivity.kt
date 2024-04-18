package woowacourse.movie.completed

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import woowacourse.movie.R
import woowacourse.movie.model.Ticket
import java.text.DecimalFormat

class ReservationCompletedActivity : AppCompatActivity(), ReservationCompletedContract.View {
    private val presenter = ReservationCompletedPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_completed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.onViewCreated()
    }

    override fun readTicketData() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("ticket", Ticket::class.java)
        } else {
            intent.getSerializableExtra("ticket") as? Ticket
        }

    override fun initializeTicketDetails(ticket: Ticket) {
        findViewById<TextView>(R.id.movie_title).text = ticket.getTitle()
        findViewById<TextView>(R.id.opening_day).text = ticket.getOpeningDay()
        findViewById<TextView>(R.id.quantity).text = "일반 ${ticket.quantity}명"
        findViewById<TextView>(R.id.price).text =
            "${DecimalFormat("#,###").format(ticket.price * ticket.quantity)}원 (현장 결제)"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
