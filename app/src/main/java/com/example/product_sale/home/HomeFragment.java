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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.product_sale.R;
import com.example.product_sale.activity.BaseFragment;
import com.example.product_sale.activity.CartActivity;
import com.example.product_sale.activity.OrderActivity;
import com.example.product_sale.adapter.PetAdapter;
import com.example.product_sale.databinding.FragmentHomeBinding;
import com.example.product_sale.models.Cart;
import com.example.product_sale.models.Pet;
import com.example.product_sale.models.PetType;
import com.example.product_sale.service.PetApiService;
import com.example.product_sale.service.PetTypeApiService;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;
    private PetAdapter petAdapter;
    private RecyclerView rvPets;
    private List<Pet> mListPet;
    private List<PetType> mPetTypeList;
    private ImageButton btnDetails;
    private Spinner spinnerPetType;
    private EditText etBreed;
    private Button btnSearch, btnClean;
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
        mPetTypeList = new ArrayList<>();
        petAdapter = new PetAdapter(getActivity(), mListPet, Cart.getInstance());
        rvPets.setAdapter(petAdapter);

        btnSearch = binding.btnSearch;
        btnClean = binding.btnClean;
        etBreed = binding.etBreed;
        spinnerPetType = binding.spinnerPetType;

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedPetType = spinnerPetType.getSelectedItem().toString();
                String breed = etBreed.getText().toString().trim();
                callApiGetPets(selectedPetType, breed);
            }
        });
        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etBreed.setText("");
                spinnerPetType.setSelection(0); // Chọn lại hint trong Spinner
                callApiGetPets(null, null); // Load lại danh sách pet
            }
        });
        loadPetTypes();
        callApiGetPets(null, null);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadPetTypes() {
        PetTypeApiService petTypeApiService = PetTypeApiService.retrofit.create(PetTypeApiService.class);
        petTypeApiService.getPetTypes().enqueue(new Callback<List<PetType>>() {
            @Override
            public void onResponse(Call<List<PetType>> call, Response<List<PetType>> response) {
                if (response.isSuccessful()) {
                    mPetTypeList.clear();
                    mPetTypeList.addAll(response.body());

                    List<String> petTypeNames = new ArrayList<>();
                    petTypeNames.add("Chọn loại"); // Thêm hint vào đầu danh sách
                    for (PetType petType : mPetTypeList) {
                        petTypeNames.add(petType.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, petTypeNames) {
                        @Override
                        public boolean isEnabled(int position) {
                            // Vô hiệu hóa lựa chọn hint
                            return position != 0;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if (position == 0) {
                                // Thiết lập màu cho hint
                                tv.setTextColor(Color.GRAY);
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            return view;
                        }
                    };
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerPetType.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "Failed to load pet types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PetType>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to load pet types", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void callApiGetPets(String petType, String breed) {
        if (petType != null && (petType.isEmpty() || petType.equals("Chọn loại"))) {
            petType = null;
        }
        List<Pet> listPet = new ArrayList<>();
        PetApiService petApiService = PetApiService.retrofit.create(PetApiService.class);
        petApiService.getPets(petType, breed).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                if (response.isSuccessful()) {
                    mListPet.clear();
                    listPet.clear();
                    listPet.addAll(response.body());
                    for (Pet pet:listPet){
                        if(pet.isAvailable()){
                            mListPet.add(pet);
                        }
                    }
                    petAdapter.notifyDataSetChanged();
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
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(Cart.getInstance().getCartItems()));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
