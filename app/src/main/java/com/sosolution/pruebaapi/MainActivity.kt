package com.sosolution.pruebaapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.sosolution.pruebaapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() , SearchView.OnQueryTextListener {

    //variable para llamar el xml
    private lateinit var  binding:ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //nueva forma de implementar las llamadas xml
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchDogs.setOnQueryTextListener(this)
        //inicializar recicler view
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        binding.recyclerViewDogs.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDogs.adapter = adapter


    }

    private fun getRetrofit() :Retrofit{
        //retorno en retrofit lo pedido por
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query:String){
        //verificar si query esta normalizado
        //todo lo implementado aqui pasara aun hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            val call= getRetrofit().create(ApiService::class.java).getDogsByBreeds("$query/images")
            val puppies = call.body()

            //ejecutar en el hilo principal
            runOnUiThread {

                if(call.isSuccessful){
                    //mostrar recicler view
                    val images = puppies?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images)
                    adapter.notifyDataSetChanged()


                }else{
                    //show error
                    showError("No se pudo cargar la busqueda")
                }

            }


        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message , Toast.LENGTH_LONG).show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            searchByName(query.lowercase())
        }
        return true
    }

    //cada letra o borre el usuario
    override fun onQueryTextChange(p0: String?): Boolean {
       return true
    }
}

