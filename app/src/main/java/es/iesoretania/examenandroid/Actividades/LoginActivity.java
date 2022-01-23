package es.iesoretania.examenandroid.Actividades;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import es.iesoretania.examenandroid.Miscelanea.AdminSQLiteOpenHelper;
import es.iesoretania.examenandroid.R;
import es.iesoretania.examenandroid.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Login App Calificaciones");
    }

    public void registrar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(
                this, "calificaciones", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        if (!binding.EditTextUsuario.getText().toString().isEmpty() &&
                !binding.EditTextPassword.getText().toString().isEmpty()){
            String user = binding.EditTextUsuario.getText().toString();
            String password = binding.EditTextPassword.getText().toString();

            Cursor fila =BaseDeDatos.rawQuery
                    ("select * from alumno where idalumno like '"+user+"'", null);

            if (fila.moveToFirst()) {
                if (fila.getString(0).equals(user)){
                    muestraAlerta("El usuario ya existe");
                }
            }else {
                Intent intentMain = new Intent(this, RegistrarActivity.class);
                //Guardo los datos en Shared Preferences.
                SharedPreferences sharedPreferences =
                        getSharedPreferences("login",
                                Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("usuario",user);
                editor.putString("contraseña",password);
                editor.commit();
                BaseDeDatos.close();
                startActivity(intentMain);
            }
        }else{
            muestraAlerta("Se deben rellenar todos los campos");
        }
    }

    public void acceder(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(
                this, "calificaciones", null, 1);
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        if (!binding.EditTextUsuario.getText().toString().isEmpty() &&
                !binding.EditTextPassword.getText().toString().isEmpty()){
            String user = binding.EditTextUsuario.getText().toString();
            String password = binding.EditTextPassword.getText().toString();

            Cursor fila =BaseDeDatos.rawQuery
                    ("select password from alumno where idalumno like '"+user+"'", null);

            if (fila.moveToFirst()) {
                if (fila.getString(0).equals(password)){
                    Intent intentMain = new Intent(this, HomeActivity.class);
                    SharedPreferences sharedPreferences =
                            getSharedPreferences("login",
                                    Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario",user);
                    editor.putString("contraseña",password);
                    editor.commit();
                    BaseDeDatos.close();
                    startActivity(intentMain);
                }else{
                    muestraAlerta("Contraseña incorrecta");
                }
            }else {
                muestraAlerta("El usuario no existe");
            }
        }else{
            muestraAlerta("Se deben rellenar todos los campos");
        }
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