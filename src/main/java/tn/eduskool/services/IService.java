package tn.eduskool.services;

import java.util.List;

public interface IService<T> {
    boolean ajouter(T t);
    boolean supprimer(int id);
    boolean modifier(T t, String ancienFichier);
    boolean modifier(T t);
    List<T> recuperer();
}
