package togle.plinko.mega.sigma.dominicanos.utils

class Once {

    private var event = Event.NOT_WAS



    fun once(block: () -> Unit) {
        if (event == Event.NOT_WAS) {
            event = Event.WAS
            block()
        }
    }


    enum class Event {
        WAS, NOT_WAS
    }

}