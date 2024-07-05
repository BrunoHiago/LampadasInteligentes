package com.example.projetos7


import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class Api(){
    val client = OkHttpClient()
    val url = "http://192.168.244.195"
    companion object {
        val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
        val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    fun ligarLed(idLed:String, state: Boolean){
        val json = """
                    {
                    }
                    """.trimIndent()

        val request = Request.Builder()
            .url("${url}/switch/${idLed}/${if (state)"turn_on" else "turn_off"}")
            .post(json.toRequestBody(MEDIA_TYPE_JSON))
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                println("OK:"+response.body!!.string())
            }
        }catch (e:Exception){
            println("Erro:"+e.message)
        }



    }


    fun estadoLed(idLed: String): Boolean {
        var value = false
        val request = Request.Builder()
            .url("${url}/switch/${idLed}")
            .get()
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val jsonResponse = response.body?.string() // Obtém o corpo da resposta como uma String
                jsonResponse?.let { // Verifica se o corpo da resposta não é nulo
                    val jsonObject = JSONObject(it) // Converte a String JSON para um JSONObject

                    val id = jsonObject.getString("id")
                    value = jsonObject.getBoolean("value")
                    val state = jsonObject.getString("state")

                    // Faça o que quiser com as informações obtidas
                    println("ID: $id, Value: $value, State: $state")

                }
            }
        }catch (e:Exception){
            println("Erro:"+e.message)
        }

        return value
    }

    fun ledRGB(ligar:Boolean, r:Float,g:Float,b:Float,brilho:Float){
        val json = """
                    {
                    }
                    """.trimIndent()
        val request = Request.Builder()
            .url("${url}/light/rgb_light/turn_on?r=$r&g=$g&b=$b&brightness=$brilho")
            .post(json.toRequestBody(MEDIA_TYPE_JSON))
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                println("Funcionou:"+response.body!!.string())
            }
        }catch (e:Exception){
            println("Erro:"+e.message)
        }


    }
}