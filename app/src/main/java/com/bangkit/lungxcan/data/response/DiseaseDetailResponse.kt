package com.bangkit.lungxcan.data.response

import com.google.gson.annotations.SerializedName

data class DiseaseDetailResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id_diseases")
	val idDiseases: Int,

	@field:SerializedName("detail")
	val detail: String,

	@field:SerializedName("id_name")
	val idName: Int
)
