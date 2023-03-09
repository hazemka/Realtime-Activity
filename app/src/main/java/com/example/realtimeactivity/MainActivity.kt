package com.example.realtimeactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.realtimeactivity.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var database:FirebaseDatabase
    private lateinit var mainRef:DatabaseReference
    private lateinit var binding:ActivityMainBinding
    private var id:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database
        mainRef = database.reference

        binding.btnSave.setOnClickListener {
            if (binding.txtConAddress.text.isNotEmpty() && binding.txtConNumber.text.isNotEmpty()
                && binding.txtConName.text.isNotEmpty()) {
                addContact(Contact(binding.txtConName.text.toString(), binding.txtConNumber.text.toString(),
                    binding.txtConAddress.text.toString()))
            }else{
                Toast.makeText(this, "Please, All info are required !", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGetData.setOnClickListener{
            getData()
        }
    }

    private fun addContact(contact: Contact){
        mainRef.child("Persons").child("$id").setValue(contact)
        id++
        Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun getData(){
        mainRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (person in snapshot.child("Persons").children){
                    val person = person.getValue<Contact>()
                    binding.textViewData.text = "${binding.textViewData.text}\n${person!!.name} ${person.number} ${person.address}"
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}