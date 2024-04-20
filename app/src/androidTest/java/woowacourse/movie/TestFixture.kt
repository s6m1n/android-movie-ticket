package woowacourse.movie

import woowacourse.movie.model.Movie
import woowacourse.movie.model.Quantity
import woowacourse.movie.model.Reservation
import woowacourse.movie.model.pricing.PricingSystem
import woowacourse.movie.model.screening.Schedule
import woowacourse.movie.model.screening.Screening

object TestFixture {
    val DUMMY_MOVIE =
        Movie(R.drawable.poster, "제목", "설명", "2024.3.1", 120)

    fun reservationBuilder(
        quantityValue: Int,
        pricingSystem: PricingSystem,
    ): Reservation {
        val screening = screeningBuilder(quantityValue)
        return Reservation(screening, pricingSystem)
    }

    fun screeningBuilder(quantityValue: Int) =
        Screening(
            DUMMY_MOVIE,
            Schedule("2024.3.1"),
            Quantity(quantityValue),
        )
}