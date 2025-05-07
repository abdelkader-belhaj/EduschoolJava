package tn.eduskool.controllers;

import tn.eduskool.entities.Utilisateur;

public interface BaseController {
    void setUtilisateur(Utilisateur utilisateur);

    Utilisateur getUtilisateur();
}