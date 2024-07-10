package com.example.product_sale.chat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.product_sale.R;
import com.example.product_sale.activity.BaseFragment;
import com.example.product_sale.config.AppConfig;
import com.example.product_sale.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatFragment extends BaseFragment {
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FragmentChatBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            if (isAdmin(currentUser)) {
                getFragmentManager().beginTransaction().replace(R.id.chat_container, new ChatFragmentAdmin()).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.chat_container, new ChatFragmentUser()).commit();
            }
        }
        return root;
    }

    private boolean isAdmin(FirebaseUser user) {
        if (user.getEmail().equals(AppConfig.EMAIL_ADMIN)) return true;
        else return false;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
