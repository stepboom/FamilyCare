import android.app.Application;

/**
 * Created by Stepboom on 10/27/2017.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize things(s) here
        Contextor.getInstance().init(getApplicationContext());
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/TH Niramit AS Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
