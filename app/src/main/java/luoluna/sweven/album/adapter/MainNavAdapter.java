package luoluna.sweven.album.adapter;

import android.app.Activity;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import luoluna.sweven.album.R;

public class MainNavAdapter implements NavigationView.OnNavigationItemSelectedListener {
    private Activity context;
    private NavigationView navigationView;
    private MainDrawerAdapter adapter;

    public MainNavAdapter(Activity context) {
        this.context = context;
        init();
    }

    public void setAdapter(MainDrawerAdapter adapter) {
        this.adapter = adapter;
    }

    private void init() {
        navigationView = context.findViewById(R.id.main_nav);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            default:
                break;
        }
        adapter.closeDrawer();
        return false;
    }
}
