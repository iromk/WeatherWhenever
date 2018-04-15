package pro.xite.dev.weatherwhenever;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pro.xite.dev.weatherwhenever.data.Weather;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OneDayWeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneDayWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneDayWeatherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "widgetScaleFactor";
    private static final String ARG_PARAM2 = "param2";
    public static final float SIZE_L = 1f;
    public static final float SIZE_XL = 1.25f;
    public static final float SIZE_M = 0.75f;
    public static final float SIZE_S = 0.5f;
    public static final float SIZE_XS = 0.25f;

    // TODO: Rename and change types of parameters
    private Float widgetScaleFactor;
//    private TimeZone timeZone;

    private Weather weather;

    private OnFragmentInteractionListener mListener;
    private View view;
    private TextView tvTempScale;
    private ImageView iwWeatherIcon;
    private TextView tvTemp;
    private TextView tvTimestampNote;

    public OneDayWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @para m param2 Parameter 2.
     * @return A new instance of fragment OneDayWeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneDayWeatherFragment newInstance(float param1) {//}, TimeZone param2) {
        OneDayWeatherFragment fragment = new OneDayWeatherFragment();
        Bundle args = new Bundle();
        args.putFloat(ARG_PARAM1, param1);
//        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            widgetScaleFactor = getArguments().getFloat(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_day_weather, container, false);
        view.setScaleX(widgetScaleFactor);
        view.setScaleY(widgetScaleFactor);
        tvTempScale = view.findViewById(R.id.textview_tscale);
        iwWeatherIcon = view.findViewById(R.id.imageview_weather_icon);
        tvTemp = view.findViewById(R.id.textview_temp);
        tvTimestampNote = view.findViewById(R.id.textview_notes);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViews();
    }

    public void updateViews() {
        if (getView() != null) {
            tvTempScale.setVisibility(View.INVISIBLE);
            if (weather != null) {
                iwWeatherIcon.setImageResource(Helpers.getResIdByName("owm_" + weather.getIconId(), R.drawable.class));
                tvTemp.setText(Helpers.tempToString(weather.getTemperature()));
                tvTimestampNote.setText(String.format("%1$ta, %1$te %1$tb, %1$tR", weather.getCalendar()));
                tvTempScale.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
        updateViews();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
