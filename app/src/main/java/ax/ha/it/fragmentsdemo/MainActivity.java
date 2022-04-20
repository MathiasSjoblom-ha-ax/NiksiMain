package ax.ha.it.fragmentsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ax.ha.it.fragmentsdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.main_activity);
    }
}
