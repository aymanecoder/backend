package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "webscraping")
public class Scrapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String Titre;

    private String URL;

    private String Site_Name;

    private String Date_de_publication;

    private String Date_pour_postuler;

    private String Adresse_entreprise;

    private String Nom_entreprise;

    private String Description_entreprise;

    private String Description_du_poste;

    private String Region;

    private String ville;

    private String Secteur_activite;

    private String Metier;

    private String Type_du_contrat;

    private String Niveau_etudes;

    private String Specialite;

    private String Diplome;

    private String Experience;

    private String Profil_recherche;

    private String Traits_de_personnalite;


    private String Competences_requisesHard_skills;

    private String Soft_Skills;

    private String Teletravail;


}
