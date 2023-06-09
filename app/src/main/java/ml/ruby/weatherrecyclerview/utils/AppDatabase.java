package ml.ruby.weatherrecyclerview.utils;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ml.ruby.weatherrecyclerview.MyApplication;
import ml.ruby.weatherrecyclerview.room.PlacesDbDao;
import ml.ruby.weatherrecyclerview.model.db.PlaceRecodeItem;

/**
 * @author: jwhan
 * @createTime: 2022/05/18 9:29 PM
 * @description:
 */
@Database(entities = {PlaceRecodeItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlacesDbDao getPlacesDbDao();

    private static class AppDatabaseContainer {
        private static final AppDatabase instance =
                Room.databaseBuilder(MyApplication.getContext(), AppDatabase.class, "places.db").build();
    }

    public static AppDatabase getInstance() {
        return AppDatabaseContainer.instance;
    }
}
