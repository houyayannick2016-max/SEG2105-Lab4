package com.example.lab4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText editTextProductName;
    private EditText editTextPrice;

    private Button buttonUpdate;
    private Button buttonDelete;

    private DatabaseReference databaseProducts;

    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_product);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextPrice = findViewById(R.id.editTextPrice);

        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);

        databaseProducts = FirebaseDatabase.getInstance().getReference("products");

        // Récupération des données envoyées par MainActivity
        productId = getIntent().getStringExtra("productId");
        String productName = getIntent().getStringExtra("productName");
        double productPrice = getIntent().getDoubleExtra("productPrice", 0);

        editTextProductName.setText(productName);
        editTextPrice.setText(String.valueOf(productPrice));

        buttonUpdate.setOnClickListener(v -> updateProduct());

        buttonDelete.setOnClickListener(v -> deleteProduct());
    }

    private void updateProduct() {

        String name = editTextProductName.getText().toString().trim();
        String priceText = editTextPrice.getText().toString().trim();

        if (name.isEmpty() || priceText.isEmpty()) {

            Toast.makeText(this,
                    "Please enter all values",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceText);

        Product product = new Product(productId, name, price);

        databaseProducts.child(productId).setValue(product);

        Toast.makeText(this,
                "Product Updated",
                Toast.LENGTH_SHORT).show();

        finish();
    }

    private void deleteProduct() {

        databaseProducts.child(productId).removeValue();

        Toast.makeText(this,
                "Product Deleted",
                Toast.LENGTH_SHORT).show();

        finish();
    }
}