package com.example.taye.ProjectALC.controller;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taye.ProjectALC.R;
import com.example.taye.ProjectALC.adapter.RecyclerViewAdapter;
import com.example.taye.ProjectALC.model.Item;
import com.example.taye.ProjectALC.model.ItemResponse;
import com.example.taye.ProjectALC.network.APIClient;
import com.example.taye.ProjectALC.network.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    TextView Connect;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.launcher);

        display();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.magnitude9);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserList();
                Toast.makeText(MainActivity.this, "List Refreshed....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserList() {
        Connect = (TextView) findViewById(R.id.connect);
        try {
            APIService service = APIClient.getClient().create(APIService.class);
            Call<ItemResponse> call = service.getItems();
            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                    List<Item> items = response.body().getItems();
                    recyclerView.setAdapter(new RecyclerViewAdapter(getApplicationContext(), items));
                    recyclerView.smoothScrollToPosition(0);
                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.hide();
                }

                @Override
                //When internet is missing this is method is called

                public void onFailure(Call<ItemResponse> call, Throwable t) {
                    Log.d("onFailure", t.toString());
                    Toast.makeText(MainActivity.this, "Unable to fetch data,Please Connect to the Internet...", Toast.LENGTH_SHORT).show();
                    Connect.setVisibility(View.VISIBLE);
                    progressDialog.hide();
                }
            });
        }
        catch (Exception ex) {
            Log.d("Error", ex.getMessage());
            Toast.makeText(MainActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //display method is called when fetching Java Dev from the internet
    private void display() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Java Developers in Lagos.....");
        progressDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        getUserList();
    }
}
