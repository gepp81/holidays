package ar.com.gep.holydays.services;


import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface NoLaborableAPI {

    static final String NO_URL = "http://nolaborables.com.ar";
    static final String NO_API_PATH = "API/v1/";
    static final String NO_API_PATH_YEAR = NO_API_PATH + "{year}?excluir=opcional";

    @GET(NO_API_PATH_YEAR)
    Call<List<HolidayDTO>> getHolyDays(@Path("year") String year);
}
