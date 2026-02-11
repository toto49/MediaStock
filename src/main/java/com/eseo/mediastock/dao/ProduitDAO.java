package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ProduitDAO {
    void addProduit(Produit p) throws SQLException;

}

