package com.monolit.mobilerealty.Room.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.monolit.mobilerealty.RealtorObjects.Reservation;

import java.util.List;

@Dao
public interface ReservationsDao {

    @Query("SELECT * FROM reservations ORDER BY client")
    List<Reservation> getAllReservations();

    @Query("SELECT * FROM reservations WHERE client LIKE '%' || :name || '%' OR status LIKE  '%' || :name || '%' OR object LIKE '%' || :name || '%' ORDER BY client")
    List<Reservation> getReservationsByFilter(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReservations(List<Reservation> reservations);

    @Query("DELETE FROM reservations")
    void deleteAll();
}
