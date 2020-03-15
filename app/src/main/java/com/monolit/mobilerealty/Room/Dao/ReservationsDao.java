package com.monolit.mobilerealty.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.monolit.mobilerealty.RealtorObjects.Client;
import com.monolit.mobilerealty.RealtorObjects.Reservation;
import com.monolit.mobilerealty.RealtorObjects.Task;

import java.util.List;

@Dao
public interface ReservationsDao {

    @Query("SELECT * FROM reservations ORDER BY client")
    List<Reservation> getAllReservations();

    @Query("SELECT * FROM reservations WHERE client LIKE :client OR status LIKE :status OR object LIKE :object ORDER BY client")
    List<Reservation> getReservationsByFilter(String client, String status, String object);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReservation(Reservation reservation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReservations(List<Reservation> reservations);

}
