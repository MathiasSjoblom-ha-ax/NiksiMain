package ax.ha.it.fragmentsdemo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.ArrayList;
import java.util.List;

public class myViewModel extends androidx.lifecycle.ViewModel {
    private MutableLiveData<List<Advice>> advice;

    public myViewModel(SavedStateHandle savedStateHandle) {
        if(savedStateHandle.contains("advice")) {
            advice = savedStateHandle.getLiveData("advice");
        } else {
            List<Advice> adviceList = new ArrayList<>();
            advice = new MutableLiveData<>();
            advice.setValue(adviceList);
            savedStateHandle.set("advice", adviceList);
        }
    }

    public void setAdvice(MutableLiveData<List<Advice>> advice) {
        this.advice = advice;
    }

    public LiveData<List<Advice>> getAdvice() {
        return advice;
    }

    public void addAdvice(Advice newAdvice) {
        List<Advice> adviceList = advice.getValue();
        adviceList.add(newAdvice);
        advice.setValue(adviceList);
    }
}
