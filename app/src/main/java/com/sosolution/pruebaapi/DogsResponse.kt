package com.sosolution.pruebaapi

import com.google.gson.annotations.SerializedName

/**
 *
 * para la transformacion de json a data class se puede usar el @SerializeName para no usar el mismo nombre exacto del json
 */
data class DogsResponse (@SerializedName("status") var status:String,
                         @SerializedName("message")var images:List<String>
                         )