package es.iesoretania.examenandroid.Actividades;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import es.iesoretania.examenandroid.Miscelanea.AdminSQLiteOpenHelper;
import es.iesoretania.examenandroid.databinding.ActivityRegistrarBinding;

public class RegistrarActivity extends AppCompatActivity {
    private ActivityRegistrarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Crear Cuenta");

        String[] opciones = {"HOMBRE", "MUJER"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, opciones);
        binding.spinner.setAdapter(adapter);
    }

    public void aceptar(View view) {
        String nombre, apellidos, sexo;

        SharedPreferences sharedPreferences =
                getSharedPreferences("login", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("usuario", "");
        String contraseña = sharedPreferences.getString("contraseña", "");

        nombre = binding.EditTextNombre.getText().toString();
        apellidos = binding.EditTextApellidos.getText().toString();
        sexo = binding.spinner.getSelectedItem().toString();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(
                this, "calificaciones", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        if (!nombre.isEmpty() && !apellidos.isEmpty()){
            ContentValues registro = new ContentValues();

            registro.put("idalumno",user);
            registro.put("password",contraseña);
            registro.put("nombre",nombre);
            registro.put("apellidos",apellidos);
            registro.put("sexo",sexo);

            BaseDeDatos.insert("alumno", null, registro);

            BaseDeDatos.close();
            Toast.makeText(this, "Alumno creado correctamente", Toast.LENGTH_SHORT).show();
            Intent intentMain = new Intent(this, HomeActivity.class);
            intentMain.putExtra("user", user);
            intentMain.putExtra("pasword", contraseña);
            startActivity(intentMain);
        } else{
            muestraAlerta("Debes introducir todos los campos");
        }
    }

    public void cancelar(View view) {
        Intent intentLogin = new Intent(this,LoginActivity.class);
        startActivity(intentLogin);
    }

    private void muestraAlerta(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialogo = builder.create();
        dialogo.show();
    }
}