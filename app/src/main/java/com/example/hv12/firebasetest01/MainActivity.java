package com.example.hv12.firebasetest01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    public static final String TITULO = "titulo";
    public static final String MSG = "msg";
    //controles
    EditText txtTitulo;
    EditText txtMsg;
    Button btnGuardar;
    TextView lblMsg;
    //referencia al documento el firebase
    DocumentReference mDocRef = FirebaseFirestore.getInstance().document("mensaje/test");


    @Override
    protected void onStart() {
        super.onStart();

        mDocRef.addSnapshotListener(this, (documentSnapshot, exception) -> {
            if(documentSnapshot.exists()){
                String titulo = documentSnapshot.getString(TITULO);
                String msg = documentSnapshot.getString(MSG);
                lblMsg.setText("Titulo : "+titulo+"\nMensaje : "+msg);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitulo  = findViewById(R.id.txtTitulo);
        txtMsg     = findViewById(R.id.txtMensaje);
        btnGuardar = findViewById(R.id.btnGuardar);
        lblMsg     = findViewById(R.id.lblMsg);

        btnGuardar.setOnClickListener(v ->{
            saveMsg();
        });

    }

    private void saveMsg(){
        String title = txtTitulo.getText().toString();
        String msg   = txtMsg.getText().toString();
        if(title.isEmpty() || msg.isEmpty()){
            Toast.makeText(this, "Ingrese texto", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,Object> dataToSave = new HashMap<>();
        dataToSave.put(TITULO,title);
        dataToSave.put(MSG,msg);
        mDocRef.set(dataToSave).addOnSuccessListener(this,v->{
            Toast.makeText(MainActivity.this, "Mensaje guardado", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(this,v->{
            Toast.makeText(MainActivity.this, "No se pudo guardar", Toast.LENGTH_SHORT).show();
        });

    }
}
