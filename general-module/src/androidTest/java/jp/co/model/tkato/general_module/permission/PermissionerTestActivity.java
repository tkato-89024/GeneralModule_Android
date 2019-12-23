package jp.co.model.tkato.general_module.permission;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.core.app.ActivityScenario;

import java.util.concurrent.CountDownLatch;

import jp.co.model.tkato.general_module.test.R;

public class PermissionerTestActivity extends Activity {

    private Permissioner   permissioner;
    private CountDownLatch latch;

    private Button   forceFinishButton;
    private TextView testTitleLabel;
    private View.OnClickListener onClickListener;

    private ActivityScenario.ActivityAction<PermissionerTestActivity> onResumeListener;

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
        if (null != forceFinishButton) {
            this.forceFinishButton.setOnClickListener(this.onClickListener);
        }
    }

    public void setTestTitle(String title) {
        if (null != this.testTitleLabel) {
            this.testTitleLabel.setText(title);
        }
    }

    public void setTestParameter(Permissioner permissioner, CountDownLatch latch) {
        this.permissioner = permissioner;
        this.latch = latch;
    }

    public void setOnResumeListener(ActivityScenario.ActivityAction<PermissionerTestActivity> listener) {
        this.onResumeListener = listener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissionertest);

        forceFinishButton = findViewById(R.id.activity_permissionertest_button_fail);
        testTitleLabel    = findViewById(R.id.activity_permissionertest_label_test);

        if (null != savedInstanceState) {
            final String title = savedInstanceState.getString(getString(R.id.activity_permissionertest_label_test));
            testTitleLabel.setText(title);
        }

        // rule.getScenario().onActivity(activity -> {}); 内で await してると画面固まったまま

        if (null != forceFinishButton) {
            forceFinishButton.setOnClickListener(this.onClickListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != onResumeListener) {
            onResumeListener.perform(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (null != permissioner) {
            permissioner.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != testTitleLabel) {
            outState.putString(getString(R.id.activity_permissionertest_label_test), testTitleLabel.getText().toString());
        }
    }
}
