package woowacourse.movie.feature.seat

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import woowacourse.movie.R
import woowacourse.movie.feature.completed.ReservationCompletedActivity
import woowacourse.movie.feature.main.ui.ScreeningModel
import woowacourse.movie.feature.reservation.ui.SeatBoardView
import woowacourse.movie.feature.reservation.ui.SeatModel
import woowacourse.movie.feature.reservation.ui.TicketModel
import java.text.DecimalFormat

class SeatSelectionActivity : AppCompatActivity(), SeatSelectionContract.View {
    private val movieId by lazy { intent.getLongExtra(SCREENING_ID, -1L) }
    private val datePosition by lazy { intent.getIntExtra(DATE_POSITION, -1) }
    private val timePosition by lazy { intent.getIntExtra(TIME_POSITION, -1) }
    private val quantity by lazy { intent.getIntExtra(QUANTITY, -1) }
    private val selectedSeatList: MutableList<String> = emptyList<String>().toMutableList()
    private val presenter by lazy {
        SeatSelectionPresenter(
            this,
            movieId,
            datePosition,
            timePosition,
        )
    }
    private val movieTitleTv by lazy { findViewById<TextView>(R.id.tv_screen_movie_title) }
    private val priceTv by lazy { findViewById<TextView>(R.id.tv_seat_price) }
    private val seatBoardView: SeatBoardView by lazy { createSeatTextViews().let(::SeatBoardView) }
    private val reservationConfirmTv by lazy { findViewById<TextView>(R.id.btn_reservation_confirm) }
    private var price: Long = 0

    private fun createSeatTextViews(): List<TextView> =
        findViewById<TableLayout>(R.id.table_seat)
            .children
            .filterIsInstance<TableRow>()
            .flatMap { it.children }
            .filterIsInstance<TextView>()
            .toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_selection)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter.fetchData()
    }

    private fun updatePriceTextView() {
        val formattedPrice = DecimalFormat(TicketModel.DECIMAL_FORMAT).format(price)
        priceTv.text = getString(R.string.seat_price, formattedPrice)
    }

    override fun initialize(
        movie: ScreeningModel,
        seatModels: List<SeatModel>,
    ) {
        updatePriceTextView()
        movieTitleTv.text = movie.title
        seatBoardView.initText(seatModels)
        seatBoardView.setupClickListener { index, seatView ->
            setUpClickedSeatViewControl(seatView, seatModels, index)
        }
        reservationConfirmTv.setOnClickListener {
            if (selectedSeatList.size == quantity) {
                showReservationConfirmDialog()
            } else {
                Toast.makeText(this, "${quantity}개의 좌석을 선택해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setUpClickedSeatViewControl(
        seatView: TextView,
        seatModels: List<SeatModel>,
        index: Int,
    ) {
        if (seatView.isSelected) {
            selectedSeatList.remove(seatView.text.toString())
            price -= seatModels[index].price
            seatView.isSelected = !seatView.isSelected
            updatePriceTextView()
        } else {
            if (selectedSeatList.size < quantity) {
                selectedSeatList.add(seatView.text.toString())
                price += seatModels[index].price
                seatView.isSelected = !seatView.isSelected
                updatePriceTextView()
            } else {
                Toast.makeText(this, "${quantity}개의 좌석을 선택해 주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showReservationConfirmDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("예매 확인")
            .setMessage("정말 예매하시겠습니까?")
            .setNegativeButton(
                "취소",
                DialogInterface.OnClickListener { _, _ ->
                },
            )
            .setPositiveButton(
                "예매 완료",
                DialogInterface.OnClickListener { _, _ ->
                    presenter.saveTicket(selectedSeatList.toList(), price)
                },
            )
        builder.show()
    }

    override fun navigateToReservationCompleted(reservationId: Long) {
        startActivity(ReservationCompletedActivity.getIntent(this, reservationId))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList("selected_seats", ArrayList(selectedSeatList))
        outState.putLong("price", price)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getStringArrayList("selected_seats")?.forEach {
            selectedSeatList.add(it)
        }
        price = savedInstanceState.getLong("price")
        updatePriceTextView()
        seatBoardView.update(selectedSeatList.toList())
    }

    companion object {
        const val SCREENING_ID = "screening_id"
        const val TIME_POSITION = "time_position"
        const val DATE_POSITION = "date_position"
        const val QUANTITY = "quantity"

        fun getIntent(
            context: Context,
            movieId: Long,
            datePosition: Int,
            timePosition: Int,
            quantity: Int,
        ): Intent {
            return Intent(context, SeatSelectionActivity::class.java).apply {
                putExtra(SCREENING_ID, movieId)
                putExtra(DATE_POSITION, datePosition)
                putExtra(TIME_POSITION, timePosition)
                putExtra(QUANTITY, quantity)
            }
        }
    }
}
