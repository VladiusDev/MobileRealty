package com.monolit.mobilerealty.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.RealtorObjects.RealtyObject;

import java.util.List;

@Dao
public interface RealtyObjectsDao {

    @Query("SELECT * FROM realtyObjects ORDER BY name")
    List<RealtyObject> getAllObjects();

    @Query("SELECT * FROM realtyObjects WHERE name LIKE :name ORDER BY name")
    List<RealtyObject> getRealtyObjectsByFilter(String name);

    @Query("SELECT * FROM realtyObjects WHERE id1c == :id")
    RealtyObject getRealtyObjectById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRealtyObject(RealtyObject realtyObject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllRealtyObjects(List<RealtyObject> realtyObjects);


}
