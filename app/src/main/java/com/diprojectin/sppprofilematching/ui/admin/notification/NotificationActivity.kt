package com.diprojectin.sppprofilematching.ui.admin.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.diprojectin.models.Notification
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.NotificationResponse
import com.diprojectin.network.responses.SppResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityNotificationBinding
import com.diprojectin.sppprofilematching.ui.adapters.NotificationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding
    private lateinit var adapter: NotificationAdapter
    private lateinit var listNotification: List<Notification>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initData()
        initView()
    }

    private fun initData() {
        getNotifications()
    }

    private fun initView() = with(binding) {
        appBar.tvTitleHeader.text = "Notifikasi"
        appBar.btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun setRecyclerView() = with(binding) {
       adapter = NotificationAdapter(this@NotificationActivity, arrayListOf(), object : NotificationAdapter.OnItemClickListener{
           override fun onItemClick(item: Notification) {
               val intent = Intent(this@NotificationActivity, DetailNotifiactionActivity::class.java)
               intent.putExtra("judul", item.title)
               intent.putExtra("deskripsi", item.deskripsi)
               intent.putExtra("image", item.imagePath)
               startActivity(intent)
           }
       })

        rvNotification.adapter = adapter
        rvNotification.layoutManager = LinearLayoutManager(this@NotificationActivity)
        rvNotification.setHasFixedSize(true)

        adapter.setList(listNotification)
    }

    private fun getNotifications() = with(binding) {
        binding.loadingView.visibility = View.VISIBLE
        val apiClient = ApiClient.client(this@NotificationActivity)?.create(ApiInterface::class.java)
        val call = apiClient?.getNotification()
        call?.enqueue(object: Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                binding.loadingView.visibility = View.GONE

                if(!response.isSuccessful){
                    Toast.makeText(this@NotificationActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        listNotification = response.body()?.data!!

                        setRecyclerView()
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@NotificationActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                binding.loadingView.visibility = View.GONE
                Toast.makeText(this@NotificationActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}