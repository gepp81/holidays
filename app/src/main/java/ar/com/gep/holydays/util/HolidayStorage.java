package ar.com.gep.holydays.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.gep.holydays.MainActivity;
import ar.com.gep.holydays.services.HolidayDTO;
import ar.com.gep.holydays.services.NoLaborableAPI;
import retrofit.Response;

public class HolidayStorage {

    /**
     * @param response
     * @return
     */
    @NonNull
    private static String getYear(Response<List<HolidayDTO>> response) {
        String path = response.raw().request().url().getPath();
        int pathIndex = path.indexOf(NoLaborableAPI.NO_API_PATH);
        return path.substring(pathIndex + NoLaborableAPI.NO_API_PATH.length());
    }

    /**
     * @param response
     * @param activity
     */
    public static void saveHolidayYear(Response<List<HolidayDTO>> response, MainActivity activity) {
        String year = getYear(response);
        SharedPreferences.Editor preferences = activity.getSharedPreferences(year, Context.MODE_PRIVATE).edit();
        preferences.putString(year, new Gson().toJson(response.body()));
        preferences.commit();
    }

    /**
     * @param map
     * @param activity
     * @param date
     */
    private static void putAllHolidayByYear(Map<Long, Object> map, MainActivity activity, final DateTime date) {
        final SharedPreferences preferences = activity.getSharedPreferences(String.valueOf(date.getYear()), 1);
        if (preferences != null) {
            final List<HolidayDTO> holidays = new Gson().fromJson(preferences.getString(String.valueOf(date.getYear()), null), new TypeToken<List<HolidayDTO>>() {
            }.getType());
            for (final HolidayDTO dto : holidays) {
                map.put(new DateTime(date.getYear(), dto.getMes(), dto.getDia(), 0, 0).getMillis(), null);
            }
        }
    }

    /**
     * @param startDate
     * @param endDate
     * @param activity
     * @return
     */
    public static Map<Long, Object> getHoleyDates(DateTime startDate, final DateTime endDate, MainActivity activity) {
        Map<Long, Object> map = new HashMap<>();
        putAllHolidayByYear(map, activity, startDate);
        while (startDate.getYear() < endDate.getYear()) {
            startDate.plusYears(1);
            putAllHolidayByYear(map, activity, startDate);
        }
        return map;
    }

}