package asilapp.sms.com.measure.core.chat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;

import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import asilapp.sms.com.measure.ui.MainActivity;
import asilapp.sms.com.measure.ui.fragments.ChatFragment;

public class RequestTask extends AsyncTask <AIRequest, Void, AIResponse>{
    private Activity context;
    private AIDataService aiDataService;
    private AIServiceContext customAIServiceContext;
    RequestTask(Activity context,AIDataService aiDataService, AIServiceContext customAIServiceContext){
        this.context=context;
        this.aiDataService = aiDataService;
        this.customAIServiceContext = customAIServiceContext;
    }

    @Override
    protected AIResponse doInBackground(AIRequest... aiRequests) {
        final AIRequest request = aiRequests[0];
        try {
            return aiDataService.request(request, customAIServiceContext);
        } catch (AIServiceException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(AIResponse aiResponse) {
        ((MainActivity)context).callback(aiResponse);
    }

}
