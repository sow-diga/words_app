package com.mas.quranwords.qari

object Reciters {
    val WORD_ONLY = Reciter("Word only", null)
    val AYMAN_SUWAID = Reciter("Ayman Suwaid", "Ayman_Sowaid_64kbps")
    val HUSARY = Reciter("Husary", "Husary_64kbps")

    val ALL = listOf(
        WORD_ONLY,
        HUSARY,
        AYMAN_SUWAID,
        Reciter("HusaryMuallim", "Husary_Muallim_128kbps"),
        Reciter("Minshawi", "Minshawy_Murattal_128kbps"),
        Reciter("Ibrahim Akhdar", "Ibrahim_Akhdar_32kbps"),
        Reciter("Al Banna", "mahmoud_ali_al_banna_32kbps"),
        Reciter("Abdullah Basfar", "Abdullah_Basfar_192kbps")
    )
}