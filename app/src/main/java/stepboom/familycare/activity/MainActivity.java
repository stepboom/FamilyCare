package stepboom.familycare.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import stepboom.familycare.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, HomeFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        }*/
    }

    private void initInstances() {
    }
}
