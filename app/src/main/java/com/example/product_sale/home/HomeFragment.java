package com.example.product_sale.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sale.R;
import com.example.product_sale.activity.CartActivity;
import com.example.product_sale.activity.HomeActivity;
import com.example.product_sale.activity.LoginActivity;
import com.example.product_sale.adapter.PetAdapter;
import com.example.product_sale.databinding.FragmentHomeBinding;
import com.example.product_sale.models.Cart;
import com.example.product_sale.models.Pet;
import com.example.product_sale.service.PetApiService;
import com.example.product_sale.utils.MyDatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PetAdapter petAdapter;
    private RecyclerView rvPets;
    private List<Pet> mListPet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        rvPets = binding.rvPets;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvPets.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvPets.addItemDecoration(itemDecoration);

        mListPet = new ArrayList<>();
        petAdapter = new PetAdapter(getActivity(), mListPet, Cart.getInstance());
        rvPets.setAdapter(petAdapter);
        callApiGetPets();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void callApiGetPets() {
        PetApiService petApiService = PetApiService.retrofit.create(PetApiService.class);
        petApiService.getPets(null, null).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                if (response.isSuccessful()) {
                    mListPet.clear(); // Xóa danh sách cũ
                    mListPet.addAll(response.body()); // Thêm danh sách mới từ response
                    petAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                } else {
                    Toast.makeText(getActivity(), "Failed to get pets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
                Log.e("API_ERROR", "Failure: " + t.getMessage());
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu); // Inflate the menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_home_cart) {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(Cart.getInstance().getCartItems()));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
