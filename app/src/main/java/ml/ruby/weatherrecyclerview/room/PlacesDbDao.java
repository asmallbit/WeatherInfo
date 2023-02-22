package ml.ruby.weatherrecyclerview.room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import ml.ruby.weatherrecyclerview.model.db.PlaceRecodeItem;

/**
 * @author: jwhan
 * @createTime: 2022/05/16 10:49 PM
 * @description: The dao for place database
 */
@Dao
public interface PlacesDbDao {
    // For star place
    @Insert()
    public void insert(PlaceRecodeItem... places);

    // For unstar place
    @Query("DELETE FROM places_table WHERE latitude = :lat AND longitude = :lon AND is_for_star = :isForStar")
    public void deleteByPosition(Double lat, Double lon, boolean isForStar);

    @Query("SELECT * FROM places_table")
    public List<PlaceRecodeItem> getAll();

    @Query("SELECT * FROM places_table WHERE place = :place AND (state = :state OR state IS NULL) AND country = :country AND latitude = :lat AND longitude = :lon")
    public List<PlaceRecodeItem> isRecordExist(String place, String state, String country, Double lat, Double lon);
}
