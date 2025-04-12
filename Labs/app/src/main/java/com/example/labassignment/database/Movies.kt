package com.example.labassignment.database

import com.example.labassignment.model.Movie

public class Movies {
    fun getMovies(): List<Movie> {
        return listOf<Movie> (
            Movie(
                1,
                "A Minecraft Movie",
                "/yFHHfHcUgGAxziP1C3lLt0q2T4s.jpg",
                "/2Nti3gYAX513wvhp8IiLL6ZDyOm.jpg",
                "2025-03-31",
                "By day, they're invisible—valets, hostesses, and bartenders at a luxury hotel. By night, they're the Carjackers, a crew of skilled drivers who track and rob wealthy clients on the road. As they plan their ultimate heist, the hotel director hires a ruthless hitman, to stop them at all costs. With danger closing in, can Nora, Zoe, Steve, and Prestance pull off their biggest score yet?"
            ),
            Movie(
                2,
                "In the Lost Lands",
                "/iHf6bXPghWB6gT8kFkL1zo00x6X.jpg",
                "/op3qmNhvwEvyT7UFyPbIfQmKriB.jpg",
                "2025-02-27",
                "A queen sends the powerful and feared sorceress Gray Alys to the ghostly wilderness of the Lost Lands in search of a magical power, where the sorceress and her guide, the drifter Boyce must outwit and outfight man and demon."
            ),

            Movie(
                3,
                "G20",
                "/tSee9gbGLfqwvjoWoCQgRZ4Sfky.jpg",
                "/k32XKMjmXMGeydykD32jfER3BVI.jpg",
                "2025-04-09",
                "After the G20 Summit is overtaken by terrorists, President Danielle Sutton must bring all her statecraft and military experience to defend her family and her fellow leaders."
            ),
            Movie(
                4,
                "Novocaine",
                "/xmMHGz9dVRaMY6rRAlEX4W0Wdhm.jpg",
                "/bYVygxg5enGs89V5qgejbWl0Oq6.jpg",
                "2025-03-12",
                "When the girl of his dreams is kidnapped, everyman Nate turns his inability to feel pain into an unexpected strength in his fight to get her back."
            ),
            Movie(
                5,
                "देवा",
                "/4wKpglgZYMYpISMdThgdqS1TSQc.jpg",
                "/lqHt4icP1GTaNBeVTxTrwTZdoAW.jpg",
                "2025-01-31",
                "Dev Ambre, a ruthless cop, loses his memory in an accident just after he has finished solving a murder case and now has to reinvestigate it while keeping his memory loss a secret from everyone except DCP Farhan Khan."
            )
        )
    }
}