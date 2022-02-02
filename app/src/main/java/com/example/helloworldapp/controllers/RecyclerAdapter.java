package com.example.helloworldapp.controllers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworldapp.MainActivity;
import com.example.helloworldapp.R;
import com.example.helloworldapp.models.Person;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private ArrayList<Person> personsList;
    private MainActivity mainActivity;
    public RecyclerAdapter(ArrayList<Person> personsList, MainActivity mainActivity) {
        this.personsList = personsList;
        this.mainActivity = mainActivity;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView prenom;
        private TextView nom;
        private TextView email;
        private TextView dateNaissance;
        private AppCompatImageButton editBtn;
        private AppCompatImageButton deleteBtn;
        public MyViewHolder(final View itemView) {
            super(itemView);
            prenom = itemView.findViewById(R.id.firstname_text);
            nom = itemView.findViewById(R.id.lastname_text);
            email = itemView.findViewById(R.id.email_text);
            dateNaissance = itemView.findViewById(R.id.birth_text);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_line_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        final Person person = personsList.get(position);
        if (person.getPrenom() == null ) return;
        holder.prenom.setText(person.getPrenom().length() > 0 ? person.getPrenom().substring(0, 1).toUpperCase() + person.getPrenom().substring(1): "");
        holder.nom.setText(person.getNom().toUpperCase());
        holder.email.setText(person.getEmail());
        holder.dateNaissance.setText("Né(e) le " + person.getDateNaissance());

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.delete(person.getEmail());
            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setEditView(person);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }
}
