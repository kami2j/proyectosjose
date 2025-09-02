package com.modasmart.modasmartfinal.ui.inicio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.modasmart.modasmartfinal.Buscarproducto
import com.modasmart.modasmartfinal.R
import com.modasmart.modasmartfinal.databinding.FragmentInicioBinding

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val btnPolera = binding.buttonPolera
        val btnPantalon = binding.buttonPantalon
        val btnCamisa = binding.buttonCamisa
        val btnDeportivo = binding.buttonDeportiva
        val btnVestido = binding.buttonVestido
        val btnCalzado = binding.buttonCalzado
        val btnInterior = binding.buttonInterior
        val btnAccesorio = binding.buttonAccesorio
        val btnReciclar = binding.buttonReciclar


        btnPolera.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_reciclar)
        }

        btnPolera.setOnClickListener {
            buscarProductos("Polera")
        }

        btnPantalon.setOnClickListener {
            buscarProductos("Pantalon")
        }

        btnCamisa.setOnClickListener {
            buscarProductos("Camisa")
        }
        btnDeportivo.setOnClickListener {
            buscarProductos("Deportivo")
        }

        btnVestido.setOnClickListener {
            buscarProductos("Vestido")
        }

        btnCalzado.setOnClickListener {
            buscarProductos("Calzado")
        }
        btnInterior.setOnClickListener {
            buscarProductos("Interior")
        }
        btnAccesorio.setOnClickListener {
            buscarProductos("Accesorio")
        }

        return root
    }

    private fun buscarProductos(categoria: String) {
        val intent = Intent(activity, Buscarproducto::class.java)
        intent.putExtra("categoria", categoria)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}