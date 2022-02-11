package com.example.musiqal.util

import android.content.Context
import com.example.musiqal.R
import com.example.musiqal.datamodels.RandomSearchQueryWithImage
import kotlin.random.Random

object Constants {

    val YOUTUBE_CONTENTDETAIL_PARTS = "contentDetails"
    fun getRandomMp3Api(): String {
        val randomy = true
        if (randomy) {
            return Constants.MP3_API_ALL_API_KEY.get(Random.nextInt(Constants.MP3_API_ALL_API_KEY.size - 1))
        } else {
            return Constants.MP3_API_ALL_API_KEY.get(0)
        }
    }

    fun getRandomYoutubeDataKey(context: Context,randomy:Boolean=true): String {
        if (randomy) {
            val list = context.resources.getStringArray(R.array.api_keys);
            return list.get(Random.nextInt(list.size))
        } else {
            val list = context.resources.getStringArray(R.array.api_keys);
            return list.get(0)
        }
    }

    val YOUTUBE_MP3_RapidHost = "youtube-mp36.p.rapidapi.com"

    val HOME_FRAGMENT_TAG = "homeFragmentTag"
    val COLLECTION_FRAGMENT_TAG = "collectionFragmentTag"
    val LIBRARY_HOME_FRAGMENT_TAG = "libraryFragmentTag"
    val PLAYLIST_PREVIEW_FRAGMENT_TAG = "playListPreviewTag"

    val IMAGE_QUALITY: String = "sharesPreferenceFileImageQuality"
    val SHARED_PREF_USER_SETTINGS = "sharesPreferenceSettingsFile"

    val PLAYLIST_MIXED_ID = 1
    val PLAYLIST_LATEST_ID = 2
    val PLAYLIST_TOP_ID = 3
    val PLAYLIST_SINGERONE_ID = 4
    val PLAYLIST_SINGERTWO_ID = 5

    val IMAQE_Default_QUALITY = 1
    val IMAQE_HIGH_QUALITY = 2
    val IMAQE_MEDIAM_QUALITY = 3

    val youtubeType_video = "video"
    val youtubeType_playList = "playlist"
    val filmAnimationYoutubeId = 1
    val autosVehicles = 2
    val musicYoutubeId = 10
    val pets_Animals = 15
    val sportsYoutubeId = 17
    val shortMoviesYoutubeId = 18
    val gamingYoutubeId = 20
    val comedyYoutubeId = 23
    val educationYoutubeId = 27
    val scienceTechnologyYoutubeId = 28
    val animeAnimationYoutubeId = 31
    val documentaryYoutubeId = 35
    val horrorYoutubeId = 39

    val mp3ApiTooManyRequestsError = "Too Many Requests"

    val mapOfYoutubeSearchs: List<RandomSearchQueryWithImage> = listOf(
        RandomSearchQueryWithImage(
            "Jazz", listOf<String>(
                "https://i.ytimg.com/vi/w8JhM6wRzKc/maxresdefault.jpg",
                "https://images.squarespace-cdn.com/content/v1/5511cf35e4b0e773eeb7420e/1427998743287-ZUTB7V1CXZV41QI6WZQB/Richard+Neylon+Saxophone"
            )
        ),
        RandomSearchQueryWithImage(
            "Rap", listOf(
                "https://wallpaperaccess.com/full/276347.jpg",
                "https://cdn.wallpapersafari.com/14/25/jogdtE.jpg",
                "https://cdn.wallpapersafari.com/62/95/VEHxGh.jpg"
            )
        ), RandomSearchQueryWithImage(
            "Classic", listOf(
                "https://thumbs.dreamstime.com/b/classic-violin-music-notes-dark-background-178550864.jpg",
                "https://thejewishnews.com/wp-content/uploads/2019/03/GettyImages-487109892.jpg",
                "https://m.media-amazon.com/images/I/61U7YpUem-L._SS500_.jpg"
            )
        ), RandomSearchQueryWithImage(
            "dance", listOf(
                "https://wallpaperaccess.com/full/4743322.jpg",
                "https://wallpaperaccess.com/full/4743325.jpg",
                "https://wallpaperaccess.com/full/6669969.jpg",

                )
        ),
        RandomSearchQueryWithImage(
            "8D songs", listOf(
                "https://i.pinimg.com/736x/90/8c/5e/908c5edeb32d7a35e9885adbe5a6bfe8.jpg",
                "https://i.ytimg.com/vi/EsClXtl0Vpk/maxresdefault.jpg"
            )
        ), RandomSearchQueryWithImage(
            "motivational", listOf(
                "https://wallpapercave.com/wp/wp2819443.jpg",
                "https://img.lovepik.com/photo/50085/1730.jpg_wh860.jpg"
            )
        ),
        RandomSearchQueryWithImage(
            "relax", listOf(
                "https://i.ytimg.com/vi/tQ0N_-S3DvM/maxresdefault.jpg",
                "https://i.ytimg.com/vi/fVZNMluihl4/maxresdefault.jpg",
                "https://i.ytimg.com/vi/ZuHUKcuUSE8/maxresdefault.jpg"
            )
        ),
        RandomSearchQueryWithImage(
            "Gym", listOf(
                "https://wallpaperaccess.com/full/1087589.jpg",
                "https://media.istockphoto.com/photos/workout-gym-with-gym-equipment-picture-id1135188049?k=20&m=1135188049&s=612x612&w=0&h=fV8_RLPM-42RzX3VXLYNx90NWi2tXOAfazHODyTBj2E=",
                "https://images.unsplash.com/photo-1590487988256-9ed24133863e?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTl8fGd5bXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80",
            )
        )
    )


    val MP3_API_ALL_API_KEY =
        listOf(
            //mostafa
            "1b4f301056msha34c558fa982bb8p100857jsn2b48c352e764",
            //abdelrhman
            "f9cbdffdb3msh01470815c601b1ep1e6f6fjsn6ec55644837d",
            //sasaYT
            "6ef26fe9edmsh844999cafc52541p1f9db6jsn3027459635e7",
            //facebook
            "69df107b91mshbf5463d0ab83739p1bc2d3jsnbf235a046418",
            //dumm1
            "67215c13e0msh2dab9dd8e95c191p1aae01jsnab1f01c600b2",
//            //dumm2
//            "07de125e4dmshf985756affde310p19242djsn4a33a449a558",
//            //dummy3
//        "9289181c70msheca20edcd203bf6p1bf9dcjsn785d9d70bed6",
            //dummy 4

        )
    val PRIVATE_VIDEO = "Private video"

    val listOFSingersPart1 =
        listOf<String>(
            "Daneliya Tuleshova",
            "Ariana Grande",
            "Dua Lipa",
            "Dua Lipa",
            "Dua Lipa",
            "Batisdabest added",
            "Billie Eilish",
            "Billie Eilish",
            "Taylor Swift",
            "Taylor Swift",
            "Coldplay",
            "Coldplay",
            "Ava Max",
            "Selena Gomez",
            "Doja Cat",
            "Ed Sheeran",
            "Adele",
            "Tate McRae",
            "Demi Lovato",
            " Bruno Mars",
            "Conan Gray",
            "Coldplay",
            "Coldplay",
            "Coldplay",
            "Coldplay",
            "Coldplay",


            )
    val listOFSingerspart2 =
        listOf<String>(
            "Lizzo",
            "Jonas Brothers",
            "Justin Bieber",
            "P!nk",
            "Saweetie",
            "Joshua Bassett",
            "The Black Eyed Peas",
            "Dixie D Amelio",
            "The Weeknd",
            "Zayn Malik",
            "Dua Lipa",
            "Adam Levine",
            "Miley Cyrus",
            "Shawn Mendes",
            "Post Malone",
            "Khalid",
            "Demi Lovato",
            "Sam Smith",
            "Doja Cat",
            "Chris Brown",
            "Olivia Rodrigo",
            "Sia",
            "Sia",
            "Sia",
            "charlotte cardin",
            "charlotte cardin",
            "charlotte cardin",
            "Beyonce",
            "Lady Gaga",
            "Micheal Jackson"
        )

}
