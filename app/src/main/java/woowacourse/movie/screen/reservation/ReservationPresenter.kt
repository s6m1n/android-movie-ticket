package woowacourse.movie.screen.reservation

import woowacourse.movie.model.Movie
import woowacourse.movie.model.Quantity
import woowacourse.movie.model.Reservation
import woowacourse.movie.model.screening.Schedule
import woowacourse.movie.model.screening.Screening

class ReservationPresenter(private val view: ReservationContract.View) :
    ReservationContract.Presenter {
    private val quantity = Quantity()

    override fun onStart() {
        val movie = view.readMovieData() ?: return
        view.initializeMovieDetails(movie)
        view.setupReservationCompletedButton(movie)
        view.setupTicketQuantityControls(quantity)
    }

    override fun onReservationCompleted(movie: Movie) {
        val screening = Screening(movie, Schedule("2024.3.1"), quantity)
        val reservation = Reservation(screening)
        view.moveToCompletedActivity(reservation)
    }

    override fun plus() {
        quantity.increase()
        view.setQuantityText("${quantity.value}")
    }

    override fun minus() {
        quantity.decrease()
        view.setQuantityText("${quantity.value}")
    }
}