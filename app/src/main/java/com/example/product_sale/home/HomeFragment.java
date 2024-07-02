package com.example.product_sale.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.product_sale.adapter.PetAdapter;
import com.example.product_sale.databinding.FragmentHomeBinding;
import com.example.product_sale.models.Pet;
import com.example.product_sale.utils.MyDatabaseHelper;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private PetAdapter petAdapter;
    private List<Pet> petList;
    private MyDatabaseHelper databaseHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.petList;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        databaseHelper = new MyDatabaseHelper(requireContext());
        databaseHelper.addSamplePets();
        petList = databaseHelper.getAllPets();

        petAdapter = new PetAdapter(petList);
        recyclerView.setAdapter(petAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
