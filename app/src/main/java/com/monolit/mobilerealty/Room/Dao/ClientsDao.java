package com.monolit.mobilerealty.Room.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.monolit.mobilerealty.RealtorObjects.Client;

import java.util.List;

@Dao
public interface ClientsDao {


    @Query("SELECT * FROM clients ORDER BY name LIMIT 100 OFFSET :offset")
    List<Client> getAllClients(int offset);

    @Query("SELECT * FROM clients WHERE name LIKE '%' || :name || '%' OR phone LIKE '%' || :name || '%' OR email LIKE '%' || :name || '%' ORDER BY name")
    List<Client> getClientsByFilter(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClients(List<Client> clients);

}
