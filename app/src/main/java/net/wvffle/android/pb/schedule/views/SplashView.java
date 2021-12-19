package net.wvffle.android.pb.schedule.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import net.wvffle.android.pb.schedule.R;
import net.wvffle.android.pb.schedule.api.DatabaseSyncService;
import net.wvffle.android.pb.schedule.databinding.FragmentSplashViewBinding;

public class SplashView extends BaseView<FragmentSplashViewBinding> {
    @Override
    int getLayoutId() {
        return R.layout.fragment_splash_view;
    }

    @Override
    void setup(FragmentSplashViewBinding binding) {
        SharedPreferences pref = requireActivity().getSharedPreferences("setup", Context.MODE_PRIVATE);
        long then = System.currentTimeMillis();
        Handler handler = new Handler();

        // NOTE: Ensure we have the latest info in the database.
        DatabaseSyncService.sync((update, isNew) -> {
            long delta = System.currentTimeMillis() - then;

            handler.postDelayed(() -> navigate(
                    pref.getBoolean("setup-done", false)
                            ? R.id.action_splashView_to_homeView
                            : R.id.action_splashView_to_setupView
            ), Math.max(0, 3000 - delta));
        });
    }
}