package com.example.lab4;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText editTextProductName;
    private EditText editTextPrice;
    private Button buttonAdd;
    private ListView listViewProducts;

    private DatabaseReference databaseProducts;

    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Initialisation des contrôles
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewProducts = findViewById(R.id.listViewProducts);

// Liste des produits
        productList = new ArrayList<>();

// Référence Firebase
        databaseProducts = FirebaseDatabase.getInstance().getReference("products");
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        databaseProducts.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                productList.clear();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {

                    Product product = productSnapshot.getValue(Product.class);

                    if (product != null) {
                        productList.add(product);
                    }
                }

                ArrayAdapter<Product> adapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        productList
                );

                listViewProducts.setAdapter(adapter);
                listViewProducts.setOnItemLongClickListener((parent, view, position, id) -> {

                    Product product = productList.get(position);

                    Intent intent = new Intent(MainActivity.this, UpdateProductActivity.class);

                    intent.putExtra("productId", product.getProductId());
                    intent.putExtra("productName", product.getProductName());
                    intent.putExtra("productPrice", product.getProductPrice());

                    startActivity(intent);

                    return true;
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(
                        MainActivity.this,
                        databaseError.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    private void addProduct() {

        String name = editTextProductName.getText().toString().trim();
        String priceText = editTextPrice.getText().toString().trim();

        if (!name.isEmpty() && !priceText.isEmpty()) {

            double price = Double.parseDouble(priceText);

            String id = databaseProducts.push().getKey();

            Product product = new Product(id, name, price);

            databaseProducts.child(id).setValue(product);

            Toast.makeText(this,
                    "Product Added",
                    Toast.LENGTH_SHORT).show();

            editTextProductName.setText("");
            editTextPrice.setText("");

        } else {

            Toast.makeText(this,
                    "Please enter product name and price",
                    Toast.LENGTH_SHORT).show();
        }
    }
}