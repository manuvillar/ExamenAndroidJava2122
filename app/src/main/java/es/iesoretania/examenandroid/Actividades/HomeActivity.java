package es.iesoretania.examenandroid.Actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import es.iesoretania.examenandroid.Miscelanea.AdminSQLiteOpenHelper;
import es.iesoretania.examenandroid.R;
import es.iesoretania.examenandroid.databinding.ActivityMainBinding;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences =
                getSharedPreferences("login", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("usuario", "");

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.inicioFragment)
                .setOpenableLayout(drawer)
                .build();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "calificaciones", null, 4);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor fila =BaseDeDatos.rawQuery
                ("select * from alumno where idalumno like '"+user+"'", null);

        if (fila.moveToFirst()) {
            ImageView avatar = navigationView.getHeaderView(0).findViewById(R.id.imageViewHeader);
            if (fila.getString(4).equals("HOMBRE")) {
                avatar.setImageResource(R.drawable.ic_man);
            } else {
                avatar.setImageResource(R.drawable.ic_woman);
            }

            TextView textView1 = navigationView.getHeaderView(0).findViewById(R.id.textviewHeader1);
            textView1.setText(fila.getString(2));
            TextView textView2 = navigationView.getHeaderView(0).findViewById(R.id.textViewHeader2);
            textView2.setText(fila.getString(3));
        }

        NavController navController = Navigation.findNavController(this, R.id.contenedor);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_salir:
                Intent intentLogin = new Intent(this,LoginActivity.class);
                startActivity(intentLogin);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.contenedor);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}