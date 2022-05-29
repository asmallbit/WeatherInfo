package ml.ruby.weatherrecyclerview.utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import ml.ruby.weatherrecyclerview.model.db.PlaceRecodeItem;

/**
 * @author: jwhan
 * @createTime: 2022/05/30 5:25 PM
 * @description:
 */
public class PlacesDiffCallback extends DiffUtil.Callback {
    private List<PlaceRecodeItem> currPlaces;
    private List<PlaceRecodeItem> newPlaces;

    public PlacesDiffCallback(@NonNull List<PlaceRecodeItem> currPlaces, @NonNull List<PlaceRecodeItem> newPlaces) {
        this.currPlaces = currPlaces;
        this.newPlaces = newPlaces;
    }

    @Override
    public int getOldListSize() {
        return currPlaces.size();
    }

    @Override
    public int getNewListSize() {
        return newPlaces.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        PlaceRecodeItem oldPlace = currPlaces.get(oldItemPosition);
        PlaceRecodeItem newPlace = newPlaces.get(newItemPosition);
        return Math.abs(oldPlace.getLat() - newPlace.getLat()) <= Constants.EQUALS_ZERO &&
                Math.abs(oldPlace.getLon() - newPlace.getLon()) <= Constants.EQUALS_ZERO;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        PlaceRecodeItem oldPlace = currPlaces.get(oldItemPosition);
        PlaceRecodeItem newPlace = newPlaces.get(newItemPosition);
        if (oldPlace.getState() == null) {
            if (newPlace.getState() != null) {
                return false;
            } else {
                return oldPlace.getPlace().equals(newPlace.getPlace()) &&
                        oldPlace.getCountry().equals(newPlace.getCountry());
            }
        }
        return oldPlace.getPlace().equals(newPlace.getPlace()) &&
                oldPlace.getCountry().equals(newPlace.getCountry()) &&
                oldPlace.getState().equals(newPlace.getState());
    }
}
