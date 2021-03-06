package net.wvffle.android.pb.schedule.views.setup;

import static net.wvffle.android.pb.schedule.util.IntegersUtil.getIntFromEnd;

import android.annotation.SuppressLint;

import androidx.recyclerview.widget.LinearLayoutManager;

import net.wvffle.android.pb.schedule.ObjectBox;
import net.wvffle.android.pb.schedule.R;
import net.wvffle.android.pb.schedule.databinding.FragmentSecondSetupStepViewBinding;
import net.wvffle.android.pb.schedule.models.Schedule;
import net.wvffle.android.pb.schedule.models.Schedule_;
import net.wvffle.android.pb.schedule.util.GenericRecyclerViewAdapter;
import net.wvffle.android.pb.schedule.viewmodels.SetupViewModel;
import net.wvffle.android.pb.schedule.views.BaseView;

import java.util.List;
import java.util.stream.Collectors;

public class SecondSetupStep extends BaseView<FragmentSecondSetupStepViewBinding> {
    private final SetupViewModel viewModel;

    public SecondSetupStep(SetupViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_second_setup_step_view;
    }

    @Override
    protected void setup(FragmentSecondSetupStepViewBinding binding) {
        binding.setViewModel(viewModel);

        GenericRecyclerViewAdapter<String> adapter = new GenericRecyclerViewAdapter<>(viewModel, R.layout.adapter_item_semester);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerView.setAdapter(adapter);

        viewModel.getDegree().observe(this, degree -> {
            if (degree == null) return;

            @SuppressLint("DefaultLocale")
            List<String> semesters = ObjectBox.getScheduleBox()
                    .query()
                    .equal(Schedule_.degreeId, degree.id)
                    .build()
                    .find()
                    .stream()
                    .map(Schedule::getSemester)
                    .distinct()
                    .sorted()
                    .map(semester -> String.format(getString(R.string.semester_n), semester))
                    .collect(Collectors.toList());

            viewModel.setSemesters(semesters);
            adapter.setData(semesters);
            adapter.setOnItemClickListener((view, item, position) -> {
                viewModel.setSemester(getIntFromEnd(item));
                viewModel.setMaxStep(3);
            });
        });
    }
}