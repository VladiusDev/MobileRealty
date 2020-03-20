package com.monolit.mobilerealty.Room.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.monolit.mobilerealty.RealtorObjects.RealtyObject;

import java.util.List;

@Dao
public interface RealtyObjectsDao {

    @Query("SELECT * FROM realtyObjects ORDER BY name")
    List<RealtyObject> getAllObjects();

    @Query("SELECT * FROM realtyObjects WHERE name LIKE '%' ||  :name || '%' ORDER BY name")
    List<RealtyObject> getRealtyObjectsByFilter(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllRealtyObjects(List<RealtyObject> realtyObjects);

    @Query("DELETE FROM realtyObjects")
    void deleteAll();

}
